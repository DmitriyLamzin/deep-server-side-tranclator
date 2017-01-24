package com.github.dmitriylamzin.redisfiller.service.batch;

import com.github.dmitriylamzin.dto.Bitcoin;
import com.github.dmitriylamzin.redisfiller.repository.BitcoinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisBitcoinWriter implements ItemWriter<Bitcoin>, ItemWriteListener<Bitcoin>, StepExecutionListener {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private BitcoinRepository bitcoinRepository;

  private StepExecution stepExecution;

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public void afterWrite(List<? extends Bitcoin> items) {
    stepExecution.setTerminateOnly();

    stepExecution.setStatus(BatchStatus.COMPLETED);
    stepExecution.setExitStatus(ExitStatus.COMPLETED);
  }

  @Override
  public void write(List<? extends Bitcoin> items) throws Exception {
    logger.debug("bitcoin items: " + items);
    items.stream()
            .forEach(item -> redisTemplate.convertAndSend("bitcoins", item));
    bitcoinRepository.save(items);
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;

  }

  @Override
  public void beforeWrite(List<? extends Bitcoin> items) {
  }

  @Override
  public void onWriteError(Exception exception, List<? extends Bitcoin> items) {
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }
}
