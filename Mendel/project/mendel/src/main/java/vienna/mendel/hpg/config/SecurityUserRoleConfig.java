package vienna.mendel.hpg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import vienna.mendel.hpg.model.constants.MendelRole;

/**
 * Security configuration for User Role
 *
 * @author wws2003
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Order(2)
class SecurityUserRoleConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // Path config
                .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/resources/**", "/about", "/public/**").permitAll()
                .anyRequest().hasRole(MendelRole.USER.getName())
                .and()
                // Login config
                .formLogin()
                .loginPage("/auth/userLogin").permitAll()
                .failureUrl("/auth/userLogin?error=1")
                .loginProcessingUrl("/authenticate") // This is not the URL to process login form !
                .and()
                // Logout config
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/auth/userLogin?logout");
    }

    @Bean(name = "userDetailsService")
    @Override
    protected UserDetailsService userDetailsService() {
        // Refer from https://spring.io/guides/gs/securing-web/
        UserDetails users = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(users);
    }

    @Bean(name = "authenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
