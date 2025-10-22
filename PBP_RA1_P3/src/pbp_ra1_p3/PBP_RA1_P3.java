package pbp_ra1_p3;

import java.util.Scanner;

/**
 *
 * @author Pau Borrell
 */
public class PBP_RA1_P3 {

    public static void main(String[] args) {
        int opcio = 0;

        Scanner teclat = new Scanner(System.in); // Creem objecte Scanner de lectura

        menu();

        while (opcio != 6) {
            System.out.println("--Escull opcio: ");
            opcio = teclat.nextInt(); // Cridem al metode nextInt per llegir el numero i guardar a opcio

            System.out.println("");
            seleccio(opcio);
        }

        teclat.close();
    }

    private static void menu() {
        System.out.println("************************************");
        System.out.println("******* MENÚ - Pau Borrell *********");
        System.out.println("1.- Llegir dades jugadors i partides");
        System.out.println("2.- Sortir");
        System.out.println("************************************");
    }

    private static void seleccio(int opcio) {
        switch (opcio) {
            case 1:
                Exercicis.practica3_1();
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}
