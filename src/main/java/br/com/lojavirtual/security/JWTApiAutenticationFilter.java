package br.com.lojavirtual.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import br.com.lojavirtual.ExceptionLojaVirtual;

/*FILTRO ONDE TODAS AS REQUISIÇÕES SERÃO CAPTURADAS PARA AUTENTICAR*/
public class JWTApiAutenticationFilter extends GenericFilterBean {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
		
			/*ESTABELECE A AUTENTICAÇÃO DO USUÁRIO*/
			Authentication authentication = new JWTTokenAutenticationService()
					.getAuthentication(
							(HttpServletRequest) request, 
							(HttpServletResponse) response);
			
			/*PROCESSO DE AUTENTICAÇÃO COM O SPRING SECURITY*/
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			chain.doFilter(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("** Ocorreu um erro no sistema - avise o administrador: **\n" + e.getMessage());
		}
	}
}
