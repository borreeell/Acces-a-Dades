import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class PBP_RA1_P2 {

    public static void main(String[] args) {
        int opcio = 0;
        Scanner teclat = new Scanner(System.in);

        while (opcio != 3) {
            System.out.println("**********************");
            System.out.println("******* MENÚ *********");
            System.out.println("1. Registrar usuaris");
            System.out.println("2. Log In");
            System.out.println("3. Sortir");
            System.out.println("**********************");
            System.out.print("--Escull opcio: ");

            // Llegim opcio
            if (teclat.hasNextInt()) {
                opcio = teclat.nextInt();
                teclat.nextLine(); // neteja buffer
            } else {
                System.out.println("Opció no vàlida");
                teclat.nextLine(); // neteja buffer
                continue;
            }

            switch (opcio) {
                case 1:
                    escriureUsuariContrasenya(teclat);
                    break;
                case 2:
                    System.out.print("Introdueix usuari: ");
                    String usuari = teclat.nextLine();

                    System.out.print("Introdueix contrasenya: ");
                    String contrasenya = teclat.nextLine();

                    boolean loginCorrecte = validarUsuariContrasenya("usuaris.txt", usuari, contrasenya);

                    if (loginCorrecte) {
                        System.out.println("Usuari i contrasenya correctes. Benvingut, " + usuari + "!");
                    } else {
                        System.out.println("Usuari o contrasenya incorrectes.");
                    }
                    break;
                case 3:
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
            System.out.println();
        }

        teclat.close();
    }

    // Funció per registrar usuaris
    public static void escriureUsuariContrasenya(Scanner sc) {
        try (FileWriter fw = new FileWriter("usuaris.txt", true)) {
            for (int i = 0; i < 15; i++) {
                System.out.print("Nom usuari (enter per acabar): ");
                String usuari = sc.nextLine();

                if (usuari.isEmpty()) {
                    break; // Si l’usuari prem enter en blanc, sortim
                }

                System.out.print("Contrasenya: ");
                String contrasenya = sc.nextLine();

                fw.write(usuari + ":" + contrasenya + "\n");
            }
            System.out.println("Usuaris registrats correctament.");
        } catch (Exception e) {
            System.out.println("Error escrivint al fitxer: " + e.getMessage());
        }
    }

    // Funció per validar usuari i contrasenya
    public static boolean validarUsuariContrasenya(String fitxer, String usuari, String contrasenya) {
        try {
            List<String> linies = Files.readAllLines(Paths.get(fitxer));

            for (String linia : linies) {
                String[] parts = linia.split(":");
                if (parts.length == 2) {
                    String u = parts[0].trim();
                    String c = parts[1].trim();
                    if (u.equals(usuari) && c.equals(contrasenya)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error llegint el fitxer: " + e.getMessage());
        }
        return false;
    }
}
