package com.learn.database.services;

import com.learn.database.domain.entities.AuthorEntity;
import com.learn.database.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    AuthorEntity save(AuthorEntity author);

    List<AuthorEntity> findAll();

    Optional<AuthorEntity> findOne(Long id);

    boolean isExists(Long id);
}
