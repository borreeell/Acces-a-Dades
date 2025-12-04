/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra2_p1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author AluCiclesGS1
 */
public class Exercicis { 
    /**
     * @throws SQLException
     */
    public static void connectarDB() throws SQLException {
        // Registrar el driver de PostgreSQL
        try { 
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }
        
        Connection connection = null;
        // Database connect
        connection = (Connection) DriverManager.getConnection(
            "jdbc:postgresql://127.0.0.1:5433/clash",
            "postgres", 
            "accedir"
        );
        
        connection.close();
    }
}
