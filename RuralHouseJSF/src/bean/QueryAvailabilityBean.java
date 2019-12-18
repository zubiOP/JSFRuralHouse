package bean;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import businessLogic.ApplicationFacadeInterfaceWS;
import domain.Offer;
import domain.RuralHouse;
import domain.User;
@ManagedBean
@ViewScoped
public class QueryAvailabilityBean {
	ApplicationFacadeInterfaceWS facadeBL;
	private RuralHouse ruralHouse;
	private Vector<RuralHouse> ruralHouses;
	private Date firstDay;
	private int numberOfNights;
	private Vector<Offer> offers;
	private Offer offer;
	private boolean disableQuery;
	private boolean disableBook;
	
	public QueryAvailabilityBean() {
		facadeBL = FacadeBean.getBusinessLogic();
		this.disableQuery= false;
		this.disableBook= false;
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

	public Date getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Date firstDay) {
		this.firstDay = firstDay;
		disableQuery= false;
	}

	public int getNumberOfNights() {
		return numberOfNights;
	}

	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
	}

	public Vector<Offer> getOffers() {
		return offers;
	}

	public void setOffers(Vector<Offer> offers) {
		this.offers = offers;
		
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
		disableBook= false;
	}

	public void getOffersFacade() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(firstDay);
		calendar.add(Calendar.DAY_OF_YEAR, numberOfNights);
		Date lastDay = calendar.getTime();
		offers = facadeBL.getOffers(ruralHouse, firstDay, lastDay);
		if (offers.size() == 0)
			facesContext.addMessage(null, new FacesMessage("There are no offers at these dates"));
		else
			facesContext.addMessage(null, new FacesMessage("Select an offer if you want to book"));
	}

	public void bookOffer() {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		User u = (User) session.getAttribute("user");
		if (facadeBL.bookOffer(u, offer))
			facesContext.addMessage(null, new FacesMessage("Ondo erreserbatu da oferta"));
		else
			facesContext.addMessage(null, new FacesMessage("Oferta jadanik erreserbaturik zegoen."));
	}

	public boolean isDisableQuery() {
		return disableQuery;
	}

	public void setDisableQuery(boolean disableQuery) {
		this.disableQuery = disableQuery;
	}

	public boolean isDisableBook() {
		return disableBook;
	}

	public void setDisableBook(boolean disableBook) {
		this.disableBook = disableBook;
	}

}
