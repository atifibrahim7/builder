module builder {
	requires javafx.controls;
	requires javafx.fxml;
	 requires java.sql; // Required for JDBC
	opens application to javafx.graphics, javafx.fxml;
	exports application ; 
}
