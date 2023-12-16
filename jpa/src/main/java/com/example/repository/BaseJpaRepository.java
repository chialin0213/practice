package com.example.repository;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import javax.persistence.EntityManager;

public abstract class BaseJpaRepository<T,ID> extends SimpleJpaRepository<T,ID> {
    public EntityManager em;

    BaseJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
    }
}
