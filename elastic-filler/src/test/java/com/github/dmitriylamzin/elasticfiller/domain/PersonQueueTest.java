package com.github.dmitriylamzin.elasticfiller.domain;

import com.github.dmitriylamzin.dto.Person;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PersonQueueTest {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Test
  public void writeToPersonQueue() {
    logger.info("test of writing to person queue");
    Queue<Person> queue = new ConcurrentLinkedQueue<>();

    PersonQueue personQueue = new PersonQueue(queue);

    List<Person> personList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Person person = new Person();
      person.setEmail(i + "@email.ru");
      person.setId(i);
      personList.add(person);
    }

    personQueue.write(personList);

    assertTrue(queue.size() == 10);
  }

  @Test
  public void readFromPersonQueue() {
    logger.info("testing of reading from person queue");

    Queue<Person> queue = new ConcurrentLinkedQueue<>();

    for (int i = 0; i < 10; i++) {
      Person person = new Person();
      person.setEmail(i + "@email.ru");
      person.setId(i);
      queue.add(person);
    }

    PersonQueue personQueue = new PersonQueue(queue);

    List<Person> personList = new ArrayList<>();

    Person firstPersonInTheQueue = personQueue.read();

    assertEquals(firstPersonInTheQueue.getEmail(), "0@email.ru");

    Person secondPersonInTheQueue = personQueue.read();

    assertEquals(secondPersonInTheQueue.getEmail(), "1@email.ru");
  }

}
