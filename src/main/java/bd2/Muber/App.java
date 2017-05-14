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
		
		/**** Creando conductores ***/
		System.out.println("Punto 4.A.");
		// Estos tienen viajes cerrados
		Driver roberto = new Driver("roberto", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		Driver tito = new Driver("tito", "tito", new GregorianCalendar(2021, 2, 21).getTime());
		Driver agueda = new Driver("agueda", "facil", new GregorianCalendar(2020, 2, 20).getTime());
		Driver lihuen = new Driver("lihuen", "redificil", new GregorianCalendar(2018, 4, 22).getTime());
		Driver daniel = new Driver("daniel", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		Driver cristian = new Driver("cristian", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		Driver barbaro = new Driver("barbaro", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		Driver jonsnow = new Driver("jonsnow", "no sabes nada, amigo", new GregorianCalendar(2020, 2, 20).getTime());
		Driver thor = new Driver("thor", "dame el martillo", new GregorianCalendar(2020, 2, 20).getTime());
		Driver loki = new Driver("loki", "mucho", new GregorianCalendar(2020, 2, 20).getTime());
		// Este tendrá viajes abierto.
		Driver abierto = new Driver("abierto", "redificil", new GregorianCalendar(2020, 2, 20).getTime());
		
		/**** Creación de viajes abiertos ****/
		Travel viaje1 = roberto.createTravel("La Plata", "Tres Arroyos", 4, 1900);
		Travel viaje2 = tito.createTravel("El Peligro", "El Pato", 4, 900);
		Travel viaje3 = agueda.createTravel("El Peligro", "El Pato", 4, 800);
		Travel viaje4 = lihuen.createTravel("La Plata", "Tres Arroyos", 6, 2000);
		Travel viaje5 = daniel.createTravel("La Plata", "Tres Arroyos", 3, 3000);
		Travel viaje6 = cristian.createTravel("La Plata", "Bariloche", 5, 9000);
		Travel viaje7 = agueda.createTravel("Buenos Aires", "Bariloche", 5, 9000);
		Travel viaje8 = jonsnow.createTravel("The Wall", "King's Landing", 2, 100000);
		Travel viaje9 = thor.createTravel("Buenos Aires", "Montevideo", 3, 100000);
		Travel viaje10 = loki.createTravel("New York", "Ragnarok", 3, 100000);
		
		/*** Viaje cerrado ***/
		Travel viaje11 = abierto.createTravel("Ensenada", "La Plata", 3, 300);
		
		/**** Algunos pasajeros ***/
		Passenger german = new Passenger("german", "ger", 20000);
		Passenger alicia = new Passenger("alicia", "ali", 15000);
		Passenger margarita = new Passenger("margarita", "mar", 15000);
		german.addTravel(viaje11);
		alicia.addTravel(viaje11);
		margarita.addTravel(viaje11);
		
		/**** Viajes calificados ****/
		german.addTravel(viaje1);
		alicia.addTravel(viaje1);
		margarita.addTravel(viaje1);
		german.addTravel(viaje2);
		alicia.addTravel(viaje3);
		margarita.addTravel(viaje4);
		german.addTravel(viaje5);
		alicia.addTravel(viaje6);
		margarita.addTravel(viaje7);
		
		/*** Calificamos para Rusia ***/
		Qualification q1 = german.qualify(viaje1, 5, "alto viaje, me re cabio");
		Qualification q2 = alicia.qualify(viaje1, 4, "todo bien pero el chofer durmio todo el viaje");
		Qualification q3 = margarita.qualify(viaje1, 4, "los mates eran dulces");
		viaje1.finalize();
		Qualification q4 = german.qualify(viaje2, 1, "viajamos solo, creo que le cabe la soledad");
		viaje2.finalize();
		Qualification q5 = alicia.qualify(viaje3, 2, "todo bien pero el chofer durmio todo el viaje");
		viaje3.finalize();
		Qualification q6 = margarita.qualify(viaje4, 4, "los mates eran dulces");
		viaje4.finalize();
		Qualification q7 = german.qualify(viaje5, 5, "alto viaje, me re cabio");
		viaje5.finalize();
		Qualification q8 = alicia.qualify(viaje6, 3, "todo bien pero el chofer durmio todo el viaje");
		viaje6.finalize();
		Qualification q9 = margarita.qualify(viaje7, 4, "los mates eran dulces");
		viaje7.finalize();
		viaje8.finalize();
		viaje9.finalize();
		viaje10.finalize();
		
		/*** Hay que asociar todo a muber ***/
		
		muber.addDriver(roberto);
		muber.addDriver(tito);
		muber.addDriver(agueda);
		muber.addDriver(lihuen);
		muber.addDriver(daniel);
		muber.addDriver(cristian);
		muber.addDriver(barbaro);
		muber.addDriver(jonsnow);
		muber.addDriver(thor);
		muber.addDriver(loki);
		muber.addDriver(abierto);
		
		muber.addTravel(viaje1);
		muber.addTravel(viaje2);
		muber.addTravel(viaje3);
		muber.addTravel(viaje4);
		muber.addTravel(viaje5);
		muber.addTravel(viaje6);
		muber.addTravel(viaje7);
		muber.addTravel(viaje8);
		muber.addTravel(viaje9);
		muber.addTravel(viaje10);
		muber.addTravel(viaje11);

		muber.addPassenger(german);
		muber.addPassenger(alicia);
		muber.addPassenger(margarita);

		System.out.println("Guardando...");
		session.save(muber);
		
		System.out.println("Cerrando transacción...");
		session.getTransaction().commit();
		session.flush();
		session.close();
		sf.close();
		System.out.println("Terminado.");
	}

}
