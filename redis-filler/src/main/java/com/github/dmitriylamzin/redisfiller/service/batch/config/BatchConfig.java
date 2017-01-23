package com.github.dmitriylamzin.redisfiller.service.batch.config;

import com.github.dmitriylamzin.dto.Bitcoin;
import com.github.dmitriylamzin.redisfiller.domain.BitcoinQueue;
import com.github.dmitriylamzin.redisfiller.service.batch.RedisBitcoinWriter;
import com.github.dmitriylamzin.redisfiller.service.parse.XmlToBitcoinParser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfig implements ApplicationContextAware {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Bean
  public ItemWriter<Bitcoin> writer(){
    return new RedisBitcoinWriter();
  }

  @Bean
  public XmlToBitcoinParser xmlToBitcoinParser(){
    return new XmlToBitcoinParser();
  }
  @Bean
  public BitcoinQueue queueService() throws IOException {
    XmlToBitcoinParser xmlToBitcoinParser = xmlToBitcoinParser();

    BitcoinQueue bitcoinQueue = xmlToBitcoinParser.
            parseFile(applicationContext.getResource("redis-data.xml").getFile().getPath());

    return bitcoinQueue;
  }

  @Bean
  public Step redisFillStep() throws IOException {
    return stepBuilderFactory.get("redisFillStep")
            .<Bitcoin, Bitcoin> chunk(50)
            .reader(queueService())
            .writer(writer())
            .listener(writer())
            .build();
  }

  @Bean
  public Job importBitcoinJob() throws IOException {
    return jobBuilderFactory.get("importBitcoinJob")
            .incrementer(new RunIdIncrementer())
            .start(redisFillStep())
            .build();
  }


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

  }
}
