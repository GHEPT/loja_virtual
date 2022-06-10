package br.com.lojavirtual.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.ApplicationContextLoad;
import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;
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
		
		liberacaoCors(response);
		
		/*USADO PARA VER NO POSTMAN PARA TESTE*/
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		
	}
	

	/*MÉTODO DE VALIDAÇÃO DO TOKEN - RETORNA O USUÁRIO VALIDADO OU NULL*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(HEADER_STRING);
		
		if (token != null) {
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			/*FAZENDO A VALIDAÇÃO DO TOKEN DO USUÁRIO NA REQUISIÇÃO E EXTRAINDO O USER*/
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(tokenLimpo)
					.getBody().getSubject(); /*ADMIN ou EDUARDO*/
			
			if (user != null) {
				Usuario usuario = ApplicationContextLoad
						.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if (usuario != null) {
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getPassword(), 
							usuario.getAuthorities());					
				}
			}
		}
		
		liberacaoCors(response);
		return null;
	}
	
	/*FAZENDO LIBERAÇÃO PARA ERRO DE CORS NO NAVEGADOR*/
	private void liberacaoCors(HttpServletResponse response) {
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
	}
}
