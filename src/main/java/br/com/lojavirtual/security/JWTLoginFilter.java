package br.com.lojavirtual.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lojavirtual.model.Usuario;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	/*CONFIGURANDO O GERENCIADOR DE AUTENTICAÇÃO*/
	public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		/*OBRIGA AUTENTICAR A URL*/
		super(new AntPathRequestMatcher(url));
		/*GERENCIADOR DE AUTENTICAÇÃO*/
		setAuthenticationManager(authenticationManager);
	}

	/*RETORNA O USUÁRIO AO PROCESSAR A AUTENTICAÇÃO*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		/*OBTER O USUÁRIO*/
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		/*RETORNA USER COM LOGIN E SENHA*/
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(
						user.getLogin(), 
						user.getSenha()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		try {
			new JWTTokenAutenticationService().addAuthentication(response,authResult.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		if (failed instanceof BadCredentialsException) {
			response.getWriter().write("Erro de login e senha - NOT FOUND");
		}
		else {
			response.getWriter().write("Falha ao logar: " +failed.getMessage());
		}
		//super.unsuccessfulAuthentication(request, response, failed);
	}
}
