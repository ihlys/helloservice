package com.ihordev.helloservice.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ihordev.helloservice.dao.GenericDAO;

/**
 * 
 * An abstract generic data access object (DAO) implementation for any entity.
 * 
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class GenericDAOImpl<E, ID extends Serializable> implements GenericDAO<E, ID>
{

    @PersistenceContext
    private EntityManager entityManager;
    private Class<E> entityClass;

    public GenericDAOImpl(Class<E> entityClass)
    {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    public void persist(E entity)
    {
        getEntityManager().persist(entity);
    }

    @Override
    public E merge(E entity)
    {
        return getEntityManager().merge(entity);
    }

    @Override
    public void remove(E entity)
    {
        getEntityManager().remove(entity);
    }

    @Override
    public E findById(ID id)
    {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public E findReferenceById(ID id)
    {
        return getEntityManager().getReference(entityClass, id);
    }

    @Override
    public List<E> findAll()
    {
        CriteriaQuery<E> q = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        q.select(q.from(entityClass));
        return getEntityManager().createQuery(q).getResultList();
    }

}
