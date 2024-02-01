package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;

import gestorBDD.GestorBDDProductos;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Producto;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * JavaFX clase que mostrara la información de la base de Datos Local
 * examen1.productos. Está información se podrá modificar y eliminar. Se podrá
 * añadir productos nuevos.
 */
public class ProductosController implements Initializable {

	@FXML
	private MenuItem mnItAcercaDe;

	@FXML
	private MenuItem mnItOffline;

	@FXML
	private MenuItem mnItOnline;

	@FXML
	private TextField txtFCodigo;

	@FXML
	private TextField txtFNombre;

	@FXML
	private TextField txtFPrecio;

	@FXML
	private Button btnSubirImagen;

	@FXML
	private CheckBox chbxDisponible;

	@FXML
	private Button btnCrear;

	@FXML
	private Button btnActualizar;

	@FXML
	private Button btnLimpiar;

	@FXML
	private TableView<Producto> tbViewProductos;

	@FXML
	private TableColumn<Producto, String> tbColCodigo;

	@FXML
	private TableColumn<Producto, String> tbColNombre;

	@FXML
	private TableColumn<Producto, Float> tbColPrecio;

	@FXML
	private TableColumn<Producto, Integer> tbColDisponible;

	private GestorBDDProductos productosGstr;

	private Producto productoModEli;

	/**
	 * Selecciona un producto de la tabla y pondrá la información de esté en los
	 * campos para editarlo. Una vez seleccionado el producto se deshabilitará el
	 * botón Crear y se habilitará el boton actualizar.
	 * 
	 * @param event Este método se ejecutará cuando clickemos en la tabla
	 */
	void selectProducto(MouseEvent event) {
		if (tbViewProductos.getSelectionModel().getSelectedItem() != null) {
			productoModEli = tbViewProductos.getSelectionModel().getSelectedItem();
			txtFCodigo.setText(productoModEli.getCodigo());
			txtFNombre.setText(productoModEli.getNombre());
			txtFPrecio.setText(productoModEli.getPrecio() + "");
			if (productoModEli.getDisponible() == 1)
				chbxDisponible.setSelected(true);
			else
				chbxDisponible.setSelected(false);
			btnActualizar.setDisable(false);
			btnCrear.setDisable(true);
		}
	}

	/**
	 * Actualiza el producto seleccionado si todos los campos están correctos, de lo
	 * contrario saltará una ventana de error explicanod que campos están mal o
	 * faltan.
	 * 
	 * @param event Se ejecutará este método al hacer clickar en el botón Actualizar
	 */
	@FXML
	void actualizarProducto(ActionEvent event) {
		if (txtFCodigo.getText().length() != 5 || txtFNombre.getText().isEmpty() || txtFPrecio.getText().isEmpty()
				|| !txtFPrecio.getText().matches("[-+]?[0-9]*\\.?[0-9]+")) {
			Alert alertWindows = new Alert(Alert.AlertType.ERROR);

			Stage stage = (Stage) alertWindows.getDialogPane().getScene().getWindow();

			alertWindows.setHeaderText(null);
			String mensaje = "";
			if (txtFCodigo.getText().length() != 5) {
				mensaje += "El Código tiene que ser de 5 caracteres\n";
			}
			if (txtFNombre.getText().isEmpty()) {
				mensaje += "El campo Nombre es Obligatorio \n";
			}
			if (txtFPrecio.getText().isEmpty()) {
				mensaje += "El campo Precio es Obligatorio \n";
			}
			if (!txtFPrecio.getText().matches("[-+]?[0-9]*\\.?[0-9]+")) {
				mensaje += "El campo Precio debe ser númerico \n";
			}

			alertWindows.setContentText(mensaje);
			alertWindows.showAndWait();
		} else {
			String codigo = txtFCodigo.getText().toString();
			String nombre = txtFNombre.getText().toString();
			Float precio = Float.parseFloat(txtFPrecio.getText().toString());
			int disponible;
			if (chbxDisponible.isSelected())
				disponible = 1;
			else
				disponible = 0;
			Producto p = new Producto(codigo, nombre, precio, disponible);
			productosGstr.modProducto(productoModEli, p);

			limpiarSelect(new ActionEvent());

			tbViewProductos.setItems(productosGstr.cargarProductos());
		}
	}

	/**
	 * Insertará en la base de datos un nuevo producto si todos los campos están
	 * correctos, de lo contrario saltará una ventana de error explicanod que campos
	 * están mal o faltan.
	 * 
	 * @param event Se ejecutará este método al hacer clickar en el botón Crear.
	 */
	@FXML
	void crearProducto(ActionEvent event) {

		if (txtFCodigo.getText().length() != 5 || txtFNombre.getText().isEmpty() || txtFPrecio.getText().isEmpty()
				|| !txtFPrecio.getText().matches("[-+]?[0-9]*\\.?[0-9]+")) {
			Alert alertWindows = new Alert(Alert.AlertType.ERROR);

			Stage stage = (Stage) alertWindows.getDialogPane().getScene().getWindow();

			alertWindows.setHeaderText(null);
			String mensaje = "";
			if (txtFCodigo.getText().length() != 5) {
				mensaje += "El Código tiene que ser de 5 caracteres\n";
			}
			if (txtFNombre.getText().isEmpty()) {
				mensaje += "El campo Nombre es Obligatorio \n";
			}
			if (txtFPrecio.getText().isEmpty()) {
				mensaje += "El campo Precio es Obligatorio \n";
			}
			if (!txtFPrecio.getText().matches("[-+]?[0-9]*\\.?[0-9]+")) {
				mensaje += "El campo Precio debe ser númerico \n";
			}

			alertWindows.setContentText(mensaje);
			alertWindows.showAndWait();
		} else {
			String codigo = txtFCodigo.getText().toString();
			String nombre = txtFNombre.getText().toString();
			Float precio = Float.parseFloat(txtFPrecio.getText().toString());
			int disponible;
			if (chbxDisponible.isSelected())
				disponible = 1;
			else
				disponible = 0;
			Producto p = new Producto(codigo, nombre, precio, disponible);
			productosGstr.insertProducto(p);

			limpiarSelect(new ActionEvent());

			tbViewProductos.setItems(productosGstr.cargarProductos());
		}
	}

	/**
	 * Desselecciona el producto seleccionado de la tabla. Quitando la información
	 * de este de los campos.
	 * 
	 * @param event Se ejecutará este método al hacer clickar en el botón Limpiar.
	 */
	@FXML
	void limpiarSelect(ActionEvent event) {
		productoModEli = null;
		txtFCodigo.setText(null);
		txtFNombre.setText(null);
		txtFPrecio.setText(null);
		chbxDisponible.setSelected(false);
		btnActualizar.setDisable(true);
		btnCrear.setDisable(false);
	}

	/**
	 * Saltará una ventana de información que mostrará un mensaje.
	 * 
	 * @param event Se ejecutará este método al hacer clickar en el menu Item Acerca
	 *              de...
	 */
	@FXML
	void mostrarAcercaDe(ActionEvent event) {
		Alert info = new Alert(Alert.AlertType.INFORMATION);
		info.setTitle("Acerca de...");
		info.setHeaderText(null);
		String mensaje = "Acerca de...";
		info.setContentText(mensaje);
		info.showAndWait();
	}

	@FXML
	void ayudaOffline(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visorWebOffline.fxml"));
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Ayuda");
			stage.show();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			e.printStackTrace();
			alert.showAndWait();
		}
	}

	@FXML
	void ayudaOnline(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/visorWeb.fxml"));
		Parent root;
		try {
			root = loader.load();
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Web Online");
			stage.show();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText(e.getMessage());
			e.printStackTrace();
			alert.showAndWait();
		}
	}

	
	private void mostrarInforme(ActionEvent event) {
		try {
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("nomMedico", productoModEli.getCodigo().toString());
			parameters.put("espMedico", productoModEli.getNombre().toString());
			parameters.put("numMedico", productoModEli.getPrecio()+"");
			parameters.put("nomPaciente", productoModEli.getDisponible()+"");

			JasperReport report = (JasperReport) JRLoader
					.loadObject(getClass().getResource("informe_medico.jasper"));
			JasperPrint jprint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			JasperViewer viewer = new JasperViewer(jprint, false);
			viewer.setVisible(true);
		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setTitle("ERROR");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina el producto seleccionado de la Base de Datos.
	 */
	private void eliminarProducto(ActionEvent event) {
		productosGstr.eliminarProducto(productoModEli);
		limpiarSelect(event);
		tbViewProductos.setItems(productosGstr.cargarProductos());
	}

	/**
	 * Inicializa la información que contendrá la TableView y sus columnas al igual
	 * que el gestor de la Base de Datos producto. El TableView tendrá un
	 * ContextMenu para eliminar o mostrar la imagen del producto seleccionado.
	 * 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		productosGstr = new GestorBDDProductos();

		tbColCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCodigo()));
		tbColNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
		tbColPrecio.setCellValueFactory(new PropertyValueFactory<Producto, Float>("precio"));
		tbColDisponible.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("disponible"));

		tbViewProductos.setItems(productosGstr.cargarProductos());
		tbViewProductos.setOnMouseClicked(e -> selectProducto(e));

		/**
		 * Creación del Context Menu y sus Menu Items.
		 */
		ContextMenu contextMenu = new ContextMenu();
		MenuItem verJasperItem = new MenuItem("Ver Informe");
		MenuItem eliminarItem = new MenuItem("Eliminar");

		verJasperItem.setOnAction(event -> mostrarInforme(event));
		eliminarItem.setOnAction(event -> eliminarProducto(event));

		contextMenu.getItems().addAll(verJasperItem, eliminarItem);
		tbViewProductos.setContextMenu(contextMenu);
	}

	
}
