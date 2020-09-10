package com.example.algamoneyapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // habilita a seguranca nos metodos
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	
	/*@Autowired
	private UserDetailsService userDetailsService;
	
	//metodo solicitação autenticação básica para acesso as informações
	/*@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("admin").password("admin").roles("ROLE");
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}*/


	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()  // .antMatchers("/categoria").permitAll() -> permite acesso a opçao cateria sem necessidade de senha
				.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.csrf().disable();
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.stateless(true);
	}
	
	//O objeto que ira realizar a seguranca dos metodos atraves do OAuth2
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler(){
		return new OAuth2MethodSecurityExpressionHandler();
	}
	
	/*
	
	@Bean
	public UserDetailsService userDetailsService() {
	    User.UserBuilder builder = User.withDefaultPasswordEncoder();
	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	    manager.createUser(builder.username("admin").password("admin").roles("ROLE").build());
	    return manager;
	}*/
	
}
