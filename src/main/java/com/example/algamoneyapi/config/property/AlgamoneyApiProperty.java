package com.example.algamoneyapi.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 *Classe que ira alternar de modo externo os "Todo" de false/true 
 */

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {
	
	private String originPermitida = "https://projeto-algamoney-ui.herokuapp.com/";
	
	//pertmir acesso a porta de forma externa
	//private String originPermitida = getOriginPermitida();
	
	private final Seguranca seguranca = new Seguranca ();
	
	
	public Seguranca getSeguranca() {
		return seguranca;
	}

	
	
	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}





	public static class Seguranca {
		
		private boolean enableHttps;


		public boolean isEnableHttps() {
			return enableHttps;
		}


		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
		
	}

}
