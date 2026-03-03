package application;

public class Movie {
	private String title, description, startDate, endDate, posterPath;
    private String[] times ;
    private double ticketprice;
    
    public Movie(String title, String description, String startDate, String endDate, String posterPath, String[] times,double ticketprice) {
    	this.title = title;
    	this.description = description;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.posterPath = posterPath;
    	this.times = times;
    	this.ticketprice = ticketprice;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String[] getTimes() {
		return times;
	}

	public void setTimes(String[] times) {
		this.times = times;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}

	public double getTicketprice() {
		return ticketprice;
	}

	public void setTicketprice(double ticketprice) {
		this.ticketprice = ticketprice;
	}
	
	

}
