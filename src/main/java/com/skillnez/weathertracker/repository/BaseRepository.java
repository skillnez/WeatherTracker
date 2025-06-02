package com.skillnez.weathertracker.repository;

import com.skillnez.weathertracker.entity.BaseEntity;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    protected final SessionFactory sessionFactory;
    private final Class<E> clazz;

    protected BaseRepository(Class<E> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public E save(E entity) {
        sessionFactory.getCurrentSession().persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
        sessionFactory.getCurrentSession().remove(sessionFactory.getCurrentSession().get(clazz, id));
    }

    @Override
    public void update(E entity) {
        sessionFactory.getCurrentSession().merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(clazz, id));
    }

    @Override
    public List<E> findAll() {
        return sessionFactory.getCurrentSession().createQuery("FROM " + clazz.getSimpleName(), clazz).getResultList();
    }
}
