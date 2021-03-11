package com.example.algamoneyapi.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoneyapi.config.token.CustomTokenEnhancer;

@Profile("oauth-security")
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig  extends AuthorizationServerConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients)throws Exception {
		
		clients.inMemory()
				.withClient("angular")
				.secret("$2a$10$NFbpn0LWLFTw3L2TrZnw0e7esnEDm3mhz9F4g3tEP818tPcLbpyEG")
				.scopes("read" , "write")
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(25) //1800
				.refreshTokenValiditySeconds(360) // 3600 * 24
			.and()
				.withClient("mobile")
				.secret("$2a$10$kvnBnzhwd0sh2yor2DUpaOiclFzwH405jk1xJDO1NVDqXr5VBJ4Uu")
				.scopes("read") //além da permissão de usuario via BD, podemos adicionar permissao pelo scope.
				.authorizedGrantTypes("password", "refresh_token")
				.accessTokenValiditySeconds(30)//1800
				.refreshTokenValiditySeconds(3600 * 24);// 3600 * 24
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception{
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
				.tokenStore(tokenStore())
				.accessTokenConverter(accessTokenConverter())
				.tokenEnhancer(tokenEnhancerChain)
				.reuseRefreshTokens(false)
				.userDetailsService(this.userDetailsService)
				.authenticationManager(this.authenticationManager);
	}
	
	// Este metodo é para permitir acesso ao Token JWT
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
		
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks");
		return accessTokenConverter;
		
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
		
	}
}
