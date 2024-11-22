package csit.semit.kde.javaspringwebappskdelab3.config;

import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom implementation of the {@link RoleHierarchy} interface to define role hierarchies in the application.
 * <p>
 * This class is annotated with `@Component`, indicating that it is a Spring-managed bean.
 * It provides a method to determine the reachable granted authorities based on the current authorities.
 * </p>
 * <p>
 * The `getReachableGrantedAuthorities` method takes a collection of granted authorities and returns a set of
 * reachable authorities, including additional roles based on predefined hierarchies.
 * </p>
 * <p>
 * For example, if a user has the role of `CASHIER` or `TRAIN_MANAGER`, they will also be granted the `USER` role.
 * If a user has the `ADMIN` role, they will be granted all other roles except `ADMIN`.
 * </p>
 * <p>
 * This configuration ensures that role-based access control is properly managed and that users have the necessary
 * permissions based on their roles.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see org.springframework.security.access.hierarchicalroles.RoleHierarchy
 * @see org.springframework.security.core.GrantedAuthority
 * @see org.springframework.security.core.authority.SimpleGrantedAuthority
 * @see org.springframework.stereotype.Component
 * @since 1.0.0
 */
@Component
public class MyRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> reachableAuthorities = new HashSet<>(authorities);

        for (GrantedAuthority authority : authorities) {
            if (("ROLE_" + Role.CASHIER.name()).equals(authority.getAuthority()) || ("ROLE_" + Role.TRAIN_MANAGER.name()).equals(authority.getAuthority())) {
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));
            }
            if (("ROLE_" + Role.ADMIN.name()).equals(authority.getAuthority())) {
                reachableAuthorities.addAll(
                        Arrays.stream(Role.values())
                                .filter(role -> role != Role.ADMIN)
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                                .toList()
                );
            }
        }
        return reachableAuthorities;
    }
}
