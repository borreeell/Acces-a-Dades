import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Pau Borrell
 */
public class ExercicisP1 {
    public static void exercici2() {
        // Crea un objecte File que rep com a parametre un string amb la ruta de l'arxiu.
        File arxiu = new File("C:\\Windows\\notepad.exe");
        
        // Comprova si l'arxiu existeix
        if (arxiu.exists()) {
            System.out.println("L'arxiu existeix");
        } else {
            System.out.println("L'arxiu no existeix");
        }
    }

    public static void exercici3() {
        File directori = new File("C:\\Windows");
        // Obté la llista de noms d'arxius i directoris dins de C:\Windows
        String[] llista = directori.list();

        // Mostra cada nom d'arxiu/directori per pantalla
        for (String nomArxiu: llista) {
            System.out.println(nomArxiu);
        }
    }

    public static void exercici4() {
        try {
            // Creem els dos arxius
            FileWriter fw = new FileWriter("arxiu.txt");
            FileWriter fwUser = new FileWriter("arxiu-terminal.txt");

            // Llegim el text a escriure al segon arxiu
            System.out.println("Text a escriure a l'arxiu: ");
            Scanner fileText = new Scanner(System.in);
            String text = fileText.nextLine();

            fw.write("Aquest es un primer escrit al meu arxiu"); // Escrivim dins del primer arxiu
            fwUser.write(text);

            fileText.close();
            fw.close();
            fwUser.close();

            System.out.println("Els arxius s'han creat correctament");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exercici5() throws FileNotFoundException, IOException{
        // Creem els objectes FileReader per llegir els diferents arxius
        FileReader txt1 = new FileReader("arxiu.txt");
        FileReader txt2 = new FileReader("arxiu-terminal.txt");
        FileReader arxiuPdf = new FileReader("arxiuPdf.pdf");

        // Creem els BufferedReader's per cada arxiu
        BufferedReader bufTxt1 = new BufferedReader(txt1);
        BufferedReader bufTxt2 = new BufferedReader(txt2);
        BufferedReader bufPdf = new BufferedReader(arxiuPdf);

        // Guardem les linies que hem llegit de cada arxiu
        String liniaTxt1 = bufTxt1.readLine();
        String liniaTxt2 = bufTxt2.readLine();
        String liniaPdf = bufPdf.readLine();

        // Iterem cada linia dels arxius i les imprimi per consola
        while (liniaTxt1 != null) {
            System.out.println("Contingut arxiu1: " + liniaTxt1);
            liniaTxt1 = bufTxt1.readLine();
        }

        while (liniaTxt2 != null) {
            System.out.println("Contingut arxiu2: " + liniaTxt2);
            liniaTxt2 = bufTxt2.readLine();
        }

        while (liniaPdf != null) {
            System.out.println("Contingut pdf: " + liniaPdf);
            liniaPdf = bufPdf.readLine();
        }

        // Tanquem els BufferedReader's
        bufTxt1.close();
        bufTxt2.close();
        bufPdf.close();
    }

    public static void exercici6() {
        // Guardem el generat aleatoriament
        String nomArxiu = generaNomAleatori();

        // Guardem el nom del directori
        String nomDirectori = "directori";

        // Creem el directori
        File directori = new File(nomDirectori);

        // Crea el directori si no existeix
        if (!directori.exists()) {
            if (directori.mkdir()) {
                System.out.println("Directori creat");
            } else {
                System.out.println("No s'ha pogut crear el directori");
            }
        }

        // Creem objecte File per l'arxiu dins del directori
        File arxiu = new File(directori, nomArxiu);

        try {
            arxiu.createNewFile();
            System.out.println("S'ha creat l'arxiu");
        } catch(IOException e) {
            System.out.println("Error creant l'arxiu: " + e);
        }
    }

    public static void exercici7() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Entra l'adreça d'un fitxer: ");
        String adrecaFitxer = sc.next();
        File f = new File(adrecaFitxer);

        if (f.isFile()) {
            System.out.println("Nom: " + f.getName());
            System.out.println("Path: " + f.getPath());
            System.out.println("Longitud: " + f.length() + " bytes");
        } else {
            System.out.println("El fitxer no existeix\n");
        }

        sc.close();
    }

    public static String generaNomAleatori() {
        // Caracters permesos en el nom
        String caracters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz";
        Random random = new Random();

        StringBuilder nom = new StringBuilder();

        // Genera el nom de 7 caracters
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(caracters.length());
            nom.append(caracters.charAt(index));
        }

        return nom.toString();
    }
}