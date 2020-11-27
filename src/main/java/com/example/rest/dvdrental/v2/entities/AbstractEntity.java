package com.example.rest.dvdrental.v2.entities;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(of="id")
public abstract class AbstractEntity<ID extends Serializable> {
    public abstract ID getId();
    public abstract void setId(ID id);
}
