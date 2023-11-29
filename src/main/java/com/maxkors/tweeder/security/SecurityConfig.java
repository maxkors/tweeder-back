package com.maxkors.tweeder.security;

import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.security.auth.message.config.AuthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//    private final AuthenticationProvider authenticationProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(HttpMethod.GET, "/tweets").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/profiles").hasRole(RoleName.ROLE_ADMIN.value())
//                        .requestMatchers(HttpMethod.GET, "/tweets/liked").authenticated()
//                        .requestMatchers("/tweets/{id}/like").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/profile").authenticated()
                        .anyRequest().denyAll())
                .sessionManagement(configurer -> configurer.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin(configurer -> configurer
//                        .permitAll()
//                        .loginProcessingUrl("/login")
//                        .usernameParameter("username")
//                        .passwordParameter("password")
//                        .successHandler((req, res, auth) -> {
//                            res.setStatus(HttpStatus.OK.value());
//                            res.sendRedirect("/api/profile");
//                        })
//                        .failureHandler((req, res, ex) -> res.sendError(HttpStatus.UNAUTHORIZED.value())))
//                .rememberMe(configurer -> configurer
//                        .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
//                        .tokenRepository()
//                        .key("remember_me_secret_key"))
//                .logout(configurer -> configurer
//                        .permitAll()
//                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.POST.name()))
//                        .clearAuthentication(true)
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID", "remember-me")
//                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.UNAUTHORIZED.value())))
//                .exceptionHandling(configurer -> configurer
//                        .authenticationEntryPoint((req, res, authEx) -> res.sendError(HttpStatus.UNAUTHORIZED.value())))
        ;

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
         var authProvider = new DaoAuthenticationProvider();
//         authProvider.setUserDetailsService(userDetailsService());
        authProvider.setUserDetailsService(userDetailsService);
         authProvider.setPasswordEncoder(passwordEncoder());
         return authProvider;
    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        return new UserDetailsServiceImpl(userRepository);
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/signin").allowedOrigins("http://localhost:3000", "*");
//            }
//        };
//    }
}
