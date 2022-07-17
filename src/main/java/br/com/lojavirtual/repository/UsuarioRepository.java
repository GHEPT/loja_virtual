package br.com.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	@Query(value = "SELECT u FROM Usuario u WHERE u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query(value = "SELECT u FROM Usuario u WHERE u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida();

	@Query(value = "SELECT u FROM Usuario u WHERE u.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'usuario_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO usuario_acesso(usuario_id, acesso_id) values (?1, (SELECT id FROM acesso WHERE descricao = 'ROLE_USER'))")
	void insereAcessoUser(Long idUser);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "INSERT INTO usuario_acesso(usuario_id, acesso_id) values (?1, (SELECT id FROM acesso WHERE descricao = ?2 limit 1))")
	void insereAcessoUserPj(Long idUser, String acesso);

}
