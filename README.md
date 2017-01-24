# deep-server-side-translator

Application structure:

![alt tag](http://oi67.tinypic.com/dczlt0.jpg)


You have 2 Docker containers. On the first container, pure ElasticSearch is installed, on another container - Redis.


On the diagram on top of each container fillers are attached

ElasticFiller - microservice which get data from elastic-data.json file and every 30 seconds fills ElasticSearch
with this data. But only in quantity of 50 ids.

That is, every 30 seconds, the database is filled with 50 followed by rows of id / first_name and so on.

RedisFiller does the same. But data for Redis is taken from redis-data.xml. Fills with the same periodic.

Testing data gets from: https://goo.gl/a1zqTs


Simple rules for filler services

* Every 30 seconds in the both services should be implemented through @Scheduled by Spring.

* "Every next 50 ids" is filled by batching.

* RedisFiller uses StAX.

* ElasticFiller uses Jackson (but not databind).

* Services uses GRADLE as a Build tool.

* Both services run their Docker containers remotely by Docker Java Client.


Below your third TranslatorService microservis, which takes data from the already filled containers with docker.
The only difference is in the fact that all 3 of your service will start the same way, so the bottom "may" wait for
the data, but if he sees them, he takes away.


How he does it?

He takes data from Redis and ElasticSearch and combines them by id, that is, if from elasticSearch comes
ID: 1 Name: Masha Surname: Ivanova, and so on, then it is added to the same ID Bitcoin from Redis and this ready
glued full record puts in MongoDB which is installed you locally.


This pet-project helps to understand the:


* Spring Data/Scheduled, Concurrency Basics

* Gradle/Docker/DockerClient/

* ElasticSearch/Redis/Jackson/StAX

The task is complicated, so if some processes seem strange to you - you know, it's made for I to meet new stack of
technologies.
