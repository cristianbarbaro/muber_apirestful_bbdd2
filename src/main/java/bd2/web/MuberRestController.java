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
import org.springframework.web.bind.annotation.PathVariable;
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
	
	protected Map<String, Object> getDriverToMap(Driver driver){
		Map<String, Object> driverMap = new HashMap<String, Object>();
		driverMap.put("username", driver.getUsername());
		driverMap.put("addmissionDate", driver.getAdmissionDate());
		driverMap.put("averageScore", driver.getAverangeScore());
		driverMap.put("licenceExpiration", driver.getLicenceExpiration());
		return driverMap;
	}
	
	protected Map<String, Object> getPassengerToMap(Passenger passenger){
		Map<String, Object> passengerMap = new HashMap<String, Object>();
		passengerMap.put("username", passenger.getUsername());
		passengerMap.put("admissionDate", passenger.getAdmissionDate());
		passengerMap.put("totalCredits", passenger.getTotalCredit());
		return passengerMap;
	}

	@RequestMapping(value = "/conductores", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String conductores() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		//aMap.put("result", "OK");
		Muber muber = this.getMuber();
		List<Driver> drivers = muber.getDrivers();
		
		for (int i = 0; i < drivers.size(); i++){
			Driver currentDriver = drivers.get(i);
			aMap.put(currentDriver.getIdUser(), this.getDriverToMap(currentDriver));
		}
		
		return new Gson().toJson(aMap);
	}

	@RequestMapping(value = "/pasajeros", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String pasajeros() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Muber muber = this.getMuber();
		
		List<Passenger> passengers = muber.getPassengers();
		
		for (int i = 0; i < passengers.size(); i++){
			Passenger currentPassenger = passengers.get(i);
			aMap.put(passengers.get(i).getIdUser(), this.getPassengerToMap(currentPassenger));
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
	
	@RequestMapping(value = "/conductores/detalles/{conductorId}", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String infoConductor(@PathVariable("conductorId") long conductorId){
		Session session = this.getSession();
		Driver driver = (Driver) session.get(Driver.class, conductorId);
		if (driver != null){
			return new Gson().toJson(this.getDriverToMap(driver));
		}
		// Ver cómo solucionar el problema de las relaciones circulares para no usar estas funciones creadas a mano.
		return new Gson().toJson(driver);
	}
	
}
