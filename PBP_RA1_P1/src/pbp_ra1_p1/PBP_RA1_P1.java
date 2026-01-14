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
        System.out.println("************************************");
        System.out.println("******* MENÚ - Pau Borrell *********");
        System.out.println("1.- Comprova fitxers");
        System.out.println("2.- Llista d'arxius");
        System.out.println("3.- Crear arxiu TXT");
        System.out.println("4.- Mostrar contingut");
        System.out.println("5.- Crear directori i arxiu");
        System.out.println("6.- Exercici 7");
        System.out.println("7.- Sortir");
        System.out.println("************************************");
    }
    
    private static void seleccio(int opcio) {
        switch(opcio) {
            case 1:
                ExercicisP1.exercici2();
                break;
            case 2:
                ExercicisP1.exercici3();
                break;
            case 3:
                ExercicisP1.exercici4();
                break;
            case 4:
                try {
                    ExercicisP1.exercici5();
                } catch (Exception e) {
                    System.out.println("Error.");
                }
                break;
            case 5:
                ExercicisP1.exercici6();
                break;
            case 6:
                ExercicisP1.exercici7();
            case 7:
                break;
            default:
                break;
            
        }
    }
}
