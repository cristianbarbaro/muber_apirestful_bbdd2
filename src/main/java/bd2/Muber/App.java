package bd2.Muber;

import java.util.GregorianCalendar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import bd2.Muber.models.*;

public class App {

	public static void main(String[] args) {
		Configuration conf = new Configuration().configure("hibernate/hibernate.cfg.xml");
		ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
		SessionFactory sf = conf.buildSessionFactory(sr);
		Session session = sf.openSession();
		session.beginTransaction();
		System.out.println("Transacción creada.");
		System.out.println("Creando y guardando Muber...");
		Muber muber = new Muber();
		
		/******* PUNTO 4 A ******/
		System.out.println("Punto 4.A.");
		Driver roberto = new Driver("roberto", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		System.out.println("creado usuario:"+roberto.getUsername());
		Travel viaje = roberto.createTravel("La Plata", "Tres Arroyos", 4, 900);
		System.out.println("creado viaje:"+viaje.getOrigin()+viaje.getDestiny());
		muber.addDriver(roberto);
		muber.addTravel(viaje);

		/******* PUNTO 4 B ******/
		System.out.println("Punto 4.B.");
		Passenger german = new Passenger("german", "ger", 1500);
		Passenger alicia = new Passenger("alicia", "ali", 1500);
		Passenger margarita = new Passenger("margarita", "mar", 1500);
		german.addTravel(viaje);
		alicia.addTravel(viaje);
		margarita.addTravel(viaje);
		muber.addPassenger(german);
		muber.addPassenger(alicia);
		muber.addPassenger(margarita);

		/******* PUNTO 4 C ******/
		System.out.println("Punto 4.C.");
		Qualification q1 = german.qualify(viaje, 5, "alto viaje, me re cabio");
		Qualification q2 = alicia.qualify(viaje, 4, "todo bien pero el chofer durmio todo el viaje");
		Qualification q3 = margarita.qualify(viaje, 4, "los mates eran dulces");

		/******* PUNTO 4 D ******/
		System.out.println("Punto 4.D.");
		viaje.finalize();

		System.out.println("Guardando...");
		session.save(roberto);
		session.save(viaje);
		session.save(german);
		session.save(alicia);
		session.save(margarita);
		session.save(q1);
		session.save(q2);
		session.save(q3);
		session.save(muber);
		
		System.out.println("Cerrando transacción...");
		session.getTransaction().commit();
		session.flush();
		session.close();
		sf.close();
		System.out.println("Terminado.");
	}

}
