package pbp_ra2_p1;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * @author Pau Borrell
 */
public class PBP_RA2_P1 {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {     
        int opcio = 0;
        
        Scanner teclat = new Scanner(System.in); // Creem objecte scanner Scanner de lectura
        
        menu();
        
        // Mentre opcio sigui diferent de 5 (sortir)
        while (opcio != 5) { 
            System.out.println("-- Escull una opcio: ");
            opcio = teclat.nextInt(); // Cridem metode nextInt per llegir el numero i guardar a opcio
            
            System.out.println("");
            seleccio(opcio, teclat); // Executar l'exercici escollit
        }
    }
    
    private static void menu() {
        System.out.println("****************************************");
        System.out.println("********** MENÚ - Pau Borrell **********");
        System.out.println("1.- Llistar els jugadors i partides");
        System.out.println("2.- Afegir un jugador");
        System.out.println("3.- Modificar un jugador");
        System.out.println("4.- Afegir una nova partida");
        System.out.println("5.- Sortir");
        System.out.println("****************************************");
    }
    
    private static void seleccio(int opcio, Scanner teclat) throws SQLException {
        switch (opcio) {
            case 1:
                ExercicisRA2P1.exercici1();
                break;
            case 2:
                ExercicisRA2P1.exercici2();
                break;
            case 3:
                ExercicisRA2P1.exercici3(teclat);
                break;
            case 4:
                ExercicisRA2P1.exercici4(teclat);
            case 5:
                System.out.println("Sortint del programa....");
                break;
            default:
                break;
        }
    }
}
