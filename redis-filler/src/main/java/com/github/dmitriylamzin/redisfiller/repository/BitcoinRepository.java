package com.github.dmitriylamzin.redisfiller.repository;

import com.github.dmitriylamzin.dto.Bitcoin;
import org.springframework.data.repository.CrudRepository;

public interface BitcoinRepository extends CrudRepository<Bitcoin, Long> {
}
