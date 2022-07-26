package com.youngjo.ssg.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngjo.ssg.global.enumeration.Role;
import com.youngjo.ssg.global.security.filter.JwtRequestProcessingFilter;
import com.youngjo.ssg.global.security.handler.JwtAuthenticationFailureHandler;
import com.youngjo.ssg.global.security.handler.JwtAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String LOGIN_PROCESSING_URL = "/api/login";
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler;
    private final ObjectMapper objectMapper;
    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.logout().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        http.headers().frameOptions().disable();
        http.rememberMe().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/**").permitAll();

        http.addFilterAfter(new JwtRequestProcessingFilter("/api/*"), SecurityContextPersistenceFilter.class);
        http.addFilterBefore(corsFilter, SecurityContextPersistenceFilter.class);
        applyJwtConfigurer(http);
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(String.format("%s > %s", Role.ROLE_ADMIN, Role.ROLE_NORMAL));
        return roleHierarchy;
    }

    @Bean
    public AccessDecisionVoter<?> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    private void applyJwtConfigurer(HttpSecurity http) throws Exception {
        http.apply(new JwtLoginConfigurer<>(LOGIN_PROCESSING_URL))
                .addAuthenticationManager(authenticationManager())
                .loginSuccessHandler(jwtAuthenticationSuccessHandler)
                .loginFailureHandler(jwtAuthenticationFailureHandler)
                .objectMapper(objectMapper);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider);
    }
}
