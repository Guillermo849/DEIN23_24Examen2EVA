module DEIN23_24Examen1EVA {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.web;
	requires javafx.fxml;
	requires javafx.media;
	requires javafx.graphics;
	requires java.sql;
	requires jasperreports;
	
	opens model to javafx.base;
	opens application to javafx.graphics, javafx.fxml;
	opens controllers to javafx.graphics, javafx.fxml;
}
