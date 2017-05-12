package bd2.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.service.ServiceRegistry;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.google.gson.Gson;

import bd2.Muber.models.*;

@ControllerAdvice
@RequestMapping("/services")
@ResponseBody
@EnableWebMvc
public class MuberRestController {

	protected Muber getMuber(Session session){
		Criteria criteria = session
		    .createCriteria(Muber.class)
		    .setProjection(Projections.max("idMuber"));
		long maxIdMuber = (long) criteria.uniqueResult();
		Muber muber = (Muber) session.get(Muber.class, maxIdMuber);
		//session.close();
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
		driverMap.put("averageScore", driver.getQualificationAverange());
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
	
	protected ResponseEntity<?> response(HttpStatus code, String message){
		JSONObject json = new JSONObject();
		json.put("code", code.value());
		json.put("message", message);
		return ResponseEntity.status(code).body(json.toString());
	}
	
	protected ResponseEntity<?> response(HttpStatus code, Map message){
		JSONObject json = new JSONObject();
		json.put("code", code.value());
		json.put("message", message);
		return ResponseEntity.status(code).body(json.toString());
	}

	
	protected ResponseEntity<?> response(HttpStatus code, JSONObject message){
		JSONObject json = new JSONObject();
		json.put("code", code.value());
		json.put("message", message);
		return ResponseEntity.status(code).body(json.toString());
	}
	
	protected ResponseEntity<?> response(){
		JSONObject json = new JSONObject();
		json.put("code", HttpStatus.NO_CONTENT.value());
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(json.toString());
	}

	@RequestMapping(value = "/pasajeros", method = RequestMethod.GET, produces = "application/json")
	public String pasajeros() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Session session = this.getSession();
		Muber muber = this.getMuber(session);
		List<Passenger> passengers = muber.getPassengers();
		for ( Passenger currentPassenger : passengers ){
			aMap.put(currentPassenger.getIdUser(), this.getPassengerToMap(currentPassenger));
		}
		session.close();
		return new Gson().toJson(aMap);
	}

	@RequestMapping(value = "/conductores", method = RequestMethod.GET, produces = "application/json" )
	public String conductores() {
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Session session = this.getSession();
		Muber muber = this.getMuber(session);
		List<Driver> drivers = muber.getDrivers();
		for ( Driver currentDriver : drivers ){
			aMap.put(currentDriver.getIdUser(), this.getDriverToMap(currentDriver));
		}
		session.close();
		return new Gson().toJson(aMap);
	}
	
	@RequestMapping(value = "/viajes/abiertos", method = RequestMethod.GET, produces = "application/json" )
	public String viajesAbiertos(){
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		Session session = this.getSession();
		Muber muber = this.getMuber(session);
		 List<Travel> travels = muber.getTravels();
		 
		 for ( Travel currentTravel : travels ){
			 // Verifico que el viaje no esté finalizado antes de agregarlo a la lista.
			 // Falta poder listar todos los pasajeros en este viaje (se puede serializar una coleccion dentro de otra?)
			 if (!currentTravel.isFinalized()){
				 Map<String, Object> JSONTravel = new HashMap<String, Object>();
				 JSONTravel.put("date", currentTravel.getDate());
				 JSONTravel.put("origin", currentTravel.getOrigin());
				 JSONTravel.put("destiny", currentTravel.getDestiny());
				 JSONTravel.put("driver", currentTravel.getDriver().getUsername());
				 JSONTravel.put("maxPassenger", currentTravel.getMaxPassengers());
				 JSONTravel.put("passengerCount", currentTravel.getPassengers().size());
				 JSONTravel.put("totalCost", currentTravel.getTotalCost());
				 // Agrego el JSON a otro json:
				 aMap.put(currentTravel.getIdTravel(), JSONTravel);
			 }
		 }
		 session.close();
		 return new Gson().toJson(aMap);
	}

	
	/*
	 * {
		  "points": 2,
		  "comment": "El Peligrwwwo",
		  "travel" : { "idTravel": 1 },
		  "passenger": { "idPassenger": 3 }
		}
	*/
	@RequestMapping(value = "/viajes/calificar", method = RequestMethod.POST, produces = "application/json", headers = "Accept=application/json,application/xml", consumes = {"application/xml", "application/json"} )
	public ResponseEntity<?> calificarViaje(@RequestBody Qualification qualification){	
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		Passenger passenger = (Passenger) session.get(Passenger.class, qualification.getPassenger().getIdPassenger());
		Travel travel = (Travel) session.get(Travel.class, qualification.getTravel().getIdTravel());
		if (passenger == null){
			return this.response(HttpStatus.NOT_FOUND, "No existe el passengerId");
		}
		if (travel == null){
			return this.response(HttpStatus.NOT_FOUND, "No existe el travelId");
		}
		passenger.qualify(travel, qualification);
		t.commit();
		session.close();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	@RequestMapping(value = "/conductores/detalle/{conductorId}", method = RequestMethod.GET, produces = "application/json", headers = "Accept=application/json")
	public String infoConductor(@PathVariable("conductorId") long conductorId){
		Session session = this.getSession();
		Driver driver = (Driver) session.get(Driver.class, conductorId);
		if (driver != null){
			return new Gson().toJson(this.getDriverToMap(driver));
		}
		// Ver cómo solucionar el problema de las relaciones circulares para no usar estas funciones creadas a mano.
		return new Gson().toJson(driver);
	}

	

	/*
	 * {
		  "destiny": "La Plata",
		  "origin": "El Peligro",
		  "maxPassengers" : 6,
		  "totalCost" : 8000,
		  "driver": { "idDriver": 4 }
		}
	 */
	@RequestMapping(value = "/viajes/nuevo", method = RequestMethod.POST, produces = "application/json", headers = "Accept=application/json,application/xml", consumes = {"application/xml", "application/json"} )
	public ResponseEntity<?> crearViaje(@RequestBody Travel travel){	
		Session session = this.getSession();
		// Iniciamos la transacción
		Transaction t = session.beginTransaction();
		Driver driver = (Driver) session.get(Driver.class, travel.getDriver().getIdDriver());
		// Chequear si existe el conductor:
		if (driver == null){
			return this.response(HttpStatus.NOT_FOUND, "No existe el conductor");
		}
		// Ahora recupero Muber (la aplicación) para agregarle el viaje.
		Muber muber = (Muber) session.get(Muber.class, (long) 1);
		//driver.addTravel(travel);
		Date date = new Date();
		travel.setDate(date);
		travel.setDriver(driver);
		muber.addTravel(travel);
		t.commit();
		session.close();
		return this.response();
	}

	@RequestMapping(value = "/viajes/agregarPasajero", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> agregarPasajero(@RequestParam long viajeId, @RequestParam long pasajeroId){
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		Travel travel = (Travel) session.get(Travel.class, viajeId);//Long.parseLong(request.getParameter("viajeId")));
		Passenger passenger = (Passenger) session.get(Passenger.class, pasajeroId);//Long.parseLong(request.getParameter("pasajeroId")));
		if (travel == null) {
			/* no existe el viaje */
			return this.response(HttpStatus.NOT_FOUND, "No existe el travelId.");
		}
		if (passenger == null) {
			/* no existe el pasajero */
			return this.response(HttpStatus.NOT_FOUND, "No existe el pasajeroId.");
		}
		if (passenger.addTravel(travel)) {
			/* se agrego correctamente */
			t.commit();
			session.close();
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		/* no se pudo agregar por  */
		return this.response(HttpStatus.BAD_REQUEST, "No se puede agregar el pasajero al viaje indicado.");
	}
	
	@RequestMapping(value = "/pasajeros/cargarCredito", method = RequestMethod.PUT, produces = "application/json" )
	public ResponseEntity<?> cargarCredito(@RequestParam("passengerId") long passengerId, @RequestParam("amount") long amount){
		//System.out.println(passengerId);
		//System.out.println(amount);
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		Passenger passenger = (Passenger) session.get(Passenger.class, passengerId);
		if (passenger == null){
			return this.response(HttpStatus.NOT_FOUND, "No existe el passengerId");
		}
		passenger.setTotalCredit(passenger.getTotalCredit() + amount);
		t.commit();
		session.close();
		return ResponseEntity.status(HttpStatus.OK).body(null); //devolver passenger
	}

	@RequestMapping(value = "/viajes/finalizar", method = RequestMethod.PUT, produces = "application/json" )
	public ResponseEntity<?> finalizarViaje(@RequestParam("travelId") long travelId){
		Session session = this.getSession();
		Transaction t = session.beginTransaction();
		Travel travel = (Travel) session.get(Travel.class, travelId);
		if (travel == null){
			return this.response(HttpStatus.NOT_FOUND, "No existe el travelId");
		}
		if (travel.isFinalized()){
			return this.response(HttpStatus.ALREADY_REPORTED, "Ya se encuentra finalizado");
		}
		travel.finalize();
		t.commit();
		session.close();
		return this.response();
	}

	@RequestMapping(value = "/conductores/top10", method = RequestMethod.GET, produces = "application/json" )
	public ResponseEntity<?> conductoresTop10(){
		Session session = this.getSession();
		List<Driver> top10 = this.getMuber(session).getTop10DriversWithoutOpenTravels();
		Map<Long, Object> aMap = new HashMap<Long, Object>();
		for ( Driver currentDriver : top10 ){
			aMap.put(currentDriver.getIdUser(), this.getDriverToMap(currentDriver));
		}
		session.close();
		return this.response(HttpStatus.OK, aMap);
	}
}
