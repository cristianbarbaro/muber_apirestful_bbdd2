package bd2.Muber.models;

import java.util.Date;

public abstract class User {
	private Long idUser;
	private String username;
	private String password;
	private Date admissionDate;
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.setAdmissionDate(new Date()); 
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public Long getIdUser() {
		return idUser;
	}
	
	private void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}
}
