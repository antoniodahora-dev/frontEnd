package com.example.algamoneyapi.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoneyapi.config.property.AlgamoneyApiProperty;

@ControllerAdvice
@CrossOrigin("http://localhost:4200")
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		//devemos inserir para que o mesmo localize o post do token
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		//Devemos converter o ServerHttpRequeste e o ServerHttpResponse em HttpServletRequeste e HttpServletResponse
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();
		
		//solucao para tirar o RefreshToken do Body da aplicacao
		DefaultOAuth2AccessToken token =(DefaultOAuth2AccessToken) body;
		
		//criamos a string para que o RefreshToken seja pego e enviado para o cookie
		String refreshToken = body.getRefreshToken().getValue();
	
		//devemos criar também o metodo adicionarRefreshTokenNoCooKie
		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		
		//removendo o token do refreshToken
		//devemos criar o metodo removerRefreshTokenBody
		removerRefreshTokenTokenBody(token);
		
		//ao invés de null devemos retornar o body
		return body;
	}

	
	private void removerRefreshTokenTokenBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);		
	}
	
	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		
		//Criação do Cookie para armazenar o ResfreshToken
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true); // não permitira o java script ter acesso ao RefreshToken
		refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); // Mudar pra true em modo producao
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token"); // requisão do ContextPaths
		refreshTokenCookie.setMaxAge(2492000);//tempo de expiracao do Token no Cookie - 2492000
		resp.addCookie(refreshTokenCookie); // irá retornar o cookie na resposta da solicitacao
	}

}
