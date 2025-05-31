package com.skillnez.weathertracker.service.registration;

import com.skillnez.weathertracker.dto.LocationResponseDto;
import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.entity.Location;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    protected void saveUser(UserAuthDto userAuthDto) throws ConstraintViolationException {
        String encodedPassword = passwordEncoder.encode(userAuthDto.getPassword());
        userRepository.save(User.builder()
                .login(userAuthDto.getUsername())
                .password(encodedPassword)
                .build());
    }

    @Transactional
    public void addLocation (String username,LocationResponseDto locationResponseDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Location location = Location.builder()
                .name(locationResponseDto.getName())
                .user(user)
                .latitude(locationResponseDto.getLat())
                .longitude(locationResponseDto.getLon())
                .build();
        if (!user.getLocations().contains(location)) {
            user.getLocations().add(location);
            userRepository.update(user);
        } else {
            throw new RuntimeException("Location already added");
        }
    }
}
