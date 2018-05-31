/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vienna.mendel.hpg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import vienna.mendel.hpg.model.constants.MendelRole;

/**
 * Security configuration file for the whole application
 *
 * @author wws2003
 */
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        // ensure the passwords are encoded properly
        UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }

    @Configuration
    @Order(1)
    public static class SecurityAdminRoleConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Path config
                    .antMatcher("/admin/**")
                    .authorizeRequests()
                    .anyRequest().hasRole(MendelRole.ADMIN.getName())
                    .and()
                    // Login config
                    .formLogin()
                    .loginPage("/auth/adminLogin").permitAll()
                    .failureUrl("/auth/adminLogin?error=1")
                    .loginProcessingUrl("/admin_authenticate") // This is not the URL to process login form !
                    .and()
                    // Logout config
                    .logout()
                    .logoutUrl("/logout").permitAll()
                    .permitAll()
                    .logoutSuccessUrl("/auth/adminLogin?logout");
        }
    }

    @Configuration
    @Order(2)
    public static class SecurityUserRoleConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Path config
                    .antMatcher("/user/**")
                    .authorizeRequests()
                    .anyRequest().hasRole(MendelRole.USER.getName())
                    .and()
                    // Login config
                    .formLogin()
                    .loginPage("/auth/userLogin").permitAll()
                    .failureUrl("/auth/userLogin?error=1")
                    .loginProcessingUrl("/user_authenticate")// This is not the URL to process login form !
                    .and()
                    // Logout config
                    .logout()
                    .logoutUrl("/logout").permitAll()
                    .logoutSuccessUrl("/auth/userLogin?logout");
        }
    }

    @Configuration
    @Order(3)
    public static class SecurityPublicConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // Path config
                    .authorizeRequests()
                    .antMatchers("/", "/favicon.ico", "/resources/**", "/about", "/public/**")
                    .permitAll();
        }
    }

}
