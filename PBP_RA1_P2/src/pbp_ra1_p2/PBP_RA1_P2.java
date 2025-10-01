/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra1_p2;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author AluCiclesGS1
 */
public class PBP_RA1_P2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcio = 0;

        System.out.println("**********************");
        System.out.println("******* MENÚ *********");
        System.out.println("1. Registrar usuaris");
        System.out.println("2. Log In");
        System.out.println("**********************");

        switch (opcio) {
            case 1:
                escriureUsuariContrasenya();
                break;
            case 2:
                
            default:
                break;
        }
    }

    public static void escriureUsuariContrasenya() {
        try {
            FileWriter fw = new FileWriter("nom usuaris.txt");
            Scanner scNomUsuari = new Scanner(System.in);
            Scanner scContrasenya = new Scanner(System.in);

            for (int i = 0; i == 15; i++) {
                System.out.println("Nom usuari: ");
                String usuari = scNomUsuari.nextLine();

                System.out.println("Contrasenya: ");
                String contrasenya = scContrasenya.nextLine();

                
                if (usuari.isEmpty()) {
                    break;
                }

                fw.write(usuari + ":" + contrasenya + "\n");
            }

            fw.close();
            scNomUsuari.close();
            scContrasenya.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

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
            System.out.println("Error: " + e);
        }
        return false;
    }
}
