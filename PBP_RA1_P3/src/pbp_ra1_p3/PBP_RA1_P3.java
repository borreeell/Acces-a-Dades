package pbp_ra1_p3;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.File;


/**
 *
 * @author Pau Borrell
 */
public class PBP_RA1_P3 {

    public static void main(String[] args) {
        try {
            // Creem l'objecte File amb el fitxer XML a llegir
            File arxiuXML = new File("PBP_RA1_P3\\clash.xml");

            // Creem el parser XML (DocumentBuilder)
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parsejem el fitxer i el carreguem en memoria com a "Document"
            Document doc = dBuilder.parse(arxiuXML);

            // Normalitzem el document (per eliminar espais i nodes buits)
            doc.getDocumentElement().normalize();

            // Mostrem l'element arrel (jugadors)
            System.out.println("Arrel: " + doc.getDocumentElement().getNodeName());
            System.out.println("------------------------------------");

            // Agafem tots els elements "jugador"
            NodeList llistaJugadors = doc.getElementsByTagName("jugador");

            // Recorrem tots els jugadors
            for (int i = 0; i < llistaJugadors.getLength(); i++) {
                Node node = llistaJugadors.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element jugador = (Element) node;

                    System.out.println("Nom: " + getTags(jugador, "nom"));
                    System.out.println("Nivell: " + getTags(jugador, "nivell"));
                    System.out.println("Copes: " + getTags(jugador, "copes"));
                    System.out.println("Or: " + getTags(jugador, "or"));
                    System.out.println("Gems: " + getTags(jugador, "gems"));
                    System.out.println("Estrelles: " + getTags(jugador, "estrelles") + "\n");

                    System.out.println("---- Ultimes 3 partides: ----");

                    // Llegim les partides del jugador
                    NodeList partides = jugador.getElementsByTagName("partida");

                    // Recorrem les partides
                    for (int j = 0; j < partides.getLength(); j++) {
                        Node partidaNode = partides.item(j);

                        if (partidaNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element partida = (Element) partidaNode;

                            // Mostrem les dades de cada partida
                            System.out.println("Data: " + getTags(partida, "data"));
                            System.out.println("Resultat: " + getTags(partida, "resultat"));
                            System.out.println("Duracio: " + getTags(partida, "durada"));
                            System.out.println("Tipus: " + getTags(partida, "tipus"));
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
    

    private static String getTags(Element pare, String tagName) {
        // Obtenim la node list amb totes les etiquetes trobades dins l'element pare amb aquest nom
        NodeList nodes = pare.getElementsByTagName(tagName);

        // Comprovem que s'hagi trobat almenys una etiqueta amb aquest nom
        if (nodes != null && nodes.getLength() > 0) {
            Node node = nodes.item(0); // Agafem el primer element de la llista
            
            // Si el node existeix i te contingut de text, el retornem
            if (node != null && node.getTextContent() != null) {
                return node.getTextContent().trim();
            }
        }

        // Si no es troba l'etiqueta
        return "";
    }
}
