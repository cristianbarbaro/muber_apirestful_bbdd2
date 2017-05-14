package bd2.Muber.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Muber {
	private Long idMuber;
	private List<Passenger> passengers;
	private List<Driver> drivers;
	private List<Travel> travels;

	public Muber() {
		super();
		this.passengers = new ArrayList<Passenger>();
		this.drivers = new ArrayList<Driver>();
		this.travels = new ArrayList<Travel>();
	}
	
	public Muber(List<Passenger> passengers, List<Driver> drivers, List<Travel> travels) {
		super();
		this.passengers = passengers;
		this.drivers = drivers;
		this.travels = travels;
	}
	
	public List<Driver> getTop10DriversWithoutOpenTravels(){
		List<Driver> top10 = new ArrayList<Driver>();
		for (Driver currentDriver : this.drivers){
			if (!currentDriver.hasOpenTravels()){
				top10.add(currentDriver);
			}
		}
		top10.sort(new Comparator<Driver>() {
			public int compare(Driver d1, Driver d2){
				return d2.getQualificationAverange().compareTo(d1.getQualificationAverange());
			}
		});
		
		return (List<Driver>) top10.subList(0, top10.size() > 10? 10 : top10.size() );
	}
	
	public void addDriver(Driver driver){
		this.drivers.add(driver);
	}

	public void addDrivers(List<Driver> drivers){
		this.drivers.addAll(drivers);
	}
	
	public void addPassenger(Passenger passenger){
		this.passengers.add(passenger);
	}

	public void addPassengers(List<Passenger> passengers){
		this.passengers.addAll(passengers);
	}
	
	public void addTravel(Travel travel){
		this.travels.add(travel);
	}

	public void addtravels(List<Travel> travels){
		this.travels.addAll(travels);
	}

	public Long getIdMuber() {
		return idMuber;
	}
	
	private void setIdMuber(Long idMuber) {
		this.idMuber = idMuber;
	}
	
	public List<Travel> getTravels() {
		return this.travels;
	}
	
	public void setTravels(List<Travel> travels) {
		this.travels = travels;
	}
	
	public List<Passenger> getPassengers() {
		return this.passengers;
	}
	
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	public List<Driver> getDrivers() {
		return this.drivers;
	}
	
	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}
	
}
