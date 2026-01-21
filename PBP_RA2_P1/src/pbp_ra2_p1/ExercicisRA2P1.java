package pbp_ra2_p1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Scanner;

/**
 *
 * @author AluCiclesGS1
 */
public class ExercicisRA2P1 { 
    
    // Connexió estàtica compartida
    private static Connection connection = null;
    
    // Paràmetres de connexió
    private static final String URL = "jdbc:postgresql://127.0.0.1:5433/clash";
    private static final String USER = "postgres";
    private static final String PASSWORD = "accedir";
    
    /**
     * Obté la connexió a la base de dades (Singleton)
     * Si no existeix, la crea. Si ja existeix, la reutilitza.
     * @return Connection
     * @throws SQLException
     */
    private static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Registrar el driver de PostgreSQL
            try { 
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }
            
            // Crear nova connexió
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Nova connexió creada a la db");
        }

        return connection;
    }
    
    /**
     * Tanca la connexió a la base de dades
     */
    public static void tancarConnexio() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexió tancada");
            }
        } catch (SQLException e) {
            System.err.println("Error al tancar la connexió: " + e.getMessage());
        }
    }
    
    /**
     * Llista tots els jugadors i les seves partides
     * @throws SQLException
     */
    public static void exercici1() throws SQLException {
        Connection conn = getConnection();
        
        try {
            // Consulta per obtenir tots els jugadors
            Statement stmtJugadors = conn.createStatement();
            ResultSet rsJugadors = stmtJugadors.executeQuery(
                "SELECT id, nom, copes, nivell FROM jugadors ORDER BY id"
            );
            
            System.out.println("=== LLISTAT DE JUGADORS I LES SEVES PARTIDES ===\n");
            
            // Recórrer tots els jugadors
            while (rsJugadors.next()) {
                int idJugador = rsJugadors.getInt("id");
                String nom = rsJugadors.getString("nom");
                int copes = rsJugadors.getInt("copes");
                int nivell = rsJugadors.getInt("nivell");
                
                // Mostrar informació del jugador
                System.out.println("Jugador: " + nom + " (ID: " + idJugador + ")");
                System.out.println("Copes: " + copes + " | Nivell: " + nivell);
                System.out.println("Partides:");
                
                // Consulta per obtenir les partides d'aquest jugador
                Statement stmtPartides = conn.createStatement();
                String queryPartides = 
                    "SELECT p.id, p.\"idJug1\", p.\"idJug2\", p.resultat, p.temps, p.tipus, " +
                    "j1.nom AS nomJug1, j2.nom AS nomJug2 " +
                    "FROM partides p " +
                    "JOIN jugadors j1 ON p.\"idJug1\" = j1.id " +
                    "JOIN jugadors j2 ON p.\"idJug2\" = j2.id " +
                    "WHERE p.\"idJug1\" = " + idJugador + " OR p.\"idJug2\" = " + idJugador + " " +
                    "ORDER BY p.id";
                
                ResultSet rsPartides = stmtPartides.executeQuery(queryPartides);
                
                boolean tePartides = false;
                while (rsPartides.next()) {
                    tePartides = true;
                    int idPartida = rsPartides.getInt("id");
                    int idJug1 = rsPartides.getInt("idJug1");
                    int idJug2 = rsPartides.getInt("idJug2");
                    String nomJug1 = rsPartides.getString("nomJug1");
                    String nomJug2 = rsPartides.getString("nomJug2");
                    String resultat = rsPartides.getString("resultat");
                    String temps = rsPartides.getString("temps");
                    String tipus = rsPartides.getString("tipus");
                    
                    // Determinar el guanyador comparant l'string resultat amb els IDs
                    String guanyador;
                    if (resultat != null && resultat.equals(String.valueOf(idJug1))) {
                        guanyador = nomJug1;
                    } else if (resultat != null && resultat.equals(String.valueOf(idJug2))) {
                        guanyador = nomJug2;
                    } else {
                        guanyador = resultat != null ? resultat : "Empat";
                    }
                    
                    // Mostrar informació de la partida
                    System.out.println("  - Partida #" + idPartida + ": " + nomJug1 + 
                        " vs " + nomJug2 + " (Temps: " + temps + 
                        ", Tipus: " + tipus + ") → Guanyador: " + guanyador
                    );
                }
                
                if (!tePartides) {
                    System.out.println("  (Sense partides)");
                }
                
                rsPartides.close();
                stmtPartides.close();
                
                System.out.println();
            }
            
            rsJugadors.close();
            stmtJugadors.close();
            
        } catch (SQLException e) {
            System.err.println("Error en l'exercici 1: " + e.getMessage());
            throw e;
        } finally {
            tancarConnexio();
        }
    }
    
    /**
     * Afegeix un nou jugador a la base de dades
     * @throws SQLException
     */
    public static void exercici2() throws SQLException {
        Connection conn = getConnection(); // Crea una connexio a la base de dades
        Scanner input = new Scanner(System.in); // Crea un objecte Scanner

        try {
            System.out.print("Introdueix el nom del jugador: ");
            String nom = input.nextLine().trim(); // Demanem el nom del nou jugador

            // Validació del nom, el nom no pot estar buit
            while (nom.isEmpty()) {
                System.out.print("El nom no pot estar buit. Torna-ho a intentar: ");
                nom = input.nextLine().trim();
            }

            // Sentencia INSERT amb preparedStatement per protegir-se de injeccio SQL
            String sqlInsert = "INSERT INTO jugadors (id, nom) VALUES (?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sqlInsert);

            // Parametres a inserir
            psInsert.setInt(1, ultimIdJugador());
            psInsert.setString(2, nom);
            
            // Executa la actualitzacio per insertar la informacio a la base de dades
            psInsert.executeUpdate();

            psInsert.close();
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
        } finally {
            tancarConnexio();
        }

        input.close();
    }

    /**
     * Modifica un jugador existent
     * @param teclat
     * @throws SQLException
     */
    public static void exercici3(Scanner teclat) throws SQLException {
        Connection conn = getConnection(); // Crea una connexio a la base de dades

        try {
            // Demana la ID del jugador que vol editar l'usuari
            System.out.println("Entra la ID del jugador que vols editar: ");
            int idJugador = teclat.nextInt();
            teclat.nextLine();

            // Consulta per obtenir les dades actuals del jugador
            String sqlSelect = "SELECT nom, nivell, copes, oro, gemes FROM jugadors WHERE id = ?";
            PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
            ResultSet rs;

            psSelect.setInt(1, idJugador);
            rs = psSelect.executeQuery();

            if (rs.next()) {
                // Guarda les dades actuals
                String nomActual = rs.getString("nom");
                int nivellActual = rs.getInt("nivell");
                int oroActual = rs.getInt("oro");
                int gemesActuals = rs.getInt("gemes");
                int copesActuals = rs.getInt("copes");

                // Demana els nous valors i modifica els actuals si canvien
                System.out.println("Nom del jugador (" + nomActual + "): ");
                String nomEditat = teclat.nextLine().trim();
                if (nomEditat.isEmpty()) nomEditat = nomActual;

                System.out.println("Nivell del jugador (" + nivellActual + "): ");
                String nivellInput = teclat.nextLine().trim();
                int nouNivell = nivellInput.isEmpty() ? nivellActual : Integer.parseInt(nivellInput);

                System.out.println("Or del jugador (" + oroActual + "): ");
                String oroInput = teclat.nextLine().trim();
                int nouOro = oroInput.isEmpty() ? oroActual : Integer.parseInt(oroInput);

                System.out.println("Gemes del jugador (" + gemesActuals + "): ");
                String inputGemes = teclat.nextLine().trim();
                int novesGemes = inputGemes.isEmpty() ? gemesActuals : Integer.parseInt(inputGemes);

                System.out.println("Copes del jugador (" + copesActuals + "): ");
                String copesInput = teclat.nextLine().trim();
                int novesCopes = copesInput.isEmpty() ? copesActuals : Integer.parseInt(copesInput);

                // Actualitza les dades del jugador
                String sqlUpdate = "UPDATE jugadors SET nom = ?, nivell = ?, oro = ?, gemes = ?, copes = ? WHERE id = ?";
                PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
                
                psUpdate.setString(1, nomEditat);
                psUpdate.setInt(2, nouNivell);
                psUpdate.setInt(3, nouOro);
                psUpdate.setInt(4, novesGemes);
                psUpdate.setInt(5, novesCopes);
                psUpdate.setInt(6, idJugador);

                psUpdate.executeUpdate();
                psUpdate.close();

                System.out.println("Valors actualitzats: Nom = " + nomEditat + ", Nivell = " + nouNivell + ", Copes = " + novesCopes + ", Oro = " + nouOro + ", Gemes = " + novesGemes);

            } else {
                System.out.println("No s'ha trobat cap jugador amb aquest ID");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e);
        } finally {
            tancarConnexio();
        }
    }

    /**
     * Afegeix una partida
     * @param teclat
     * @throws SQLException
     */
    public static void exercici4(Scanner teclat) throws SQLException {
        Connection conn = getConnection();

        try {
            // Llistar jugadors
            System.out.println("=== Jugadors disponibles ===");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom FROM jugadors ORDER BY id");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Nom: " + rs.getString("nom"));
            }

            rs.close();
            stmt.close();

            // Demanar qui es el primer i segon jugador
            System.out.println("Entra la ID del primer jugador: ");
            int idJugador1 = teclat.nextInt();
            System.out.println("Entra la ID del segon jugador: ");
            int idJugador2 = teclat.nextInt();

            teclat.nextLine();

            // Demanar resultat, tipus i durada
            System.out.println("Entra el resultat de la partida: ");
            String resultat = teclat.nextLine().trim();
            System.out.println("Entra el tipus de partida: ");
            String tipus = teclat.nextLine().trim();
            System.out.println("Entra la durada de la partida: ");
            String durada = teclat.nextLine().trim();

            // Insertar la partida
            String sqlInsert = "INSERT INTO partides(id, \"idJug1\", \"idJug2\", resultat, temps, tipus) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlInsert);

            ps.setInt(1, ultimIdPartida());
            ps.setInt(2, idJugador1);
            ps.setInt(3, idJugador2);
            ps.setString(4, resultat);
            ps.setString(5, durada);
            ps.setString(6, tipus);

            ps.executeUpdate();
            ps.close();

            System.out.println("Partida afegida.");
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            tancarConnexio();
        }

        teclat.close();
    }
    
    /**
     * Retorna l'últim ID dels jugadors
     * @return int
     * @throws SQLException
     */
    private static int ultimIdJugador() throws SQLException {
        int id = 0;
        Connection conn = getConnection();
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM jugadors ORDER BY id DESC LIMIT 1");
            
            if (rs.next()) {
                id = rs.getInt("id");
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
        }
        
        return id + 1;
    }

    /**
     * Retorna l'ID de la ultima partida
     * @return int
     * @throws SQLException
     */
    private static int ultimIdPartida() throws SQLException {
        int id = 0;
        Connection conn = getConnection();
        
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM partides ORDER BY id DESC LIMIT 1");
            
            if (rs.next()) {
                id = rs.getInt("id");
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());
        }
        
        return id + 1;
    }
}