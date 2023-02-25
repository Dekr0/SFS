package com.ece.sfs.injection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;


@Configuration
public class SecurityConfig {

    @Bean
    @Scope("singleton")
    public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    @Scope("singleton")
    public AuthenticationManager authManager(DaoAuthenticationProvider authProvider) {
        return new ProviderManager(
                List.of(authProvider)
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    @Scope("singleton")
    public InMemoryUserDetailsManager userManager(GrantedAuthority adminAuthority) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(
                new User("admin",
                        passwordEncoder().encode("admin"),
                        List.of()
                ));

        return manager;
    }
}
