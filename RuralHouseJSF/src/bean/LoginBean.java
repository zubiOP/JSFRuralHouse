package bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.ApplicationFacadeInterfaceWS;
import domain.User;

public class LoginBean {

	ApplicationFacadeInterfaceWS facadeBL;
	private String pasahitza;
	private String eposta;

	public LoginBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public String getEposta() {
		return eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}

	public String login() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		User user = facadeBL.login(eposta, pasahitza);
		if (user != null) {
			facesContext.getExternalContext().getSessionMap().put("user", user);
			return "ok";
		} else {
			facesContext.addMessage(null, new FacesMessage("Eposta edo pasahitz okerra."));
            eposta = null;
            pasahitza = null;
			return "error";
		}

	}
	 public String logout() {
	        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	        return "atera";
	   }
}
