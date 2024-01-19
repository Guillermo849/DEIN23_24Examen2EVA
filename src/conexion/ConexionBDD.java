package conexion;

/* Establece la conexion con la base de datos */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

public class ConexionBDD {
    private Connection conexion;
    public ConexionBDD()  {
    	try {
    		String baseDatos = "paises";
            String host = "localhost";
            String usuario = "user3";
            String password = "user3";
            String cadenaConexion = "jdbc:mysql://" + host + "/" + baseDatos+ "?serverTimezone=" + TimeZone.getDefault().getID();
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
