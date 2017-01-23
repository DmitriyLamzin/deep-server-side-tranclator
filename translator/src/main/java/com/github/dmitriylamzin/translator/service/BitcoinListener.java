package com.github.dmitriylamzin.translator.service;

import com.github.dmitriylamzin.dto.Bitcoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BitcoinListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinListener.class);

  private CountDownLatch latch;

  private List<Observer> observers = new ArrayList<>();

  @Autowired
  public BitcoinListener(CountDownLatch latch) {
    this.latch = latch;
  }

  public void receiveBitcoin(Bitcoin bitcoin) {
    LOGGER.info("Received <" + bitcoin + ">");
    for (Observer observer : observers) {
      observer.update(bitcoin);
    }
    latch.countDown();
  }

  public void registerObserver(Observer observer) {
    LOGGER.info("registering new observer " + observer);
    observers.add(observer);
  }

  public List<Observer> getObservers() {
    return observers;
  }

}
