package pbp_ra1_p3;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class Exercicis {
    /**
     * Obre i llegeix un fitxer XML
     * @param fitxerXML Ruta del fitxer XML a llegir
     * @return Objecte document representant el fitxer llegit
     * @throws Exception Si hi ha algun error durant l'execucio
     */
    public static Document obrirFitxerXML(String fitxerXML) throws Exception {
        // Crea un objecte file amb la ruta al fitxer
        File fxml = new File(fitxerXML);

        // Crea una nova instancia de DocumentBuilderFactory
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        
        // Crea un nou DocumentBuilder utilitzant el dbFactory creat abans
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        // Llegeix el fitxer XML i crea un objecte Document amb el contingut
        Document doc = (Document) dBuilder.parse(fxml);

        // Normalitza el text del document (si no ho fessim retornaria caracters extranys)
        doc.getDocumentElement().normalize();

        // Retorna el contingut del document normalitzat
        return doc;
    }

    public static void exercici1(Scanner scanner) {
        try {
            // Obrim el fitxer XML amb les dades dels jugadors
            Document doc = obrirFitxerXML("PBP_RA1_P3\\data\\clash.xml");

            // Demanem a l'usuari el nom del jugador que vol cercar
            System.out.print("Introdueix el nom del jugador: ");
            String nomCercat = scanner.nextLine();

            System.out.println("------------------------------------");

            // Obtenim la llista de tots els jugadors del document XML
            NodeList llistaJugadors = doc.getElementsByTagName("jugador");

            // Variable per controlar si hem trobat el jugador
            boolean jugadorTrobat = false;

            // Recorrem tots els jugadors
            for (int i = 0; i < llistaJugadors.getLength(); i++) {
                Node node = llistaJugadors.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element jugador = (Element) node;

                    // Obtenim el nom del jugador actual
                    String nomJugador = jugador.getElementsByTagName("nom").item(0).getTextContent();

                    // Comprovem si coincideix amb el nom cercat
                    if (nomJugador.equals(nomCercat)) {
                        jugadorTrobat = true;

                        System.out.println("Nom: " + nomJugador); 
                        System.out.println("Nivell: " + jugador.getElementsByTagName("nivell").item(0).getTextContent());
                        System.out.println("Copes: " + jugador.getElementsByTagName("copes").item(0).getTextContent());
                        System.out.println("Or: " + jugador.getElementsByTagName("or").item(0).getTextContent());
                        System.out.println("Gems: " + jugador.getElementsByTagName("gems").item(0).getTextContent());
                        System.out.println("Estrelles: " + jugador.getElementsByTagName("estrelles").item(0).getTextContent() + "\n");

                        System.out.println("---- Ultimes 3 partides: ----");

                        // Llegim les partides del jugador
                        NodeList partides = jugador.getElementsByTagName("partida");

                        // Recorrem les partides
                        for (int j = 0; j < partides.getLength(); j++) {
                            Node partidaNode = partides.item(j);

                            if (partidaNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element partida = (Element) partidaNode;

                                // Mostrem les dades de cada partida
                                System.out.println("Data: " + partida.getElementsByTagName("data").item(0).getTextContent());
                                System.out.println("Resultat: " + partida.getElementsByTagName("resultat").item(0).getTextContent());
                                System.out.println("Duracio: " + partida.getElementsByTagName("durada").item(0).getTextContent());
                                System.out.println("Tipus: " + partida.getElementsByTagName("tipus").item(0).getTextContent());
                                System.out.println("---------------------------");
                            }
                        }

                        break; // Sortim del bucle un cop trobat el jugador
                    }
                }
            }

            if (!jugadorTrobat) {
                System.out.println("No s'ha trobat cap jugador amb el nom: " + nomCercat);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public static void exercici2() {
        ArrayList<Jugador> jugadors = new ArrayList<>();

        try {
            Document doc = obrirFitxerXML("PBP_RA1_P3\\data\\clash.xml");

            NodeList nodeList = doc.getElementsByTagName("jugador");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    String nom = e.getElementsByTagName("nom").item(0).getTextContent();
                    int copes = Integer.parseInt(e.getElementsByTagName("copes").item(0).getTextContent());

                    Jugador j = new Jugador(nom, copes);
                    jugadors.add(j);
                } 
            }

            if (jugadors.isEmpty()) {
                System.out.println("No s'han trobat jugadors");
                return;
            }

            Jugador millorJugador = jugadors.get(0);
            for (Jugador j : jugadors) {
                if (j.copes > millorJugador.copes) {
                    millorJugador = j;
                }
            }


            System.out.println("Jugador amb mes copes: ");
            System.out.println("Nom: " + millorJugador.nom);
            System.out.println("Copes: " + millorJugador.copes);
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    public static void exercici3() {
        
    }
}
