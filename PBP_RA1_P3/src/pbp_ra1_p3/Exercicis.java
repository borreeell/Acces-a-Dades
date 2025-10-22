package pbp_ra1_p3;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class Exercicis {
    public static Document obrirFitxerXML(String fitxerXML) throws Exception {
        File fxml = new File(fitxerXML);

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = (Document) dBuilder.parse(fxml);

        doc.getDocumentElement().normalize();

        return doc;
    }

    public static void practica3_1() {
        try {
            // Creem l'objecte File amb el fitxer XML a llegir
            Document doc = obrirFitxerXML("PBP_RA1_P3\\clash.xml");

            // Normalitzem el document (per eliminar espais i nodes buits)
            doc.getDocumentElement().normalize();

            // Mostrem l'element arrel (jugadors)
            //System.out.println("Arrel: " + doc.getDocumentElement().getNodeName());
            System.out.println("------------------------------------");

            // Agafem tots els elements "jugador"
            NodeList llistaJugadors = doc.getElementsByTagName("jugador");

            // Recorrem tots els jugadors
            for (int i = 0; i < llistaJugadors.getLength(); i++) {
                Node node = llistaJugadors.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element jugador = (Element) node;

                    System.out.println("Nom: " + jugador.getElementsByTagName("nom").item(0).getTextContent());
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

                    System.out.println(); // Linia en blanc entre jugadors
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
