package com.ihordev.helloservice.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Generic data access object (DAO) interface.
 * 
 */
public interface GenericDAO<E, ID extends Serializable>
{
    void persist(E entity);

    E merge(E entity);

    void remove(E entity);

    E findById(ID id);

    E findReferenceById(ID id);

    List<E> findAll();
}