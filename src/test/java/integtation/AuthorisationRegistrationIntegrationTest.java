package integtation;


import com.skillnez.weathertracker.config.TestConfig;
import com.skillnez.weathertracker.dto.UserAuthDto;
import com.skillnez.weathertracker.entity.Session;
import com.skillnez.weathertracker.entity.User;
import com.skillnez.weathertracker.exception.UserAlreadyExistsException;
import com.skillnez.weathertracker.repository.SessionRepository;
import com.skillnez.weathertracker.repository.UserRepository;
import com.skillnez.weathertracker.service.authorization.AuthFacadeService;
import com.skillnez.weathertracker.service.authorization.SessionService;
import com.skillnez.weathertracker.service.registration.RegisterFacadeService;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AuthorisationRegistrationIntegrationTest {

    @Autowired
    private SessionService sessionService;

    @Autowired private UserRepository userRepository;

    @Autowired private RegisterFacadeService registerFacadeService;

    @Autowired private SessionRepository sessionRepository;

    @Autowired private SessionFactory sessionFactory;

    @Autowired private AuthFacadeService authFacadeService;

    @Value("${session.lifetime.seconds}")
    private int lifetimeSeconds;

    @Test
    @Transactional
    void userShouldBeRegistered() {
        int usersCountBeforeRegistration = userRepository.findAll().size();
        UserAuthDto userAuthDto = new UserAuthDto("Username", "Password", "Password");
        registerFacadeService.registerUser(userAuthDto);
        int usersCountAfterRegistration = userRepository.findAll().size();
        assertTrue(usersCountAfterRegistration > usersCountBeforeRegistration);
    }

    @Test
    @Transactional
    void shouldGetException() {
        UserAuthDto userAuthDto = new UserAuthDto("Username", "Password", "Password");
        registerFacadeService.registerUser(userAuthDto);
        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            registerFacadeService.registerUser(userAuthDto);
        });
        assertEquals("Account with this username " + userAuthDto.getUsername() + " already exists"
                , exception.getMessage());
    }

    @Test
    @Transactional
    void sessionShouldBeCreated() {
        int sessionsCountBeforeRegistration = sessionRepository.findAll().size();
        UserAuthDto userAuthDto = new UserAuthDto("Username", "Password", "Password");
        registerFacadeService.registerUser(userAuthDto);
        authFacadeService.login(userAuthDto);
        int sessionsCountAfterRegistration = sessionRepository.findAll().size();
        assertTrue(sessionsCountAfterRegistration > sessionsCountBeforeRegistration);
    }

    @Test
    @Transactional
    void sessionShouldBeExpired() {
        User user = userRepository.save(User.builder()
                .login("Username")
                .password("Password")
                .build());
        Session session = sessionRepository.save(
                Session.builder().user(user).expiresAt(LocalDateTime.now().minusMinutes(1)).build()
        );
        assertTrue(sessionService.isSessionExpired(session));
    }

    @Test
    @Transactional
    void sessionShouldNotBeExpired() {
        User user = userRepository.save(User.builder()
                .login("Username")
                .password("Password")
                .build());
        Session session = sessionRepository.save(
                Session.builder().user(user).expiresAt(LocalDateTime.now().plusSeconds(lifetimeSeconds)).build()
        );
        assertFalse(sessionService.isSessionExpired(session));
    }

    @AfterEach
    @Transactional
    void tearDown() {
        sessionFactory.getCurrentSession().clear();
        userRepository.deleteAll();

    }
}
