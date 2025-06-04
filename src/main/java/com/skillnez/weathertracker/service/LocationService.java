package com.skillnez.weathertracker.service;

import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.exception.LocationAddingException;
import com.skillnez.weathertracker.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationService {

    private final UserRepository userRepository;

    public LocationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addLocation(String username, LocationResponseDto locationResponseDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Location location = Location.builder()
                .name(locationResponseDto.getName())
                .user(user)
                .latitude(locationResponseDto.getLat())
                .longitude(locationResponseDto.getLon())
                .build();
        try {
            user.getLocations().add(location);
            userRepository.update(user);
        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            throw new LocationAddingException("Location already added in your profile");
        }
    }

    @Transactional
    public void deleteLocation(Long locationId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.getLocations().removeIf(location -> location.getId().equals(locationId));
        userRepository.update(user);
    }

}
