package com.github.dmitriylamzin.elasticfiller.service.batch;

import com.github.dmitriylamzin.dto.Person;
import com.github.dmitriylamzin.elasticfiller.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


public class ElasticsearchItemWriter implements ItemWriter<Person>, ItemWriteListener<Person>, StepExecutionListener {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private PersonRepository personRepository;

  private StepExecution stepExecution;

  public ElasticsearchItemWriter() {
  }

  @Autowired
  public ElasticsearchItemWriter(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }


  @Override
  public void write(List<? extends Person> items) throws Exception {
    logger.debug("person items " + items);
    personRepository.save(items);

  }

  @Override
  public void beforeWrite(List<? extends Person> items) {
    //Nothing to do
  }

  @Override
  public void afterWrite(List<? extends Person> items) {
    stepExecution.setTerminateOnly();

    stepExecution.setStatus(BatchStatus.COMPLETED);
    stepExecution.setExitStatus(ExitStatus.COMPLETED);
  }


  @Override
  public void onWriteError(Exception exception, List<? extends Person> items) {
    //Nothing to do
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }

}
