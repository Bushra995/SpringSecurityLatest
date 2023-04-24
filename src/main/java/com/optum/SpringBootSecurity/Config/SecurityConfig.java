package com.optum.SpringBootSecurity.Config;


import com.optum.SpringBootSecurity.Service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.security.Provider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private  CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails   normalUser= User
                .withUsername("Bushra")
                .password(passwordEncoder().encode("12345"))
                .roles("NORMAL")
                .build();

        UserDetails adminUser = User
                .withUsername("Bushra1")
                .password(passwordEncoder().encode("12345"))
                .roles("ADMIN")
                .build();


       InMemoryUserDetailsManager inMemoryUserDetailsManager= new InMemoryUserDetailsManager(normalUser,adminUser);
       return inMemoryUserDetailsManager;


    }

    //from database
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.customUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//
//        return new CustomUserDetailService();
//    }




    //we wilk set the password ans username and alsos et which api to be public or pricate
    //what kindaconfigurtion we want for m based or basic one
    //setting home/public to public and others endpoint to private
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        //role based authentication which url will be u
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
//                .requestMatchers("/home/admin/**").hasRole("ADMIN")//if there are multiple url start with admin/**  ex:http://localhost:8080/home/admin/bushra
//                .requestMatchers("/home/normal/**").hasRole("NORMAL") //ex:http://localhost:8080/home/normal
                .requestMatchers("/home/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin();
        return  httpSecurity.build();
    }
    //and in properties file i am configuRaing my user

}
