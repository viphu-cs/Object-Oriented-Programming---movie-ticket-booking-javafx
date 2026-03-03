package application;

import javafx.beans.property.SimpleStringProperty;

public class Booking {

	private String status , username, firstName, lastName, movie, date, time, seat,ID;
	private double ticketprice;
	
	public Booking(String status, String username, String firstName, String lastName, String movie, String date, String time, String seat,String ID,double ticketprice) {
		this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.movie = movie;
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.username = username;
        this.ID = ID;
        this.ticketprice = ticketprice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMovie() {
		return movie;
	}

	public void setMovie(String movie) {
		this.movie = movie;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public double getTicketprice() {
		return ticketprice;
	}

	public void setTicketprice(double ticketprice) {
		this.ticketprice = ticketprice;
	}
	
	
	
	
	
}
