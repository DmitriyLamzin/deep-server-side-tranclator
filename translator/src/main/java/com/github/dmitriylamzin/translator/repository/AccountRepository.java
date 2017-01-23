package com.github.dmitriylamzin.translator.repository;

import com.github.dmitriylamzin.dto.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, Long> {
}
