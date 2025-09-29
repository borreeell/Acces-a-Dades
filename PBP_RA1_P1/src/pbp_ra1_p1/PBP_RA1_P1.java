/*
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra1_p1;

import java.util.Scanner;

/**
 * @author Pau Borrell
 */
public class PBP_RA1_P1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcio = 0;

        Scanner teclat = new Scanner(System.in); // Creem objecte Scanner de lectura

        menu();

        while (opcio != 6) {
            System.out.println(" --Escull opció: ");
            opcio = teclat.nextInt(); // Cridem metode nextInt per llegir el numero i guardar a opcio
            
            System.out.println("");
            seleccio(opcio); // Executar l'exercici escollit
        }
        teclat.close();
    }

    private static void menu() {
        System.out.println("**********************");
        System.out.println("******* MENÚ *********");
        System.out.println("1.- Comprova fitxers");
        System.out.println("2.- Llista d'arxius");
        System.out.println("3.- Crear arxiu TXT");
        System.out.println("4.- Mostrar contingut");
        System.out.println("5.- Crear directori i arxiu");
        System.out.println("6.- Sortir");
        System.out.println("**********************");
    }
    
    private static void seleccio(int opcio) {
        switch(opcio) {
            case 1:
                Exercicis.exercici2();
                break;
            case 2:
                Exercicis.exercici3();
                break;
            case 3:
                Exercicis.exercici4();
                break;
            case 4:
                try {
                    Exercicis.exercici5();
                } catch (Exception e) {
                    System.out.println("Error.");
                }
                break;
            case 5:
                Exercicis.exercici6();
                break;
            case 6:
                break;
            default:
                break;
            
        }
    }
}
