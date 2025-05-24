package com.skillnez.weathertracker.service;

import com.skillnez.weathertracker.dto.UserRegistrationDto;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.UserRepository;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public boolean isUserAlreadyRegistered(UserRegistrationDto user) {
        try {
            return userRepository.findByUsername(user.getUsername()).isPresent();
        } catch (NonUniqueResultException e) {
            //Оставил для себя, на случай если кто-то руками в бд несмотря на констрейнт
            //сделает одинаковых юзеров и запрос в репозитории выбросит эксепшн
            log.error(e.getMessage());
            throw new IllegalStateException("User with username " + user.getUsername() + " already exists");
        }
    }

}
