package com.skillnez.weathertracker.repository;

import com.skillnez.weathertracker.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<Long, User>{

    @Autowired
    public UserRepository(SessionFactory sessionFactory) {
        super(User.class, sessionFactory);
    }

    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession().createQuery(
                "select u from User u where u.login = :username", User.class)
                .setParameter("username", username).uniqueResultOptional();
    }

    public void deleteAll () {
        sessionFactory.getCurrentSession().createQuery("delete from User").executeUpdate();
    }
}
