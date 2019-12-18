package businessLogic;

import java.util.Date;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.HibernateDataAccess;
//import domain.Booking;
import domain.Offer;
import domain.RuralHouse;
import domain.User;
import exceptions.BadDates;
import exceptions.OverlappingOfferExists;

//Service Implementation
@WebService(endpointInterface = "businessLogic.ApplicationFacadeInterfaceWS")
public class FacadeImplementationWS implements ApplicationFacadeInterfaceWS {

	public FacadeImplementationWS() {
		System.out.println(">> new FacadeImplementationWS");
		HibernateDataAccess dbManager = new HibernateDataAccess();
		dbManager.initializeDB();
	}

	/**
	 * This method creates an offer with a house number, first day, last day and
	 * price
	 * 
	 * @param House number, start day, last day and price
	 * @return the created offer, or null, or an exception
	 */
	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price)
			throws OverlappingOfferExists, BadDates {
		System.out.println(">> FacadeImplementationWS: createOffer=> ruralHouse= " + ruralHouse + " firstDay= "
				+ firstDay + " lastDay=" + lastDay + " price=" + price);

		HibernateDataAccess dbManager = new HibernateDataAccess();
		Offer o = null;
		if (firstDay.compareTo(lastDay) >= 0) {
			throw new BadDates();
		}

		boolean b = dbManager.existsOverlappingOffer(ruralHouse, firstDay, lastDay); // The ruralHouse object in the
																						// client may not be updated
		if (!b)
			o = dbManager.createOffer(ruralHouse, firstDay, lastDay, price);
		System.out.println("<< FacadeImplementationWS: createOffer=> O= " + o);
		return o;
	}

	public Vector<RuralHouse> getAllRuralHouses() {
		System.out.println(">> FacadeImplementationWS: getAllRuralHouses");

		HibernateDataAccess dbManager = new HibernateDataAccess();

		Vector<RuralHouse> ruralHouses = dbManager.getAllRuralHouses();
		
		System.out.println("<< FacadeImplementationWS:: getAllRuralHouses");

		return ruralHouses;

	}

	/**
	 * This method obtains the offers of a ruralHouse in the provided dates
	 * 
	 * @param ruralHouse, the ruralHouse to inspect
	 * @param firstDay,   first day in a period range
	 * @param lastDay,    last day in a period range
	 * @return the first offer that overlaps with those dates, or null if there is
	 *         no overlapping offer
	 */

	@WebMethod
	public Vector<Offer> getOffers(RuralHouse rh, Date firstDay, Date lastDay) {

		HibernateDataAccess dbManager = new HibernateDataAccess();
		Vector<Offer> offers = new Vector<Offer>();
		offers = dbManager.getOffers(rh, firstDay, lastDay);

		return offers;
	}

	public void initializeBD() {
		HibernateDataAccess dbManager = new HibernateDataAccess();
		dbManager.initializeDB();
	}
	public boolean register(String eposta, String pasahitza, String izena, Integer tel) {
		HibernateDataAccess dbManager = new HibernateDataAccess();
		return dbManager.register(eposta,pasahitza,izena,tel);
	}
	public User login(String eposta,String pasahitza) {
		HibernateDataAccess dbManager = new HibernateDataAccess();
		return dbManager.login(eposta,pasahitza);
	}
	public boolean bookOffer(User u, Offer offer) {
		HibernateDataAccess dbManager = new HibernateDataAccess();
		return dbManager.bookOffer(u, offer);
	}
	public boolean setOfferAriketa() {
		HibernateDataAccess dbManager = new HibernateDataAccess();
		return dbManager.setOfferAriketa();
	}
}
