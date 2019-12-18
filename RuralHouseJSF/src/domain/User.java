package domain;

import java.io.*;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {

	@XmlID
	private String eposta;
	private String izena;
	private String pasahitza;
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	private Integer tel;
	public Set<Offer> offers;
	
	public User(String eposta,String pasahitza,String izena,Integer tel) {
		this.izena=izena;
		this.pasahitza=pasahitza;
		this.eposta=eposta;
		this.tel=tel;
	}
	public User() {
		
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

	public Set<Offer> getOffers() {
		return offers;
	}

	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}
	public void addOffer(Offer offer) {
		offers.add(offer);
	}
}