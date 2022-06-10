package br.com.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * Essa classe terá duas funções:
 * 1 - Criar a autenticação;
 * 2 - Retornar a autenticação
 * */

@Service
@Component
public class JWTTokenAutenticationService {
	
	/*TOKEN DE VALIDADE DE 11 DIAS*/
	private static final long EXPIRATION_TIME = 959990000;
	
	/*CHAVE DE SENHA PARA JUNTAR COM O JWT*/
	private static final String SECRET = "client-secret*req_2022*";

	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*VAMOS GERAR O TOKEN E DAR A RESPOSTA PARA O CLIENTE COM O JWT*/
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		/*MONTAGEM DO TOKEN*/
		String JWT = Jwts.builder()/*CHAMA O GERADOR DE TOKEN*/
				.setSubject(username)/*ADICIONA O USER*/
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/*TEMPO DE EXPIRAÇÃO */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		String token = TOKEN_PREFIX + " " + JWT;
		
		/*DÁ O RETORNO PARA A TELA E PARA O CLIENTE - QUE PODE SER OUTRA API, NAVEGADOR, APP, ETC*/
		response.addHeader(HEADER_STRING, token);
		
		/*USADO PARA VER NO POSTMAN PARA TESTE*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
}
