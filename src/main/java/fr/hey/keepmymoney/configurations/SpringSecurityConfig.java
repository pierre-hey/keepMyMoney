package fr.hey.keepmymoney.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {


    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((authorize) -> authorize
                                // Autorise tout le monde à accéder à la lecture des ressources CSS, JS, img
                                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                                // Autorise tout le monde à accéder à la page d'index via les 2 urls
                                .requestMatchers("/", "/index").permitAll()
                                // Autorise tout le monde à accéder à la page d'enregistrement
                                .requestMatchers("/register/**").permitAll()
                                // Autorise tout le monde
                                .requestMatchers("/poc/all").permitAll()
                                // Autorise uniquement les utilisateurs connectés
                                .requestMatchers("/poc/auth").authenticated()
                                // Autorise uniquement les utilisateurs connectés avec le role "ADMIN"
                                .requestMatchers("/poc/admin").hasRole("ADMIN")
                                // Autorise uniquement les utilisateurs connectés avec le role "USER"
                                .requestMatchers("/poc/user").hasRole("USER")
//                                .requestMatchers("/static/**").permitAll().anyRequest().permitAll()
                                // .requestMatchers(antMatcher("/users/**")).hasRole("ADMIN")

//                                .requestMatchers("/register/**").permitAll()
//                                .requestMatchers("/register/**").permitAll()
//                                .requestMatchers("/css/**").permitAll()
//                                .requestMatchers(antMatcher("/css/**")).permitAll()
//                                .requestMatchers("/user/**").hasRole("USER")
//                                .requestMatchers("/users").hasRole("ADMIN")
                                .requestMatchers("/admin/**").hasRole("ADMIN")

                                //.requestMatchers("/templates/fragments/header.html").permitAll()
                                .anyRequest().authenticated()


                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .usernameParameter("login") // -> Définit quel champ du formulaire est
                                // le "userName" du "UserDetail" de Spring qui correspond
                                // au champ discriminant de l'identification
                                //.loginProcessingUrl("/login")
                                .defaultSuccessUrl("/index")
                                .permitAll()
                )

                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );


        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

  /*  User factice, en mémoire, pour dev
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("password")
                .roles("ADMIN", "USER")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }*/

}