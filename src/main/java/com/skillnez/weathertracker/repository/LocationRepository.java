package com.skillnez.weathertracker.repository;

import com.skillnez.weathertracker.entity.Location;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository extends BaseRepository<Long, Location> {

    @Autowired
    protected LocationRepository(SessionFactory sessionFactory) {
        super(Location.class, sessionFactory);
    }
}
