package com.example.rest.dvdrental.v2.repository;

import com.example.rest.dvdrental.v2.entities.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface GenericRepository<E extends AbstractEntity<ID>, ID extends Serializable>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
    
}
