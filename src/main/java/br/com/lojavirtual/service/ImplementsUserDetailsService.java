package br.com.lojavirtual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.lojavirtual.model.Usuario;
import br.com.lojavirtual.repository.UsuarioRepository;

@Service
public class ImplementsUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = usuarioRepository.findUserByLogin(username);/* Recebe o login para a consulta */

		if (usuario == null) {
			throw new UsernameNotFoundException("O usuário não foi encontrado");
		}

		return new User(usuario.getLogin(), usuario.getPassword(), usuario.getAuthorities());
	}
}
