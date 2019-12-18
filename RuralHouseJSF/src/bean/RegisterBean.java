package bean;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.ApplicationFacadeInterfaceWS;

public class RegisterBean {
	
	ApplicationFacadeInterfaceWS facadeBL;
	private String izena;
	private String pasahitza;
	private String pasahitza2;
	private String eposta;
	private Integer tel;

	public RegisterBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}
	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getPasahitza() {
		return pasahitza;
	}

	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	public String getPasahitza2() {
		return pasahitza2;
	}
	public void setPasahitza2(String pasahitza2) {
		this.pasahitza2 = pasahitza2;
	}
	public String getEposta() {
		return eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}

	public Integer getTel() {
		return tel;
	}

	public void setTel(Integer tel) {
		this.tel = tel;
	}
	
	public void egiaztatu() throws IOException {
		if(pasahitza.equals(pasahitza2)) {
			erregistratu();
		}else {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.addMessage(null, new FacesMessage("Pasahitzak ez dira berdinak"));
		}
			
	}
	public void erregistratu() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if( facadeBL.register(eposta, pasahitza, izena,tel)) {
			facesContext.addMessage(null, new FacesMessage("Erabiltzailea zuzen sortu da"));
			facesContext.getExternalContext().redirect("/RuralHouseJSF/faces/Hasiera.xhtml");
		} else {
			facesContext.addMessage(null, new FacesMessage("Badago eposta horrekin erabiltzaile bat edo errorea zerbitzarian."));
			
		}

	}
}
