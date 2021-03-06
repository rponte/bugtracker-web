package br.com.triadworks.bugtracker.interceptor;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Interceptor
@Transacional // nossa anotação customizada
public class TransacionalInterceptor implements Serializable {

	@Inject
	private EntityManager manager;

	@AroundInvoke
	public Object intercepta(InvocationContext ctx) throws Exception {

		EntityTransaction tx = manager.getTransaction();
		if (tx.isActive()) {
			return ctx.proceed();
		}
		
		System.out.println("Abrindo a transação");
		manager.getTransaction().begin(); // inicia transação
		
		Object resultado = ctx.proceed(); // invoca método e retorna resultado
		
		manager.getTransaction().commit(); // comita transação
		System.out.println("Commitando a transação");

		return resultado;
	}
}
