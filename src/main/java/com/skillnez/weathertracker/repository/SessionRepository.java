package com.skillnez.weathertracker.repository;

import com.skillnez.weathertracker.entity.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class SessionRepository extends BaseRepository<UUID,Session> {

    protected SessionRepository(SessionFactory sessionFactory) {
        super(Session.class, sessionFactory);
    }

    public void deleteExpiredSessions() {
        sessionFactory.getCurrentSession()
                .createQuery("delete from Session s where s.expiresAt < :now")
                .setParameter("now", LocalDateTime.now()).executeUpdate();
    }

}
