package application;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ManageBookingController {
	@FXML
    private TableView<Booking> bookedTable;
    @FXML
    private TableColumn<Booking, String> movieColumn;
    @FXML
    private TableColumn<Booking, String> dateColumn;
    @FXML
    private TableColumn<Booking, String> timeColumn;
    @FXML
    private TableColumn<Booking, String> seatColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;
    @FXML
    private TableColumn<Booking, String> ticketpriceColumn;
    @FXML
    private TableColumn<Booking, String> usernameColumn;
    @FXML
    private TableColumn<Booking, String> firstnameColumn;
    @FXML
    private TableColumn<Booking, String> lastnameColumn;
    @FXML
    private TableColumn<Booking, String> IDColumn;
    @FXML
    Label totalincome,totalticket;
     
    private static double total_income;
    private static int total_ticket;
    		
    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
    	   System.out.println("Initializing ManageBookingController...");
    	    System.out.println("movieColumn: " + movieColumn);
    	    
    	    if (movieColumn == null) {
    	        System.out.println("movieColumn is NULL! Check FXML.");
    	        return;
    	    }
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("movie"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ticketpriceColumn.setCellValueFactory(new PropertyValueFactory<>("ticketprice"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        

        loadBookings();
        totalincome();
        totaticket();
    }
    
    
	@FXML
	public void Backbotton(ActionEvent event) throws IOException {
		Main.switchScene(event, "EmployeeDashboard.fxml");
	}
	
	private void loadBookings() {

        Set<Booking> allBookings = JSONManager.loadBookedSeats();
        
        bookingList.setAll(allBookings);
        bookedTable.setItems(bookingList);
    }
	
	@FXML
    public void cancelBooking(ActionEvent event) {
        Booking booking = bookedTable.getSelectionModel().getSelectedItem();
        if (booking == null) return; // Avoid NullPointerException if no booking is selected

        booking.setStatus("Cancelled");

        // Update data in JSON file
        Set<Booking> allBookings = JSONManager.loadBookedSeats();
        for (Booking b : allBookings) {
            if (b.getID().equals(booking.getID())) {
                b.setStatus("Cancelled");
                break;
            }
        }
        JSONManager.saveBookedSeats(allBookings);
        bookedTable.refresh();
        totalincome();
        totaticket();
	}
	
	@FXML
    public void confirmBooking(ActionEvent event) {
        Booking booking = bookedTable.getSelectionModel().getSelectedItem();
        if (booking == null) return; // Avoid NullPointerException if no booking is selected

        booking.setStatus("Confirmed");

        // Update data in JSON file
        Set<Booking> allBookings = JSONManager.loadBookedSeats();
        for (Booking b : allBookings) {
            if (b.getID().equals(booking.getID())) {
                b.setStatus("Confirmed");
                break;
            }
        }
        JSONManager.saveBookedSeats(allBookings);
        bookedTable.refresh();
        totalincome();
        totaticket();
	}
	public void totalincome() {
        // Calculate the total price of all bookings that have been confirmed or booked
        double total = bookingList.stream()
                .filter(booking -> booking.getStatus().equals("Confirmed"))  // Filter only booked tickets
                .mapToDouble(booking -> booking.getTicketprice())  // Extract the ticket price and sum it up
                .sum();

        // Update the total price label
        total_income = total;
        totalincome.setText(String.valueOf(total));
    }
	public void totaticket() {
        double total = bookingList.stream()
                .filter(booking -> booking.getStatus().equals("Confirmed")).count();

        total_ticket = (int) total;
        totalticket.setText(String.valueOf(total_ticket));
    }


	public static double getTotal_income() {
		return total_income;
	}


	public static int getTotal_ticket() {
		return total_ticket;
	}
	
}
