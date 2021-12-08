package org.loktevik.netcracker.security;

import lombok.RequiredArgsConstructor;
import org.loktevik.netcracker.filter.AuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**").antMatchers("/js/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login", "/sign-in/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/login", "/customers").permitAll();
        http.authorizeRequests().antMatchers("/home", "/customers", "/update-info").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/**", "/addresses/**", "/paidtypes/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(new AuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  extends WebSecurityConfigurerAdapter{
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http.authorizeRequests().antMatchers("/").permitAll();
//        http.csrf().disable();
//    }
//}