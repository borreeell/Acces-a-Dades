import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class PBP_RA1_P2 {

    public static void main(String[] args) {
        int opcio = 0; // Variable per controlar l'accio seleccionada pel usuari
        Scanner teclat = new Scanner(System.in); // Scanner per llegir l'entrada del teclat

        // Bucle principal del menu
        while (opcio != 3) {
            System.out.println("**********************");
            System.out.println("******* MENÚ *********");
            System.out.println("1. Registrar usuaris");
            System.out.println("2. Log In");
            System.out.println("3. Sortir");
            System.out.println("**********************");
            System.out.print("--Escull opcio: ");

            // Llegim l'opcio del menu
            if (teclat.hasNextInt()) {
                opcio = teclat.nextInt(); // Llegim l'entrada nomes si es un nombre enter
                teclat.nextLine(); // Netegem el buffer
            } else {
                System.out.println("Opció no vàlida"); // Si no es un numero, mostrem un error
                teclat.nextLine();
                continue; // Tornem al principi del bucle
            }

            // Executem l'accio corresponent segons l'opcio escollida
            switch (opcio) {
                case 1:
                    escriureUsuariContrasenya(teclat); // Crida al metode per afegir usuaris
                    break;
                case 2:
                    System.out.print("Introdueix usuari: ");
                    String usuari = teclat.nextLine(); // Demanem l'usuari 

                    System.out.print("Introdueix contrasenya: ");
                    String contrasenya = teclat.nextLine(); // Demanem la contrasenya

                    // Comprovem si l'usuari es correcte comprovant el fitxer
                    int resultat = validarUsuariContrasenya("usuaris.txt", usuari, contrasenya);

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
                    break; // Sortim del programa
                default:
                    System.out.println("Opció no vàlida.");
                    break; // Si no es 1, 2 o 3 sortim del programa
            }
            System.out.println();
        }

        teclat.close();
    }

    // Funció per registrar usuaris
    public static void escriureUsuariContrasenya(Scanner sc) {
        // Obre el fitxer usuaris.txt per escriure els usuaris
        try (FileWriter fw = new FileWriter("usuaris.txt", true)) {
            for (int i = 0; i < 15; i++) {
                System.out.print("Nom usuari (enter per acabar): "); // Entrem el nom d'usuari
                String usuari = sc.nextLine();

                if (usuari.isEmpty()) {
                    break; // Si el camp d'usuari esta buit, sortim del programa
                }

                System.out.print("Contrasenya: "); // Entrem la contrasenya
                String contrasenya = sc.nextLine();

                fw.write(usuari + ":" + contrasenya + "\n"); // Escrivim usuari i contrasenya
            }
            System.out.println("Usuaris registrats correctament.");
        } catch (Exception e) {
            System.out.println("Error escrivint al fitxer: " + e.getMessage());
        }
    }

    // Funció per validar usuari i contrasenya
    public static int validarUsuariContrasenya(String fitxer, String usuari, String contrasenya) {
        // Obrim el fitxer i el llegim linia per linia amb el bufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(fitxer))){
            String linia; // Guardem la linia amb un string

            while ((linia = br.readLine()) != null) {
                String[] parts = linia.split(":"); // Separem les dues parts de la linia, separant per :
                if (parts.length == 2) {
                    String u = parts[0].trim(); // Guardem la 1ra part usuari
                    String c = parts[1].trim(); // Guardem la 2na part contrasenya
                    
                    // Comprovem si usuari o contrasenya son incorrectes, en cas que els dos siguin correctes, retorna 1
                    if (u.equals(usuari)) {
                        if (c.equals(contrasenya)) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return 0;
    }
}
