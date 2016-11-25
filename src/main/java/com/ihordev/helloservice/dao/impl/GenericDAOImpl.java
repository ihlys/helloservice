package com.ihordev.helloservice.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ihordev.helloservice.dao.GenericDAO;

/**
 * This is an abstract generic Data access object (DAO) implementation for any entity.
 */
@Transactional(propagation = Propagation.REQUIRED)
public abstract class GenericDAOImpl<E, ID extends Serializable> implements GenericDAO<E, ID>
{

    private Class<E> entityClass;
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    public GenericDAOImpl(Class<E> entityClass)
    {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void persist(E entity)
    {
        getCurrentSession().persist(entity);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E merge(E entity)
    {
        return (E) getCurrentSession().merge(entity);
    }

    @Override
    public void remove(E entity)
    {
        getCurrentSession().remove(entity);
    }

    @Override
    public E findById(ID id)
    {
        return getCurrentSession().find(entityClass, id);
    }

    @Override
    public E findReferenceById(ID id)
    {
        return getCurrentSession().getReference(entityClass, id);
    }

    @Override
    public List<E> findAll()
    {
        CriteriaQuery<E> q = getCurrentSession().getCriteriaBuilder().createQuery(entityClass);
        q.select(q.from(entityClass));
        return getCurrentSession().createQuery(q).getResultList();
    }

}
