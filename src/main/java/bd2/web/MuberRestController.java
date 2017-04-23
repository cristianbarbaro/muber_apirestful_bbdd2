package bd2.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.gson.Gson;

import bd2.Muber.models.*;

@ControllerAdvice
@RequestMapping("/services")
@ResponseBody
@EnableWebMvc
public class MuberRestController {

	protected Muber getMuber(){
		Session session = this.getSession();
		//Esto debe mejorarse... El tipo del id no puede ser Integer.
		Long id = (long) 1;
		Muber muber = (Muber) session.get(Muber.class, id);
		return muber;
	}
	
	protected Session getSession() {
		Configuration cfg = new Configuration();
		cfg.configure("hibernate/hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		//ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
		//SessionFactory factory = cfg.buildSessionFactory(sr);
		Session session = factory.openSession();
		return session;
	}

	@RequestMapping(value = "/conductores", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String conductores() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		//aMap.put("result", "OK");
		Muber muber = this.getMuber();
		List<Driver> drivers = muber.getDrivers();
		
		for (int i = 0; i < drivers.size(); i++){
			Map<String, Object> JSONDriver = new HashMap<String, Object>();
			Driver currentDriver = drivers.get(i);
			JSONDriver.put("username", currentDriver.getUsername());
			JSONDriver.put("addmissionDate", currentDriver.getAdmissionDate());
			JSONDriver.put("averageScore", currentDriver.getAverangeScore());
			JSONDriver.put("licenceExpiration", currentDriver.getLicenceExpiration());
			aMap.put(currentDriver.getIdUser(), JSONDriver);
		}
		
		return new Gson().toJson(aMap);
	}

	@RequestMapping(value = "/pasajeros", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String pasajeros() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Muber muber = this.getMuber();
		
		List<Passenger> passengers = muber.getPassengers();
		
		for (int i = 0; i < passengers.size(); i++){
			//Decido qué datos quiero mostrar (enviar la colleccion completa al HashMap generaba una excepción.
			Map<String, Object> JSONPassenger = new HashMap<String, Object>();
			Passenger currentPassenger = passengers.get(i);
			JSONPassenger.put("username", currentPassenger.getUsername());
			JSONPassenger.put("admissionDate", currentPassenger.getAdmissionDate());
			JSONPassenger.put("totalCredits", currentPassenger.getTotalCredit());
			aMap.put(passengers.get(i).getIdUser(), JSONPassenger);
			
		}
		
		return new Gson().toJson(aMap);
	}
	
	@RequestMapping(value = "/viajes/abiertos", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String viajesAbiertos(){
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Muber muber = this.getMuber();
		 List<Travel> travels = muber.getTravels();
		 
		 for (int i = 0; i < travels.size(); i++){
			 Travel currentTravel = travels.get(i);
			 // Verifico que el viaje no esté finalizado antes de agregarlo a la lista.
			 // Falta poder listar todos los pasajeros en este viaje (se puede serializar una coleccion dentro de otra?)
			 if (!currentTravel.isFinalized()){
				 Map<String, Object> JSONTravel = new HashMap<String, Object>();
				 JSONTravel.put("date", currentTravel.getDate());
				 JSONTravel.put("origin", currentTravel.getOrigin());
				 JSONTravel.put("destiny", currentTravel.getDestiny());
				 JSONTravel.put("driver", currentTravel.getDriver().getUsername());
				 JSONTravel.put("maxPassenger", currentTravel.getMaxPassengers());
				 JSONTravel.put("totalCost", currentTravel.getTotalCost());
				 // Agrego el JSON a otro json:
				 aMap.put(currentTravel.getIdTravel(), JSONTravel);
			 }
		 }
		 return new Gson().toJson(aMap);
	}
	
}
