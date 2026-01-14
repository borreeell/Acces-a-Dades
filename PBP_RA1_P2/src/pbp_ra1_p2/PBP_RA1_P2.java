import java.util.Scanner;

public class PBP_RA1_P2 {

    /**
     * @author Pau Borrell
     */
    public static void main(String[] args) {
        int opcio = 0;

        Scanner teclat = new Scanner(System.in); // Crea l'objecte Scanner per llegir input de l'usuari

        menu();

        while (opcio != 3) {
            System.out.println("-- Escull opcio: ");
            opcio = teclat.nextInt(); // Cridem al metode nextInt per llegir numero i guardar opcio

            System.out.println(" ");
            seleccio(opcio, teclat); // Executar l'opcio escollida
        }

        teclat.close(); // Tanca l'objecte teclat
    }

    private static void menu() {
        System.out.println("**********************");
        System.out.println("******* MENÚ *********");
        System.out.println("1. Registrar usuaris");
        System.out.println("2. Log In");
        System.out.println("3. Sortir");
        System.out.println("**********************");
    }

    private static void seleccio(int opcio, Scanner teclat) {
        // Executem l'accio corresponent segons l'opcio escollida
        switch (opcio) {
            case 1:
                ExercicisRA1P2.exercici1(teclat); // Crida al metode per afegir usuaris
                break;
            case 2:
                System.out.print("Introdueix usuari: ");
                String usuari = teclat.nextLine(); // Demanem l'usuari 

                System.out.print("Introdueix contrasenya: ");
                String contrasenya = teclat.nextLine(); // Demanem la contrasenya

                // Comprovem si l'usuari es correcte comprovant el fitxer
                int resultat = ExercicisRA1P2.exercici2("usuaris.txt", usuari, contrasenya);

                if (resultat == 1) {
                    System.out.println("Usuari i contrasenya correctes. Benvingut " + usuari);
                } else if (resultat == -1) {
                    System.out.println("Contrasenya incorrecte");
                } else {
                    System.out.println("Usuari incorrecte");
                }
                break;
            case 3:
                System.out.println("Sortint del programa...");
                break;
            default:
                System.out.println("Opció no vàlida.");
                break;
        }
    }
}
