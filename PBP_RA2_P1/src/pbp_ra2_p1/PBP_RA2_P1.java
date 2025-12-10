/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra2_p1;

import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author AluCiclesGS1
 */
public class PBP_RA2_P1 {

    /**1
     * 
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {     
        int opcio = -1;
        
        Scanner teclat = new Scanner(System.in);
        
        menu();
        
        while (opcio != 0) {
            System.out.println("-- Escull una opcio: ");
            opcio = teclat.nextInt();
            
            System.out.println("");
            seleccio(opcio);
        }
    }
    
    private static void menu() {
        System.out.println("****************************************");
        System.out.println("********** MENÚ - Pau Borrell **********");
        System.out.println("1.- Llistar els jugadors i partides");
        System.out.println("2.- Afegir un jugador");
        System.out.println("0.- Sortir");
        System.out.println("****************************************");
    }
    
    private static void seleccio(int opcio) throws SQLException {
        switch (opcio) {
            case 1:
                Exercicis.exercici1();
                break;
            case 2:
                break;
            case 0:
                System.out.println("Sortint del programa....");
                break;
            default:
                break;
        }
    }
}
