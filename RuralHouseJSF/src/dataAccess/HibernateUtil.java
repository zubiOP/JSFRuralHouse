package dataAccess;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// SessionFactory instantzia sortzen du, hibernate.cfg.xml dauden datuen arabera
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Fallo creando el SessionFactory." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}