/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vienna.mendel.hpg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import vienna.mendel.hpg.model.constants.MendelRole;

/**
 * Security configuration for admin role
 *
 * @author wws2003
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(1)
public class SecurityAdminRoleConfig extends WebSecurityConfigurerAdapter {

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
                .loginProcessingUrl("/authenticate") // This is not the URL to process login form !
                .and()
                // Logout config
                .logout()
                .logoutUrl("/logout").permitAll()
                .permitAll()
                .logoutSuccessUrl("/auth/adminLogin?logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .roles(MendelRole.ADMIN.getName())
                .password("password");
    }
}
