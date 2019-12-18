package dataAccess;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import javax.jws.WebMethod;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

//import domain.Booking;
import domain.Offer;
import domain.RuralHouse;
import exceptions.OverlappingOfferExists;

public class DataAccess {

	public static String fileName = "ruralHouses.odb";
	protected static EntityManagerFactory emf;
	protected static EntityManager db;

	public DataAccess() {

//		c=ConfigXML.getInstance();

//		System.out.println("Creating objectdb instance => isDatabaseLocal: "+c.isDatabaseLocal()+" getDatabBaseOpenMode: "+c.getDataBaseOpenMode());

//		if (c.isDatabaseLocal()) {

		// emf = Persistence.createEntityManagerFactory(c.getDbFilename());
		emf = Persistence.createEntityManagerFactory(fileName);
		db = emf.createEntityManager();
//		} else {		
//				  Map<String, String> properties = new HashMap<String, String>();
//				  properties.put("javax.persistence.jdbc.user", c.getUser());
//				  properties.put("javax.persistence.jdbc.password", c.getPassword());
//				  emf = Persistence.createEntityManagerFactory(
//				      "objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+c.getDbFilename(), properties);
//				  db = emf.createEntityManager();				
//		}
	}

	public void initializeDB() {

		db.getTransaction().begin();
		try {

			TypedQuery<RuralHouse> query = db.createQuery("SELECT c FROM RuralHouse c", RuralHouse.class);
			List<RuralHouse> results = query.getResultList();

			Iterator<RuralHouse> itr = results.iterator();

			while (itr.hasNext()) {
				RuralHouse rh = itr.next();
				System.out.println("Deleting " + rh.toString());
				db.remove(rh);
			}
			db.getTransaction().commit();

			db.getTransaction().begin();
			RuralHouse rh1 = new RuralHouse("Ezkioko etxea", "Ezkio");
			RuralHouse rh2 = new RuralHouse("Etxetxikia", "Iruna");
			RuralHouse rh3 = new RuralHouse("Udaletxea", "Bilbo");
			RuralHouse rh4 = new RuralHouse("Gaztetxea", "Renteria");

			db.persist(rh1);
			db.persist(rh2);
			db.persist(rh3);
			db.persist(rh4);

			db.getTransaction().commit();
			System.out.println("Db initialized");

		} catch (Exception e) {
			System.out.println("ERROR!!!!!!!!!!!!!!");
			e.printStackTrace();
		}
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
		System.out.println(">> DataAccess: createOffer=> ruralHouse= " + ruralHouse + " firstDay= " + firstDay
				+ " lastDay=" + lastDay + " price=" + price);

		try {
			RuralHouse rh = db.find(RuralHouse.class, ruralHouse.getHouseNumber());

			db.getTransaction().begin();
			Offer o = rh.createOffer(firstDay, lastDay, price);
			db.persist(o);
//		System.out.println(rh.offers);
			db.getTransaction().commit();
			return o;

		} catch (Exception e) {
			System.out.println("Offer not created: " + e.toString());
			return null;
		}
	}

	public Vector<RuralHouse> getAllRuralHouses() {
		System.out.println(">> DataAccess: getAllRuralHouses");
		Vector<RuralHouse> res = new Vector<RuralHouse>();

		TypedQuery<RuralHouse> query = db.createQuery("SELECT c FROM RuralHouse c", RuralHouse.class);
		List<RuralHouse> results = query.getResultList();

		Iterator<RuralHouse> itr = results.iterator();

		while (itr.hasNext()) {
			res.add(itr.next());
		}

		return res;

	}

	public Vector<Offer> getOffers(RuralHouse rh, Date firstDay, Date lastDay) {
		System.out.println(">> DataAccess: getOffers");
		Vector<Offer> res = new Vector<Offer>();
		RuralHouse rhn = db.find(RuralHouse.class, rh.getHouseNumber());
		res = rhn.getOffers(firstDay, lastDay);
		return res;
	}

	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws OverlappingOfferExists {
		try {
			RuralHouse rhn = db.find(RuralHouse.class, rh.getHouseNumber());
			if (rhn.overlapsWith(firstDay, lastDay) != null)
				return true;
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			return true;
		}
		return false;
	}

	public void close() {
		db.close();
		System.out.println("DataBase closed");
	}
}
