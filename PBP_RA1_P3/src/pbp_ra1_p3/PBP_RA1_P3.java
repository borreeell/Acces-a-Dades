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

        while (opcio != 2) {
            System.out.println("--Escull opcio: ");
            opcio = teclat.nextInt(); // Cridem al metode nextInt per llegir el numero i guardar a opcio
            teclat.nextLine(); // Consumir el salt de línia

            System.out.println("");
            seleccio(opcio, teclat); // Executar l'exercici escollit
        }

        teclat.close();
    }

    // Mostra el menu pel terminal
    private static void menu() {
        System.out.println("************************************");
        System.out.println("******* MENÚ - Pau Borrell *********");
        System.out.println("1.- Llegir dades jugadors i partides");
        System.out.println("2.- Jugador amb mes copes");
        System.out.println("3.- Extreure dades meteo");
        System.out.println("4.- Sortir");
        System.out.println("************************************");
    }

    private static void seleccio(int opcio, Scanner teclat) {
        switch (opcio) {
            case 1:
                Exercicis.exercici1(teclat);
                break;
            case 2:
                Exercicis.exercici2();
                break;
            case 3:
                Exercicis.exercici3();
                break;
            case 4:
                System.out.println("Sortint del programa...");
                break;
            default:
                System.out.println("Opció no vàlida!");
                break;
        }
    }
}