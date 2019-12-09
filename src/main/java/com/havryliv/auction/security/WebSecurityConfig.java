package com.havryliv.auction.security;

import com.havryliv.auction.configuration.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors().and()
                .csrf().disable()

                .httpBasic().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()//
                .antMatchers("/users/login", "/actuator/**").permitAll()//
                .antMatchers("/users/register").permitAll()//
                .antMatchers("/h2-console/**/**").permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedPage("/login").and()
                .apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/**/v2/api-docs", "/**/configuration/ui", "/**/swagger-resources",
                "/**/configuration/security", "/**/swagger-ui.html", "/**/webjars/**", "/**/static/**", "/**/styles/**", "/**/favicon.ico",
                "/**/swagger-resources/configuration/ui").and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

}
