package com.github.dmitriylamzin.translator;


import com.github.dmitriylamzin.dockerlauncher.DockerLauncher;
import com.github.dmitriylamzin.translator.repository.AccountRepository;
import com.github.dmitriylamzin.translator.repository.PersonRepository;
import com.github.dmitriylamzin.translator.service.AccountService;
import com.github.dmitriylamzin.translator.service.BitcoinListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableScheduling
public class TranslatorConfig {

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                          MessageListenerAdapter listenerAdapter) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, new PatternTopic("bitcoins"));

    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(BitcoinListener receiver) {
    MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(receiver, "receiveBitcoin");
    listenerAdapter.setSerializer(serializer());
    return listenerAdapter;
  }

  @Bean
  RedisSerializer serializer() {
    return new JdkSerializationRedisSerializer();
  }

  @Bean
  BitcoinListener receiver(CountDownLatch latch) {
    return new BitcoinListener(latch);
  }

  @Bean
  DockerLauncher dockerLauncher() {
    Properties properties = new Properties();
    return new DockerLauncher(properties);
  }
  @Bean
  AccountService accountService(BitcoinListener receiver,
                                PersonRepository personRepository,
                                AccountRepository accountRepository,
                                DockerLauncher dockerLauncher) {
    return new AccountService(receiver, personRepository, accountRepository, dockerLauncher);
  }

  @Bean
  CountDownLatch latch() {
    return new CountDownLatch(1);
  }
  public static void main(String[] args) throws IOException {
    SpringApplication.run(TranslatorConfig.class, args);

  }

}
