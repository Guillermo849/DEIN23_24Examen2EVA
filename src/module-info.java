module LanzadorDeFormularios {
	requires javafx.controls;
	requires javafx.fxml;
	requires jasperreports;
	requires java.sql;
	
	
	opens aplicacion to javafx.graphics, javafx.fxml;
	opens controllers to javafx.graphics, javafx.fxml;
}
