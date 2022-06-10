package br.com.lojavirtual.security;

import javax.servlet.http.HttpSessionListener;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.lojavirtual.service.ImplementsUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {
	
	@Autowired
	private ImplementsUserDetailsService implementsUserDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()
		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/*REDIRECIONA OU DÁ UM RETORNO PARA O INDEX QUANDO DESLOGA DO SISTEMA*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		/*MAPEIA A PARTE DE LOGOUT*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		/*FILTRA AS REQUISIÇÕES PARA LOGIN DE JWT*/
		.and().addFilterAfter(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
		.addFilterBefore(new JWTApiAutenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/*Irá consultar o user no banco com o Spring Security*/
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementsUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers(HttpMethod.GET, "/salvarAcesso", "/deletarAcesso").antMatchers(HttpMethod.POST, "/salvarAcesso", "/deletarAcesso");
	}

}
