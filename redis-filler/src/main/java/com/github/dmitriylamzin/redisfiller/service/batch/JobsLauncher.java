package com.github.dmitriylamzin.redisfiller.service.batch;


import com.github.dmitriylamzin.redisfiller.domain.BitcoinQueue;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class JobsLauncher {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());


  @Autowired
  private Job importPersonJob;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private BitcoinQueue bitcoinQueue;

  @Scheduled(fixedDelay = 3000)
  public void launch() throws JobParametersInvalidException,
          JobExecutionAlreadyRunningException,
          JobRestartException,
          JobInstanceAlreadyCompleteException,
          ExecutionException,
          InterruptedException, IOException {

    if ((bitcoinQueue.isReady() && !bitcoinQueue.isEmpty())) {
      logger.debug("scheduled task is performed");
      JobParameters jobParameters = new JobParametersBuilder()
              .addString("JOB_START_DATE", (new LocalDateTime()).toString())
              .toJobParameters();

      jobLauncher.run(importPersonJob, jobParameters);
    }
  }
}
