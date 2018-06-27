/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package vienna.mendel.hpg.biz.service.auth.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.annotation.ApplicationScope;
import vienna.mendel.hpg.biz.service.auth.abstr.IUserService;
import vienna.mendel.hpg.model.constants.MendelPrivilege;
import vienna.mendel.hpg.model.constants.MendelRole;

/**
 * Service to implement UserDetailsService
 *
 * @author wws2003
 */
@ApplicationScope
public class MendelUserDetailsServiceForUserRoleImpl implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userService.findUserByName(userName, MendelRole.USER)
                .map(mendelUser -> {
                    // Find authorities
                    List<MendelPrivilege> privileges = userService.findPrivilegesForRole(mendelUser.getRole());
                    List<GrantedAuthority> grantedAuthorities = privileges.stream()
                            .map(privilege -> new GrantedAuthority() {
                        @Override
                        public String getAuthority() {
                            return privilege.getCode();
                        }
                    })
                            .collect(Collectors.toList());

                    // Create UserDetails instance
                    UserDetails ss = User.builder()
                            .username(mendelUser.getName())
                            .password(mendelUser.getEncodedPassword())
                            .roles(mendelUser.getRole().getName())
                            .authorities(grantedAuthorities)
                            .credentialsExpired(false)
                            .build();
                    return ss;
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
