package com.github.dmitriylamzin.dockerlauncher;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.jaxrs.ListContainersCmdExec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class DockerLauncher {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private final Properties properties;

  private DockerClient dockerClient;

  private CreateContainerResponse containerResponse;

  public DockerLauncher(Properties properties) {
    this.dockerClient = DockerClientBuilder.getInstance()
            .build();

    this.properties = properties;
  }

  public void launch(){
    ExposedPort exposedPort = ExposedPort.tcp(Integer.valueOf(properties.getProperty("port")));
    Ports portBindings = new Ports();
    portBindings.bind(exposedPort,
            new Ports.Binding(properties.getProperty("host"), properties.getProperty("port")));


    Info info =  dockerClient.infoCmd().exec();

    logger.info("number of running containers: " + info.getContainersRunning());

    containerResponse = dockerClient.createContainerCmd(properties.getProperty("image"))
            .withExposedPorts(exposedPort)
            .withPortBindings(portBindings)
            .exec();

    logger.info("starting " + properties.getProperty("image") + " container");

    dockerClient.startContainerCmd(containerResponse.getId()).exec();

    Info info1 =  dockerClient.infoCmd().exec();

    List<Container> containers = dockerClient.listContainersCmd().exec();

    containers.stream().forEach(container -> logger.info("container: " +
            container.getImage() +
            " " + container.getStatus()));


    logger.info("number of running containers: " + info1.getContainersRunning());


    /*
      thread sleeping to ensure that container
      is ready for requests
     */
    try {
      Thread.sleep(5000L);
    } catch (InterruptedException e){
      logger.error("thread was interrupted");
    }

    List<Container> containers1 = dockerClient.listContainersCmd().exec();


    containers1.stream().forEach(container -> logger.info("container: " +
            container.getImage() +
            " " + container.getStatus()));

  }

  public void stopAllContainers(){

    List<Container> containers = dockerClient.listContainersCmd().exec();

    for (Container container : containers) {
      dockerClient.stopContainerCmd(container.getId()).exec();
    }

    List<Container> containers1 = dockerClient.listContainersCmd().exec();


    containers1.stream().forEach(container -> logger.info("container: " +
            container.getImage() +
            " " + container.getStatus()));

  }

  @PreDestroy
  public void stopContainer(){
    logger.info("closing container " + properties.getProperty("image"));
    dockerClient.stopContainerCmd(containerResponse.getId()).exec();

    logger.info("removing container " + properties.getProperty("image"));
    dockerClient.removeContainerCmd(containerResponse.getId()).exec();

    List<Container> containers1 = dockerClient.listContainersCmd().exec();


    containers1.stream().forEach(container -> logger.info("container: " +
            container.getImage() +
            " " + container.getStatus()));

  }
}
