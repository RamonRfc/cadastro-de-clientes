package pos_java_maven_hibernate;

import java.util.List;

import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	
	@Test
	public void testeHibernateUtil() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		
		pessoa.setIdade(47);
		pessoa.setLogin("teste");
		pessoa.setNome("Marcelo Ramos ");
		pessoa.setSenha("125");
		pessoa.setSobrenome("Fresdsas");
		
		
		daoGeneric.salvar(pessoa);
	}
	
	@Test
	public void testeBuscar(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = new UsuarioPessoa();
		pessoa.setId(1L);
		
		pessoa = daoGeneric.pesquisar(pessoa);
		System.out.println(pessoa);
		
	}
	
	@Test
	public void testeBuscar2(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L,UsuarioPessoa.class);
		System.out.println(pessoa);
		
	}
	
	@Test
	public void testeUpdate(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L,UsuarioPessoa.class);
		pessoa.setIdade(99);
		pessoa.setNome("Ramon Freitas da Costa");
		pessoa.setSenha("321");
		pessoa = daoGeneric.updateMerge(pessoa);
		
		System.out.println(pessoa);
		
	}
	
	@Test
	public void testeDelete() throws Exception{
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		UsuarioPessoa pessoa = daoGeneric.pesquisar(1L,UsuarioPessoa.class);
		daoGeneric.deletePorId(pessoa);
		
		System.out.println(pessoa);
		
	}
	
	@Test
	public void testeConsultar(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);
	
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.print(usuarioPessoa);
			System.out.print("----------------");
		}		
	}
	@Test
	public void testeQueryList(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa").getResultList();
	
				for (UsuarioPessoa usuarioPessoa : list) {
					System.out.println(usuarioPessoa);
				}
	}
	
	@Test
	public void testeQueryListMaxResult(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa order by nome").setMaxResults(3).getResultList();
	
				for (UsuarioPessoa usuarioPessoa : list) {
					System.out.println(usuarioPessoa);
				}
	}
	
	@Test
	public void testeQueryListParameter(){
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createQuery("from UsuarioPessoa where nome = :nome").setParameter("nome", "Ramon Freitas").getResultList();
	
				for (UsuarioPessoa usuarioPessoa : list) {
					System.out.println(usuarioPessoa);
				}
	}
	
	@Test
	public void testeQuerySomaIdade(){ 
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		Long somaIdade = (Long) daoGeneric.getEntityManager().createQuery("select sum(u.unidade) from UsuarioPessoa u").getSingleResult();
	
		System.out.println("Soma das idades é " + somaIdade);
	}
	
	@Test
	public void testeNameQuery1(){ 
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.todos").getResultList();
	
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeNameQuery2(){ 
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.buscaPorNome").setParameter("nome", "Ramon Freitas").getResultList();
	
		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}
	}
	
	@Test
	public void testeGravaTelefone(){ 
		DaoGeneric daoGeneric = new DaoGeneric();
	
		UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(5L,UsuarioPessoa.class);
	
		TelefoneUser telefoneUser = new TelefoneUser();
		telefoneUser.setTipo("Casa");
		telefoneUser.setNumero("9998854877");
		telefoneUser.setUsuarioPessoa(pessoa);
		daoGeneric.salvar(telefoneUser);
	}
	
			@Test
			public void testeConsultarTelefones(){ 
				DaoGeneric daoGeneric = new DaoGeneric();
			
				UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(5L,UsuarioPessoa.class);
			
				for (TelefoneUser fone : pessoa.getTelefoneUsers()) {
					System.out.println(fone.getNumero());
					System.out.println(fone.getTipo());
					System.out.println(fone.getUsuarioPessoa().getNome());
					
				}
			}
}
