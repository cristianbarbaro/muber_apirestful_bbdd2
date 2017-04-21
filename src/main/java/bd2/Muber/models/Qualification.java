package bd2.Muber.models;

public class Qualification {
	private Long idQualification;
	private int points;
	private String comment;
	private Passenger passenger;
	private Travel travel;
	
	public Qualification(){
	}
	
	public Qualification(int points, String comment, Passenger passenger, Travel travel) {
		super();
		this.points = points;
		this.comment = comment;
		this.passenger = passenger;
		this.travel = travel;
		this.travel.getDriver().addQualification(this);
	}

	public Long getIdQualification() {
		return idQualification;
	}
	
	private void setIdQualification(Long idQualification) {
		this.idQualification = idQualification;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Passenger getPassenger() {
		return passenger;
	}
	
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	
	public Travel getTravel() {
		return travel;
	}
	
	public void setTravel(Travel travel) {
		this.travel = travel;
	}
}
