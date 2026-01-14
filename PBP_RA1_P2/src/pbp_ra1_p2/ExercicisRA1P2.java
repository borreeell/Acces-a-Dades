import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Scanner;

public class ExercicisRA1P2 {
    public static void exercici1(Scanner sc) {
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

    public static int exercici2(String fitxer, String usuari, String contrasenya) {
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