package ru.danilakondr.testsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.danilakondr.testsystem.auth.AuthFilter;
import ru.danilakondr.testsystem.services.UserService;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserService service;

    private final AuthFilter authFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(service);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint unauthenticatedUsersEntryPoint() {
        return new CustomUnauthenticatedUsersEntryPoint();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((req) -> req
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/api/logout").authenticated()
                        .requestMatchers("/api/changePassword").authenticated()
                        .requestMatchers("/api/requestPasswordReset").permitAll()
                        .requestMatchers("/api/resetPassword").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tests/*").hasAuthority("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/api/myTests").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/test/*", "/api/question/*", "/api/testSession/*").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/test/*", "/api/question/*", "/api/testSession/*").authenticated()
                         .requestMatchers(HttpMethod.PATCH, "/api/test/*/reorderQuestions").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/test/*", "/api/question/*", "/api/testSession/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/test", "/api/question", "/api/testSession").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/participant/*").hasAnyAuthority("ORGANIZATOR", "ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/api/participant").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/participant/answer").hasAuthority("PARTICIPANT")
                        .requestMatchers(HttpMethod.POST, "/api/participant/complete").hasAuthority("PARTICIPANT")
                        .requestMatchers(HttpMethod.GET, "/api/testSession/*/reportAll.json").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/admin/systemInfo").hasAuthority("ADMINISTRATOR")
                )
                .exceptionHandling((exp) -> exp.accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(unauthenticatedUsersEntryPoint()))
                .addFilterAfter(authFilter, UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }
}
