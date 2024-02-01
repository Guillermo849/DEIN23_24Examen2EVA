package conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* Establece la conexion con la base de datos */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBDD {
	private Properties prop;
    private Connection conexion;
    public ConexionBDD()  {
    	prop = new Properties();
		try (FileInputStream fis = new FileInputStream(
				new File(this.getClass().getResource("/properties/properties.properties").getPath()))) {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try {
    		String host = prop.getProperty("db.url");
			String baseDatos = prop.getProperty("db.name");
			String usuario = prop.getProperty("db.user");
			String password = prop.getProperty("db.pwd");
			String cadenaConexion = "jdbc:mysql://" + host + ":3306/" + baseDatos;
			conexion = DriverManager.getConnection(cadenaConexion, usuario, password);
            conexion.setAutoCommit(true);
    	} catch (SQLException e) {
    		e.printStackTrace();
		}
        
    }
    public Connection getConexion() {
        return conexion;
    }
    public void CloseConexion() {
    	try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
}
