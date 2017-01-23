package com.github.dmitriylamzin.elasticfiller;

import com.github.dmitriylamzin.dockerlauncher.DockerLauncher;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class ElasticFillerApplication implements ApplicationContextAware {

  private static final String TRANSPORT_CLIENT_PROPERTIES = "classpath:transportclient.properties";
  private static final String ELASTIC_CONTAINER_PROPERTIES = "classpath:elastic-container.properties";

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private Environment environment;

  private TransportClient client;

  @Bean
  public DockerLauncher getDockerLauncher() throws IOException {
    Properties properties = new Properties();
    properties.load(applicationContext.getResource(ELASTIC_CONTAINER_PROPERTIES).getInputStream());
    return new DockerLauncher(properties);
  }

  @Bean
  public TransportClientFactoryBean getTransportClientFactoryBean() throws Exception {

    getDockerLauncher().launch();

    TransportClientFactoryBean transportClientFactoryBean = new TransportClientFactoryBean();

    transportClientFactoryBean.setClusterNodes("192.168.99.100:9300");
    transportClientFactoryBean.setClientTransportSniff(true);
    transportClientFactoryBean.setClusterName("elasticsearch");

    final Properties transportClientProperties = new Properties();
    transportClientProperties.load(applicationContext.getResource(TRANSPORT_CLIENT_PROPERTIES).getInputStream());

    transportClientFactoryBean.setProperties(transportClientProperties);

    return transportClientFactoryBean;
  }


	public static void main(String[] args) {
		SpringApplication.run(ElasticFillerApplication.class, args);

	}

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

  }
}
