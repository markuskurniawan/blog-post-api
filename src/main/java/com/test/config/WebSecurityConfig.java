package com.test.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	AuthenticationProvider provider;

    public WebSecurityConfig(final AuthenticationProvider authenticationProvider) {
        super();
        this.provider=authenticationProvider;
    }
    
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
    	http.csrf().disable()
    		.cors().and()
    		.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
    		.authorizeRequests()
    		.antMatchers("/login", "/ping", "/h2-console/**")
    		.permitAll()
    		.anyRequest().authenticated().and()
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    		.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and();
    	
    	http.addFilterAt(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
