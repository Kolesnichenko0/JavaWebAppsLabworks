package csit.semit.kde.javaspringwebappskdelab3.config;

import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;

/**
 * Security configuration class for setting up Spring Security in the application.
 * <p>
 * This class is annotated with `@Configuration` and `@EnableWebSecurity`, indicating that it is a source of bean definitions
 * and enables Spring Security's web security support.
 * </p>
 * <p>
 * It provides a bean of type `SecurityFilterChain` that configures HTTP security, including CSRF protection,
 * authorization rules, form login, logout, exception handling, and session management.
 * </p>
 * <p>
 * The `securityFilterChain` method configures various security aspects such as:
 * <ul>
 *   <li>Permitting access to public endpoints like static resources, home, login, and error pages.</li>
 *   <li>Requiring authentication for profile access and other secured endpoints.</li>
 *   <li>Defining role-based access control for different endpoints based on user roles like `TRAIN_MANAGER`, `CASHIER`, `ADMIN`, and `USER`.</li>
 *   <li>Configuring form login with custom login page, success URL, and failure URL.</li>
 *   <li>Setting up logout functionality with custom logout success URL and session invalidation.</li>
 *   <li>Handling exceptions for authentication entry point and access denied scenarios.</li>
 *   <li>Managing session settings including invalid session URL, maximum sessions, and expired session URL.</li>
 * </p>
 * <p>
 * This configuration ensures that the application is secured with proper authentication and authorization mechanisms,
 * promoting secure access control practices.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 * @see org.springframework.security.web.SecurityFilterChain
 * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see org.springframework.security.crypto.password.PasswordEncoder
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                                // Public endpoints
                                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers("/", "/home", "/login", "/project-description", "/error", "/error/{errorCode}").permitAll()

                                // Requires authentication
                                .requestMatchers("/profile").authenticated()

                                // ROLE endpoints
                                .requestMatchers("/trains/create").hasRole(Role.TRAIN_MANAGER.name())
                                .requestMatchers("/train-tickets/create").hasRole(Role.CASHIER.name())
                                .requestMatchers("/users/create").hasRole(Role.ADMIN.name())
//                              .requestMatchers("/trains", "/trains/{id}", "/trains/{id}/tickets").hasRole(Role.USER.name())
                                .requestMatchers("/trains/**").hasRole(Role.USER.name())
//                               .requestMatchers("/train-tickets", "/train-tickets/{id}").hasRole(Role.USER.name())
                                .requestMatchers("/train-tickets/**").hasRole(Role.USER.name())

                                //ROLE REST API endpoints
                                .requestMatchers(HttpMethod.GET, "/api/users/current").authenticated()

//                                .requestMatchers(HttpMethod.GET, "/api/trains/{id}/tickets").hasRole(Role.USER.name())
//                                .requestMatchers(HttpMethod.GET, "/api/trains/{id}").hasRole(Role.USER.name())
//                                .requestMatchers(HttpMethod.GET, "/api/trains").hasRole(Role.USER.name())
//                                .requestMatchers(HttpMethod.GET, "/api/train-tickets/{id}").hasRole(Role.USER.name())
//                                .requestMatchers(HttpMethod.GET, "/api/train-tickets").hasRole(Role.USER.name())

                                .requestMatchers(HttpMethod.GET, "/api/trains/**").hasRole(Role.USER.name())
                                .requestMatchers(HttpMethod.GET, "/api/train-tickets/**").hasRole(Role.USER.name())

                                .requestMatchers(HttpMethod.POST, "/api/trains/export").hasAnyRole(Role.USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/trains/{id}/tickets/export").hasAnyRole(Role.USER.name())
                                .requestMatchers(HttpMethod.PUT, "/api/trains/{id}").hasAnyRole(Role.TRAIN_MANAGER.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/trains/{id}").hasAnyRole(Role.TRAIN_MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/api/trains").hasAnyRole(Role.TRAIN_MANAGER.name())
                                .requestMatchers(HttpMethod.POST, "/api/train-tickets/export").hasAnyRole(Role.USER.name())
                                .requestMatchers(HttpMethod.PATCH, "/api/train-tickets/{id}").hasAnyRole(Role.CASHIER.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/train-tickets/{id}").hasAnyRole(Role.CASHIER.name())
                                .requestMatchers(HttpMethod.POST, "/api/train-tickets").hasAnyRole(Role.CASHIER.name())
                                .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole(Role.ADMIN.name())

                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", false)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            String contextPath = request.getContextPath();
                            response.sendRedirect(contextPath + "/error/401");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            String contextPath = request.getContextPath();
                            response.sendRedirect(contextPath + "/error/403");
                        })
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?sessionExpired=true")
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(false)
                        .expiredUrl("/login?sessionInvalidated=true")
                );

        return http.build();
    }
}