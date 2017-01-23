package com.github.dmitriylamzin.elasticfiller.domain;

import com.github.dmitriylamzin.dto.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class PersonQueue implements ItemWriter<Person>, ItemReader<Person>, JobExecutionListener {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private Queue<Person> personQueue;

  private boolean isComplete = false;

  public PersonQueue(Queue<Person> personQueue) {
    this.personQueue = personQueue;
  }

  public PersonQueue() {
    this.personQueue = new ConcurrentLinkedQueue<>();
  }

  @Override
  public void write(List<? extends Person> items) {
    logger.debug("writing items to queue " + items);
    personQueue.addAll(items);
  }

  @Override
  public Person read() {
    logger.debug("reading item from queue");
    return personQueue.poll();
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    //do nothing before job execution
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    setIsComplete(true);
  }

  public boolean isComplete() {
    return isComplete;
  }

  public boolean isEmpty() {
    return personQueue.isEmpty();
  }

  public void setIsComplete(boolean isComplete) {
    logger.info("queue is complete");
    this.isComplete = isComplete;
  }

}
