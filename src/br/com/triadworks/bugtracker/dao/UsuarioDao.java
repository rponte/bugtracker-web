package br.com.triadworks.bugtracker.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.triadworks.bugtracker.modelo.Grupo;
import br.com.triadworks.bugtracker.modelo.Usuario;
import br.com.triadworks.bugtracker.util.JPAUtil;

@Transactional
@Repository
public class UsuarioDao implements Serializable {
	
	@PersistenceContext
	private EntityManager manager;

	public List<Usuario> lista() {
		return manager.createQuery("select u from Usuario u", Usuario.class)
				.getResultList();
	}

	public void adiciona(Usuario usuario) {
		usuario.getGrupos().add(new Grupo("ROLE_USUARIO"));
		manager.persist(usuario);
	}

	public void atualiza(Usuario usuario) {
		// recarrega grupos
		Usuario usuarioAntigo = busca(usuario.getId());
		usuario.setGrupos(usuarioAntigo.getGrupos());
		// atualiza
		manager.merge(usuario);
	}

	public void remove(Usuario usuario) {
		manager.remove(manager.merge(usuario));
	}

	public Usuario busca(Integer id) {
		return manager.find(Usuario.class, id);
	}

	public Usuario buscaPor(String login, String senha) {
		try {
			return manager
					.createQuery(
							"select u from Usuario u "
									+ "where u.login = :login and u.senha = :senha", Usuario.class)
					.setParameter("login", login)
					.setParameter("senha", senha)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
		}
	}

	public Usuario buscaPorLogin(String login) {
		try {
			return manager
					.createQuery(
							"select distinct u from Usuario u "
									+ "left join fetch u.grupos "
									+ "where u.login = :login", Usuario.class)
					.setParameter("login", login)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}		
	}

}
