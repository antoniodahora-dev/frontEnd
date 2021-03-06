package com.example.algamoneyapi.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoneyapi.config.property.AlgamoneyApiProperty;

@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	//iremos adicionar para que possamos alterar o false para True
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	

	//ira cancelar/retirar o token do cookie
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps()); // em producao será true
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);
		
		resp.addCookie(cookie);
		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}
}

/* CLASSE QUE REALIZAR O LOGOUT DO SISTEMA REMOVENDO O COOKIE
 * 
 */
