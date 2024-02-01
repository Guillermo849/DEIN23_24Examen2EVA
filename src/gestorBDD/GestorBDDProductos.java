package gestorBDD;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexion.ConexionBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Producto;

public class GestorBDDProductos {
	private ConexionBDD conexion;

	/* Devuelve una lista de las Productos almacenadas en la base de datos */
	public ObservableList<Producto> cargarProductos() {

		ObservableList<Producto> Productos = FXCollections.observableArrayList();
		try {
			conexion = new ConexionBDD();
			String consulta = "select * from productos";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String codigo = rs.getString("codigo");
				String nombre = rs.getString("nombre");
				float precio = rs.getFloat("precio");
				int disponible = rs.getInt("disponible");
				Producto p = new Producto(codigo, nombre, precio, disponible);
				Productos.add(p);
			}
			rs.close();
			conexion.CloseConexion();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Productos;
	}

	/* Insertará una nueva Producto en la base de datos */
	public void insertProducto(Producto producto) {

		String codigo = producto.getCodigo();
		String nombre = producto.getNombre();
		float precio = producto.getPrecio();
		int disponible = producto.getDisponible();

		conexion = new ConexionBDD();
		String consulta = "INSERT INTO productos (codigo, nombre, precio, disponible) VALUES (?,?,?,?)";
		try {
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.setString(1, codigo);
			pstmt.setString(2, nombre);
			pstmt.setFloat(3, precio);
			pstmt.setInt(4, disponible);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Modificará una Producto de la base de datos */
	public void modProducto(Producto productoViejo, Producto productoNuevo) {
		
		String codigo = productoNuevo.getCodigo();
		String nombre = productoNuevo.getNombre();
		float precio = productoNuevo.getPrecio();
		int disponible = productoNuevo.getDisponible();

		try {
			conexion = new ConexionBDD();
			String consulta = "UPDATE productos SET codigo=?, nombre=?, precio=?, disponible=? WHERE codigo LIKE ?";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.setString(1, codigo);
			pstmt.setString(2, nombre);
			pstmt.setFloat(3, precio);
			pstmt.setInt(4, disponible);
			pstmt.setString(5, productoViejo.getCodigo());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Eliminará una Producto en la base de datos */
	public void eliminarProducto(Producto producto) {

		try {
			conexion = new ConexionBDD();
			String consulta = "DELETE FROM productos WHERE codigo LIKE ?;";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.setString(1, producto.getCodigo());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}