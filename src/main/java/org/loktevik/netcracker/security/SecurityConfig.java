package org.loktevik.netcracker.security;

import lombok.RequiredArgsConstructor;
import org.loktevik.netcracker.filter.AuthorizationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login", "/sign-in/**", "/logout").permitAll();
        http.authorizeRequests().antMatchers(POST, "/login", "/customers", "/logout").permitAll();
        http.authorizeRequests().antMatchers("/home", "/customers", "/update-info", "/catalogue").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers("/admin/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/api/**", "/addresses/**", "/paidtypes/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .deleteCookies("access_token", "refresh_token")
                .permitAll();
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