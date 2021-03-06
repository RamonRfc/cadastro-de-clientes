package managedBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoUsuario;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name="usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();
	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barChartModel = new BarChartModel();
	private EmailUser emailuser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	
	
	@PostConstruct
public void init() {
		
		list = daoGeneric.listar(UsuarioPessoa.class); //carregando todos os usuários
		
		montarGrafico();
	}


	private void montarGrafico() {
		barChartModel = new BarChartModel();
		
		ChartSeries userSalario = new ChartSeries(); /**Grupo de funcionarios*/
		//userSalario.setLabel("Users");
		
		for (UsuarioPessoa usuarioPessoa : list) { /**Adiciona salario para o grupo*/
			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario()); //adiciona salarios
		}
		barChartModel.addSeries(userSalario); //adiciona o grupo no barmodel
		barChartModel.setTitle("Gráfico de salários"); //titulo do gráfico
	}
//	public void init() {
//		list = daoGeneric.listar(UsuarioPessoa.class);
//		
//		ChartSeries userSalario = new ChartSeries();/*Grupo de func*/
//		
//		for (UsuarioPessoa usuarioPessoa : list) {
//			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario()); /*add salario*/
//		}
//		barChartModel.addSeries(userSalario); // adiciona o grupo
//		barChartModel.setTitle("Gráfico de salários");
//	}
	
	public void pesquisaCep(AjaxBehaviorEvent event){
		try {
//			System.out.println("Cep digitado: " + usuarioPessoa.getCep());
			URL url =  new URL("https://viacep.com.br/ws/"+ usuarioPessoa.getCep()+"/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			
			while((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			
			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplementoString(userCepPessoa.getComplementoString());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public BarChartModel getBarChartModel() {
		return barChartModel;
	}

	public void setBarChartModel(BarChartModel barChartModel) {
		this.barChartModel = barChartModel;
	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa;
	}
	
	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}
	public String salvar() {
		daoGeneric.salvar(usuarioPessoa);
		list.add(usuarioPessoa);
		
		usuarioPessoa = new UsuarioPessoa();
		
		init();
		
		FacesContext.getCurrentInstance().addMessage
		(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:","Salvo com sucesso!"));
		return "usuario-salvo";
	}
//	public String salvar() {
//		daoGeneric.salvar(usuarioPessoa);
//		list.add(usuarioPessoa);
//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: " ,"Salvo com sucesso!"));
//		return "";
//	}
	
	public String novo(){
		usuarioPessoa = new UsuarioPessoa();
		return "";
	}
	
	public List<UsuarioPessoa> getList(){
		
		return list;
	}
	
public String remover() {
		
		try {
			
		daoGeneric.removerUsario(usuarioPessoa);
		list.remove(usuarioPessoa);
		usuarioPessoa = new UsuarioPessoa();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:","Removido com sucesso!"));
		init();
		
		}catch (Exception e) {
			if(e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação:","Existem telefones para o usuário!"));
			}else {
				e.printStackTrace();
			}
		}
		return "";
	}

	public EmailUser getEmailuser() {
		return emailuser;
	}

	public void setEmailuser(EmailUser emailuser) {
		this.emailuser = emailuser;
	}
	public void addEmail () {
		emailuser.setUsuarioPessoa(usuarioPessoa);
		emailuser = daoEmail.updateMerge(emailuser);
		usuarioPessoa.getEmails().add(emailuser);
		emailuser = new EmailUser();
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado..","Salvo com sucesso!"));
	}
	
	public void removerEmail() throws Exception {
		String codigoemail = FacesContext.getCurrentInstance().getExternalContext().
				getRequestParameterMap().get("codigoemail");
		
		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail));
		daoEmail.deletePorId(remover);
		usuarioPessoa.getEmails().remove(remover);
		
	}
//	public void addEmail () {
//		emailuser.setUsuarioPessoa(usuarioPessoa);
//		emailuser = daoEmail.updateMerge(emailuser);
//		usuarioPessoa.getEmails().add(emailuser);
//		emailuser = new EmailUser();
//		FacesContext.getCurrentInstance().addMessage(null, 
//				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado..","Salvo com sucesso!"));
//		
//	}
	
	
}
