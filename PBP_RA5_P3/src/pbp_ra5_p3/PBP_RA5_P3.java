/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra5_p3;

import generated.CartaType;
import generated.ClanType;
import generated.ClashType;
import generated.JugadorType;
import java.io.File;
import java.math.BigInteger;
import java.util.List;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author AluCiclesGS1
 */
public class PBP_RA5_P3 {
        static List<JugadorType> jugadors;
        static Scanner sc = new Scanner(System.in);
        
    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(ClashType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClashType clash = (ClashType) unmarshaller.unmarshal(new File("./clash_1.xml"));

            System.out.println("Versio: " + clash.getVersio());
            jugadors = (List<JugadorType>) clash.getJugador();

            int opcio;
            
            do {
                mostrarMenu();
                opcio = Integer.parseInt(sc.nextLine().trim());
                switch (opcio) {
                    case 1:
                        mostrarJugadors();
                        break;
                    case 2:
                        crearJugador();
                        break;
                    default:
                        System.out.println("Opcio invalida");
                }
            } while (opcio != 0);
        } catch(JAXBException ex)  {
            System.out.println("Error JAXB: " + ex.getMessage());
        }
    }
    
    static void mostrarMenu() {
        System.out.println("\n=== MENÚ ===");
        System.out.println("1) Mostrar dades dels jugadors, cartes i clans");
        System.out.println("2) Crear nou jugador");
        System.out.println("3) Crear i afegir carta a un jugador");
        System.out.println("0) Sortir");
        System.out.print("Opció: ");
    }
    
    static void mostrarJugadors() {
        System.out.println("\n--- Jugadors ---");
        for (JugadorType j : jugadors) {
            System.out.println("\nJugador: " + j.getNom() + " | Copes: " + j.getCopes() + " | Nivell: " + j.getNivell());
            
            System.out.println(" Cartes:");
            for (CartaType c : j.getCarta()) {
                System.out.println(" -" + c.getNomcart() + " | Nivell: " + c.getNivellcart() + " | Raresa: " + c.getRaresa());
            }
            
            if (j.getClan() != null) {
                ClanType clan = j.getClan();
                System.out.println(" Clan: " + clan.getNomClan() + " | Copes: " + clan.getCopesClan() + " | Tipus: " + clan.getTipusClan());
            } else {
                System.out.println(" Clan: sense clan");
            }
        }
    }
    
    static void crearJugador() {
        System.out.println("Nom del jugador: ");
        String nom = sc.nextLine().trim();
        
        System.out.println("Copes: ");
        int copes = Integer.parseInt(sc.nextLine().trim());
        
        System.out.println("Nivell (1-14): ");
        int nivell = Integer.parseInt(sc.nextLine().trim());
        
        int nouId = jugadors.stream().mapToInt(j -> j.getId().intValue()).max().orElse(0) + 1;
        
        JugadorType nouJugador = new JugadorType();
        nouJugador.setNom(nom);
        nouJugador.setCopes(BigInteger.valueOf(copes));
        nouJugador.setNivell(nivell);
        nouJugador.setId(BigInteger.valueOf(nouId));
        
        jugadors.add(nouJugador);
    }
    
    static void afegirCartaJugador() {
        System.out.println("Nom del jugador: ");
        String nom = sc.nextLine().trim();
        
        if (!existeixJugador(nom)) {
            System.out.println("El jugador amb nom '" + nom + "' no existeix.");
            return;
        }
        
        JugadorType jugador = trobarJugador(nom);
        
        System.out.println("Nom de la carta: ");
        String nomCarta = sc.nextLine().trim();
        
        System.out.println("Nivell de la carta (1-14): ");
        int nivellCarta = Integer.parseInt(sc.nextLine().trim());
        
        System.out.println("Raresa (comuna/rara/epica/llegendaria/especial/campio/heroi): ");
        String raresa = sc.nextLine().trim();
        
        int nouIdCart = jugador.getCarta().stream().mapToInt(c -> c.getIdCart().intValue()).max().orElse(0) + 1;
        
        CartaType novaCarta = new CartaType();
        novaCarta.setNomcart(nomCarta);
        novaCarta.setNivellcart(nivellCarta);
        novaCarta.setRaresa(raresa);
        novaCarta.setIdCart(BigInteger.valueOf(nouIdCart));
        
        jugador.getCarta().add(novaCarta);
        System.out.println("Carta '" + nomCarta + "' afegida a " + nom + ".");
    }
    
    static boolean existeixJugador(String nom) {
        for (JugadorType j : jugadors) {
            if (j.getNom().equalsIgnoreCase(nom)) return true;
        }
        
        return false;
    }
    
    static JugadorType trobarJugador(String nom) {
        for (JugadorType j : jugadors) {
            if (j.getNom().equalsIgnoreCase(nom)) return j;
        }
        
        return null;
    }
}
