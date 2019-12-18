package bean;

import java.util.Date;
import java.util.Vector;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import businessLogic.ApplicationFacadeInterfaceWS;
import domain.Offer;
import domain.RuralHouse;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;

public class SetAvailabilityBean {
	ApplicationFacadeInterfaceWS facadeBL;
	private RuralHouse ruralHouse;
	private Vector<RuralHouse> ruralHouses;
	private float price;
	private Date firstDay;
	private Date lastDay;

	public SetAvailabilityBean() {
		facadeBL = FacadeBean.getBusinessLogic();
	}

	public RuralHouse getRuralHouse() {
		return ruralHouse;
	}

	public void setRuralHouse(RuralHouse ruralHouse) {
		this.ruralHouse = ruralHouse;
	}

	public Vector<RuralHouse> getRuralHouses() {
		ruralHouses = facadeBL.getAllRuralHouses();
		return ruralHouses;
	}

	public void setRuralHouses(Vector<RuralHouse> ruralHouses) {
		this.ruralHouses = ruralHouses;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
	}

	public Date getLastDay() {
		return lastDay;
	}

	public void setLastDay(Date lastDay) {
		this.lastDay = lastDay;
	}

	public void createOfferAriketa() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(facadeBL.setOfferAriketa()) {
			facesContext.addMessage(null, new FacesMessage("Ondo sortu dira ofertak"));
		}else
			facesContext.addMessage(null, new FacesMessage("Overlappina gertatu da ruralHousetako batean."));
	}
	public void createOfferFacade() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		try {
			Offer o = facadeBL.createOffer(ruralHouse, firstDay, lastDay, price);
			if (o == null)
				facesContext.addMessage(null, new FacesMessage("There exists an overlapping offer"));
			else
				facesContext.addMessage(null, new FacesMessage("Offer created"));
		} catch (NumberFormatException e) {
			facesContext.addMessage(null, new FacesMessage(price + " is not a valid price"));
		} catch (OverlappingOfferExists e) {
			facesContext.addMessage(null, new FacesMessage("There exists an overlapping offer"));
		} catch (BadDates e) {
			facesContext.addMessage(null, new FacesMessage("Last day is before first day in the offer"));
		}
	}
}
