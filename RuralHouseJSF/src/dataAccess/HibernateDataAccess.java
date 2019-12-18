package dataAccess;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;

import domain.Offer;
import domain.RuralHouse;
import domain.User;
import exceptions.OverlappingOfferExists;

public class HibernateDataAccess {
	public void initializeDB() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<Offer> resultO = session.createQuery("from Offer").list();
		for (Offer o : resultO)
			session.delete(o);
		List<RuralHouse> resultRH = session.createQuery("from RuralHouse").list();
		for (RuralHouse rh : resultRH)
			session.delete(rh);

		session.getTransaction().commit();
		System.out.println("DBa ezabatuta");
		session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		RuralHouse rh1 = new RuralHouse("Ezkioko etxea", "Ezkio");
		session.save(rh1);
		RuralHouse rh2 = new RuralHouse("Eskiatzeko etxea", "Jaca");
		session.save(rh2);
		RuralHouse rh3 = new RuralHouse("Udaletxea", "Bilbo");
		session.save(rh3);
		RuralHouse rh4 = new RuralHouse("Gaztetxea", "Renteria");
		session.save(rh4);

		session.getTransaction().commit();
		System.out.println("DBa hasieratuta");
	}

	public Offer createOffer(RuralHouse ruralHouse, Date firstDay, Date lastDay, float price) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Offer o = new Offer(firstDay, lastDay, price, ruralHouse);
		session.save(o);
		session.getTransaction().commit();
		return o;
	}

	public Vector<RuralHouse> getAllRuralHouses() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<RuralHouse> resultRH = session.createQuery("from RuralHouse").list();
		Vector<RuralHouse> v = new Vector(resultRH);
		session.getTransaction().commit();
		return v;
	}

	public Vector<Offer> getOffers(RuralHouse rh, Date firstDay, Date lastDay) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SimpleDateFormat formateador = new SimpleDateFormat("yy-MM-dd");
		List<Offer> resultOf = session.createQuery("from Offer where firstDay>='" + formateador.format(firstDay)
				+ "' and lastDay<='" + formateador.format(lastDay) + "'").list();
		Vector<Offer> v = new Vector(resultOf);
		session.getTransaction().commit();
		return v;
	}

	public boolean existsOverlappingOffer(RuralHouse rh, Date firstDay, Date lastDay) throws OverlappingOfferExists {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<RuralHouse> resultRH = session
				.createQuery("from RuralHouse where houseNumber='" + rh.getHouseNumber() + "'").list();
		session.getTransaction().commit();
		try {
			RuralHouse rhn = resultRH.get(0);
			if (rhn.overlapsWith(firstDay, lastDay) != null)
				return true;
		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
			return true;
		}
		return false;
	}

	public boolean register(String eposta, String pasahitza, String izena, Integer tel) {

		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			User u = new User(eposta, pasahitza, izena, tel);
			session.save(u);
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			System.out.print(e);
			return false;
		}
	}

	public User login(String eposta, String pasahitza) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<User> result = session
				.createQuery("from User where eposta='" + eposta + "' and pasahitza='" + pasahitza + "'").list();
		session.getTransaction().commit();
		if (result.isEmpty())
			return null;
		return result.get(0);
	}

	public boolean bookOffer(User u, Offer offer) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();
		List<Offer> resultOffer = session.createQuery("from Offer where offerNumber='" + offer.getOfferNumber() + "'")
				.list();
		Offer o = resultOffer.get(0);
		if (o.getUser() != null)
			return false;
		o.setUser(u);
		u.addOffer(o);
		session.save(o);
		session.getTransaction().commit();
		return true;
	}

	public boolean setOfferAriketa() {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			Calendar myCalendar = new GregorianCalendar(2020, 1, 1);
			Date firstdate = myCalendar.getTime();
			Calendar myCalendar2 = new GregorianCalendar(2020, 12, 31);
			Date seconddate = myCalendar2.getTime();
			Vector<RuralHouse> rhs = getAllRuralHouses();
			for (int i = 0; i < rhs.size(); i++) {
				createOffer(rhs.get(i), firstdate, seconddate, 500);
				
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
