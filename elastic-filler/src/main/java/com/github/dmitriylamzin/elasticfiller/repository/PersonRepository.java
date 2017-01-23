package com.github.dmitriylamzin.elasticfiller.repository;


import com.github.dmitriylamzin.dto.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PersonRepository extends ElasticsearchRepository<Person, Long> {
}
