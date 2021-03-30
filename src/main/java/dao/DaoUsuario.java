package dao;

import model.UsuarioPessoa;

public class DaoUsuario<E> extends DaoGeneric<UsuarioPessoa>{
	
	
	public void removerUsario(UsuarioPessoa pessoa) throws Exception{
	getEntityManager().getTransaction().begin();
	
	getEntityManager().remove(pessoa);
	 
	getEntityManager().getTransaction().commit();
	
}
	
}
