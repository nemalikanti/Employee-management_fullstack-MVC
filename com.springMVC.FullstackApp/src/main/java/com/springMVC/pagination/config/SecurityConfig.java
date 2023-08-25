package com.springMVC.pagination.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize
			.antMatchers("/employee/").hasAuthority("USER")
			.anyRequest().authenticated())
			.formLogin()
			.and().httpBasic()
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/employee").deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.clearAuthentication(true);
			
	} 
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("Rajesh").password("Rajesh@123").authorities("admin").and()
			.withUser("Ameen").password("Ameen@123").authorities("read").and()
			.passwordEncoder(NoOpPasswordEncoder.getInstance());
			}
}
