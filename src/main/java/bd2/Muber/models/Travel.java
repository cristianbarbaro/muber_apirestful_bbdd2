package bd2.Muber.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Travel {
	private Long idTravel;
	private String destiny;
	private String origin;
	private float totalCost;
	private Date date;
	private int maxPassengers;
	private boolean finalized;
	private List<Passenger> passengers;
	private Driver driver;
	
	public Travel(){
	}
	
	public Travel(Driver driver, String origin, String destiny, int maxPassengers, float totalCost) {
		super();
		this.driver = driver;
		this.origin = origin;
		this.destiny = destiny;
		this.maxPassengers = maxPassengers;
		this.totalCost = totalCost;
		this.date = new Date();
		this.finalized = false;
		this.passengers = new ArrayList<Passenger>();
	}

	public boolean addPassenger(Passenger passenger) {
		if (this.isFinalized() || this.passengers.size() >= this.maxPassengers)
			return false;
		this.passengers.add(passenger);
		return true;
	}
	
	public int getPassengerCount() {
		return this.passengers.size();
	}
	
	public void finalize() {
		this.finalized = true;
		float cost = this.totalCost / this.passengers.size();
		for (Iterator<Passenger> i = this.passengers.iterator(); i.hasNext();) {
			i.next().charge(cost);
		}
	}
	
	public Long getIdTravel() {
		return idTravel;
	}
	
	private void setIdTravel(Long idTravel) {
		this.idTravel = idTravel;
	}
	
	public String getDestiny() {
		return destiny;
	}
	
	public void setDestiny(String destiny) {
		this.destiny = destiny;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public float getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(float totalCost) {
		this.totalCost = totalCost;
	}

	public int getMaxPassengers() {
		return maxPassengers;
	}

	public void setMaxPassengers(int maxPassengers) {
		this.maxPassengers = maxPassengers;
	}

	public boolean isFinalized() {
		return finalized;
	}
	
	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}
	
	public List<Passenger> getPassengers() {
		return passengers;
	}
	
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
	
	public Driver getDriver() {
		return driver;
	}
	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
}
