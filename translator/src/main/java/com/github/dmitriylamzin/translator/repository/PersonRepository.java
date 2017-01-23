package com.github.dmitriylamzin.translator.repository;


import com.github.dmitriylamzin.dto.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PersonRepository extends ElasticsearchRepository<Person, Long> {
}
