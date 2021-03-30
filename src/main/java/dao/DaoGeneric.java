package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import pos_java_maven_hibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.geEntityManager();
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void salvar(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}
	
	public E updateMerge(E entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		
		return entidadeSalva;
	}
	public E pesquisar(E entidade) {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		E e = (E) entityManager.find(entidade.getClass(), id);
		return  e;
	}
	
	public E pesquisar(Long id, Class<E> entidade) {
		entityManager.clear();
		E e = (E) entityManager.createQuery("from " + entidade.getSimpleName() + " where id = " + id).getSingleResult();
		
		return  e;
	}
	
	public void deletePorId(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.createNativeQuery("DELETE FROM " + entidade.getClass().getSimpleName().toLowerCase() + " WHERE id =" + id).executeUpdate(); // faz o delete
		transaction.commit();//grava a alteração no banco de dados
	}
	
	public List<E> listar(Class<E> entidade){
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		List<E> lista = entityManager.createQuery("FROM " + entidade.getName()).getResultList();
		transaction.commit();
		
		return lista;
	}
 	
}
