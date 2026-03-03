module BookingMovieTicket {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.json;
	requires javafx.graphics;
	requires java.prefs;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
}
