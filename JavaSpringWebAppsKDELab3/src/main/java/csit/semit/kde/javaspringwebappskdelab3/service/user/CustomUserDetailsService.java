package csit.semit.kde.javaspringwebappskdelab3.service.user;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import csit.semit.kde.javaspringwebappskdelab3.service.user.UserService;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        ServiceResult<UserDTO> result = userService.findByUsernameOrEmail(usernameOrEmail);
        if (result.getStatus() != ServiceStatus.SUCCESS) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDTO userDTO = result.getEntity();
        String password = userService.findPasswordById(userDTO.getId());

//        Role[] roles = switch (userDTO.getRole()) {
//            case ADMIN -> new Role[]{Role.USER, Role.ADMIN};
//            case CASHIER -> new Role[]{Role.USER, Role.CASHIER};
//            case TRAIN_MANAGER -> new Role[]{Role.USER, Role.TRAIN_MANAGER};
//            default -> new Role[]{userDTO.getRole()};
//        };

        return User.builder()
                .username(userDTO.getUsername())
                .password(password)
                .roles(userDTO.getRole().name())
//                .roles(roles.toArray(new String[0]))
                .build();
    }
}