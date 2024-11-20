package csit.semit.kde.javaspringwebappskdelab3.service.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import csit.semit.kde.javaspringwebappskdelab3.repository.user.UserRepository;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    private UserCreateDTO createValidUserDTO() {
        return new UserCreateDTO(
                "user_123",
                "user@example.com",
                "Password1!",
                "Іван",
                Role.USER
        );
    }

    @Test
    public void testCreateUser() {
        UserCreateDTO userDTO = createValidUserDTO();

        ServiceResult<UserDTO> result = userService.save(userDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("user_123", result.getEntity().getUsername());

        User savedUser = userRepository.findById(result.getEntity().getId()).orElse(null);
        assertNotNull(savedUser);
        assertTrue(passwordEncoder.matches("Password1!", savedUser.getPassword()));
    }

    @Test
    public void testCreateUserDuplicate() {
        UserCreateDTO userDTO = createValidUserDTO();
        userService.save(userDTO);
        ServiceResult<UserDTO> result = userService.save(userDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testCreateUserInvalidEmail() {
        UserCreateDTO userDTO = createValidUserDTO();
        userDTO.setEmail("invalid-email");
        ServiceResult<UserDTO> result = userService.save(userDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateUserInvalidPassword() {
        UserCreateDTO userDTO = createValidUserDTO();
        userDTO.setPassword("short");
        ServiceResult<UserDTO> result = userService.save(userDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testFindByKeySet() {
        UserCreateDTO userDTO = createValidUserDTO();
        userService.save(userDTO);

        ServiceResult<UserDTO> result = userService.findByKeySet("user_123", "user@example.com");

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
    }

    @Test
    public void testFindByUsernameOrEmail() {
        UserCreateDTO userDTO = createValidUserDTO();
        userService.save(userDTO);

        ServiceResult<UserDTO> result = userService.findByUsernameOrEmail("user_123");

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
    }

    @Test
    public void testSaveAll() {
        List<UserCreateDTO> users = new ArrayList<>();
        users.add(createValidUserDTO());

        UserCreateDTO user2 = createValidUserDTO();
        user2.setUsername("user_124");
        user2.setEmail("user2@example.com");
        users.add(user2);

        ServiceResult<UserDTO> result = userService.saveAll(users);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals(2, result.getEntityList().size());
    }

    @Test
    public void testSaveAllDuplicate() {
        List<UserCreateDTO> users = new ArrayList<>();
        UserCreateDTO user = createValidUserDTO();
        users.add(user);
        users.add(user);

        ServiceResult<UserDTO> result = userService.saveAll(users);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }
}