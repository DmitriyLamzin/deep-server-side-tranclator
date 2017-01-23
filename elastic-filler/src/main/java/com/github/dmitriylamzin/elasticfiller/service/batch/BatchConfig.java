package com.github.dmitriylamzin.elasticfiller.service.batch;


import com.github.dmitriylamzin.dto.Person;
import com.github.dmitriylamzin.elasticfiller.domain.PersonQueue;
import com.github.dmitriylamzin.elasticfiller.repository.PersonRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class BatchConfig {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private PersonRepository personRepository;


  @Bean
  public FlatFileItemReader<Person> reader() {
    FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
    reader.setResource(new ClassPathResource("elastic-data.json"));
    reader.setLineMapper(new PersonLineMapper());
    return reader;
  }

  @Bean
  public ItemWriter<Person> writer(){
    return new ElasticsearchItemWriter(personRepository);
  }

  @Bean
  public PersonQueue personQueue(){
    return new PersonQueue();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
            .<Person, Person> chunk(50)
            .reader(personQueue())
            .writer(writer())
            .listener(writer())
            .build();
  }

  @Bean
  public Job importPersonJob() {
    return jobBuilderFactory.get("importPersonJob")
            .incrementer(new RunIdIncrementer())
            .start(step1())
            .build();
  }

  @Bean
  public Step readFromFileStep() {
    return stepBuilderFactory.get("readFromFileStep")
            .<Person, Person> chunk(50)
            .reader(reader())
            .writer(personQueue())
            .build();
  }

  @Bean
  public Step completeQueueWriting() {
    return stepBuilderFactory.get("completeQueueWriting")
            .<Person, Person> chunk(50)
            .reader(reader())
            .writer(personQueue())
            .build();
  }

  @Bean
  public Job readPersonsFromFile() {
    return jobBuilderFactory.get("readPersonsFromFile")
            .incrementer(new RunIdIncrementer())
            .listener(personQueue())
            .flow(readFromFileStep())
            .end()
            .build();
  }
}
