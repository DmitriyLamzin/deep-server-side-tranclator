package com.github.dmitriylamzin.translator.service;

import com.github.dmitriylamzin.dockerlauncher.DockerLauncher;
import com.github.dmitriylamzin.dto.Account;
import com.github.dmitriylamzin.dto.Bitcoin;
import com.github.dmitriylamzin.dto.Person;
import com.github.dmitriylamzin.translator.repository.AccountRepository;
import com.github.dmitriylamzin.translator.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AccountService implements Observer {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);


  private BitcoinListener bitcoinListener;
  private PersonRepository personRepository;
  private AccountRepository accountRepository;
  private DockerLauncher dockerLauncher;

  private int countsOfEmptyAccountsQueueCalls = 0;

  private Queue<Account> accounts = new ConcurrentLinkedQueue<>();
  private Stack<Account> stack = new Stack<>();

  @Autowired
  public AccountService(BitcoinListener bitcoinListener,
                        PersonRepository personRepository,
                        AccountRepository accountRepository,
                        DockerLauncher dockerLauncher) {

    LOGGER.info("initializing");
    LOGGER.info("bitcoin " + bitcoinListener);
    this.bitcoinListener = bitcoinListener;
    bitcoinListener.registerObserver(this);

    LOGGER.info("person repository " + personRepository);
    this.personRepository = personRepository;

    LOGGER.info("account repository " + accountRepository);
    this.accountRepository = accountRepository;

    this.dockerLauncher = dockerLauncher;
  }


  @Override
  public void update(Bitcoin bitcoin) {
    LOGGER.info("updating accounts with bitcoin " + bitcoin);
    Account account = new Account();
    account.setBitcoin(bitcoin);
    account.setId(bitcoin.getId());
    accounts.add(account);
    findMatches();
  }

  @Scheduled(fixedDelay = 30000)
  public void findMatchesBySchedule() {
    if (accounts.size() == 0) {
      countsOfEmptyAccountsQueueCalls++;
      LOGGER.info("Counts of empty account queue = " + countsOfEmptyAccountsQueueCalls);
    } else {
      LOGGER.info("Queue is not empty");
      countsOfEmptyAccountsQueueCalls = 0;
      findMatches();
    }
    if (countsOfEmptyAccountsQueueCalls >= 5) {
      LOGGER.info("No data during 5 periods, suppose that data is finished");
      LOGGER.info("Closing all dockers");
      dockerLauncher.stopAllContainers();
      System.exit(0);
    }

  }

  private void findMatches() {
    for (int i = 0; i < accounts.size(); i++) {
      Account account = accounts.poll();
      Person person = personRepository.findOne(account.getId());

      if (person != null) {
        LOGGER.info("Found person with id " + account.getId());
        LOGGER.info("Person info " + person.toString());
        account.setPerson(person);
        LOGGER.info("Save full account to MongoDB " + account.toString());
        accountRepository.save(account);
        LOGGER.info("Removing account from Queue " + account.toString());
      } else {
        accounts.add(account);
        LOGGER.info("Person with id " + account.getId() + " hasn't been indexed yet.");
      }
    }
  }

  public int getAccountsSize() {
    return accounts.size();
  }

  public Queue<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(Queue<Account> accounts) {
    this.accounts = accounts;
  }


}
