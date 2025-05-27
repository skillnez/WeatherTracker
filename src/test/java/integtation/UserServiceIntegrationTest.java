package integtation;


import com.skillnez.weathertracker.config.TestConfig;
import com.skillnez.weathertracker.repository.UserRepository;
import com.skillnez.weathertracker.service.registration.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = TestConfig.class)
@TestPropertySource (locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void databaseShouldBeEmpty() {
        assertEquals(0, userRepository.findAll().size());
    }
}
