package com.github.dmitriylamzin.redisfiller;

import com.github.dmitriylamzin.dockerlauncher.DockerLauncher;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class RedisFillerApplication implements ApplicationContextAware {

  private static final String REDIS_CONTAINER_PROPERTIES = "classpath:redis-container.properties";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private Environment environment;

  @Bean
  public DockerLauncher getDockerLauncher() throws IOException {
    Properties properties = new Properties();
    properties.load(applicationContext.getResource(REDIS_CONTAINER_PROPERTIES).getInputStream());
    DockerLauncher dockerLauncher = new DockerLauncher(properties);
    dockerLauncher.launch();
    return dockerLauncher;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
  }

  public static void main(String[] args) {
    SpringApplication.run(RedisFillerApplication.class, args);
  }
}
