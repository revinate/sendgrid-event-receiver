package com.revinate.sendgrid.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Collections;

@Configuration
@ConditionalOnMissingClass(name = "org.springframework.test.context.junit4.SpringJUnit4ClassRunner")
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${security.user.name}")
    private String username;

    @Value("${security.user.password}")
    private String password;

    @Value("${security.users.sendgrid.name}")
    private String sendgridUsername;

    @Value("${security.users.sendgrid.password}")
    private String sendgridPassword;

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_SENDGRID");
        return roleHierarchy;
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(expressionHandler);
        return webExpressionVoter;
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        return new AffirmativeBased(Collections.singletonList(webExpressionVoter()));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser(username).password(password).roles("ADMIN").and()
                    .withUser(sendgridUsername).password(sendgridPassword).roles("SENDGRID");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/interface/sendgrid/*")
                .authorizeRequests()
                    .anyRequest().hasRole("SENDGRID")
                    .accessDecisionManager(accessDecisionManager())
                .and()
                .httpBasic().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }
}
