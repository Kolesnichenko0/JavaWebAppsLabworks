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

@Component
public class MyRoleHierarchy implements RoleHierarchy {
    @Override
    public Collection<GrantedAuthority> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<GrantedAuthority> reachableAuthorities = new HashSet<>(authorities);

        for (GrantedAuthority authority : authorities) {
            if (("ROLE_" + Role.CASHIER.name()).equals(authority.getAuthority()) || ("ROLE_" + Role.TRAIN_MANAGER.name()).equals(authority.getAuthority())) {
                reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));
            }
            if(("ROLE_"+ Role.ADMIN.name()).equals(authority.getAuthority())) {
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
