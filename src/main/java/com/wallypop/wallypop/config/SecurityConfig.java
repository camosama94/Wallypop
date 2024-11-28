package com.wallypop.wallypop.config;

import com.wallypop.wallypop.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;
        //CAMBIAR COOKIES
        //CAMBIAR COOKIES
        //CAMBIAR COOKIES
        //CAMBIAR COOKIES
        //CAMBIAR COOKIES
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/wallypop/registro").permitAll()
                .requestMatchers("/wallypop/alta").permitAll()
                .requestMatchers("/wallypop/anuncio/ver/**").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/imagesAnuncios/**").permitAll()
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/wallypop/borrar/**").authenticated()
                .requestMatchers("/anuncios/*/fotos/*/delete").authenticated()
                .anyRequest().authenticated()
        )
        .formLogin(
                form -> form
                .loginPage("/")
                .loginProcessingUrl("/login") //ruta para procesar login
                .defaultSuccessUrl("/")
                .failureUrl("/?errorLogin")
                .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
        )
        .rememberMe(rememberMe -> rememberMe
                .key("uniqueAndSecret")
                .rememberMeParameter("remember-me")
                .useSecureCookie(true)
                .alwaysRemember(true)
                .tokenValiditySeconds(5*24*60*60)
                .userDetailsService(customUserDetailsService)
        )
        .requestCache(requestCache -> requestCache.disable()); // Desactivar Request Cache para evitar reintentos automáticos


        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(customUserDetailsService);
        return provider;
    }

    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();

        UserDetails userDetails1 = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
        return new InMemoryUserDetailsManager(userDetails, userDetails1);
    }*/
}
