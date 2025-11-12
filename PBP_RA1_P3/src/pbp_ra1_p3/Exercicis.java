package pbp_ra1_p3;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class Exercicis {
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

    private static Element createElement(Document doc, String etiqueta, String valor) {
        Element e = doc.createElement(etiqueta);
        e.setTextContent(valor);
        return e;
    }

    private static void afegirPartida(Element jugador, Document doc, String data, String resultat, String durada, String tipus) {
        Element partides;
        NodeList llistaPartides = jugador.getElementsByTagName("partides");

        if (llistaPartides.getLength() == 0) {
            partides = doc.createElement("partides");
            jugador.appendChild(partides);
        } else {
            partides = (Element) llistaPartides.item(0);
        }

        Element partida = doc.createElement("partida");
        partida.appendChild(createElement(doc,"data", data));
        partida.appendChild(createElement(doc, "resultat", resultat));
        partida.appendChild(createElement(doc, "durada", durada));
        partida.appendChild(createElement(doc, "tipus", tipus));

        partides.appendChild(partida);
    }

    private static void sumarCopes(Element jugador, int copes) {
        Element nodeCopes = (Element) jugador.getElementsByTagName("copes").item(0);
        int valor = Integer.parseInt(nodeCopes.getTextContent());
        nodeCopes.setTextContent(String.valueOf(valor + copes));
    }

    private static void guardarXML(Document doc, String ruta) throws TransformerConfigurationException, TransformerException {
        Transformer tFormer = TransformerFactory.newInstance().newTransformer();
        
        
        tFormer.setOutputProperty(OutputKeys.INDENT, "yes");

        Source source = new DOMSource(doc);
        Result result = new StreamResult(new File(ruta));

        tFormer.transform(source, result);
    } 

    public static void exercici2(Scanner scanner) {
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

    public static void exercici3() {
        // Creem una llista per emmagatzemar els jugadors
        ArrayList<Jugador> jugadors = new ArrayList<>();

        try {
            // Obrim i parsejem el fitxer XML que conte les dades dels jugadors
            Document doc = obrirFitxerXML("PBP_RA1_P3\\data\\clash.xml");

            // Obtenim tots els elements amb l'etiqueta "jugador"
            NodeList nodeList = doc.getElementsByTagName("jugador");

            // Recorrem tots els nodes de jugadors trobats
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // Comprovem que el node sigui un element (no text o comentari)
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    String nom = e.getElementsByTagName("nom").item(0).getTextContent();
                    int copes = Integer.parseInt(e.getElementsByTagName("copes").item(0).getTextContent());

                    // Creem un objecte jugador amb les dades extretes
                    Jugador j = new Jugador(nom, copes);

                    // Afegim el jugador a la llista
                    jugadors.add(j);
                } 
            }

            // Comprovem si s'han trobat jugadors al fitxer
            if (jugadors.isEmpty()) {
                System.out.println("No s'han trobat jugadors");
                return;
            }

            // Inicialitzem el jugador com el primer de la llista
            Jugador millorJugador = jugadors.get(0);

            // Recorrem tots els jugadors per trobar el que te mes copes
            for (Jugador j : jugadors) {
                if (j.copes > millorJugador.copes) {
                    millorJugador = j;
                }
            }

            // Mostrem per pantalla les dades del jugador amb mes copes
            System.out.println("Jugador amb mes copes: ");
            System.out.println("Nom: " + millorJugador.nom);
            System.out.println("Copes: " + millorJugador.copes);
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    public static void exercici4() {
        try {
            // Rutes dels fitxers amb les dades meteorologiques
            String[] fitxers = {
                "PBP_RA1_P3\\data\\meteo2015.xml",
                "PBP_RA1_P3\\data\\meteo2016.xml",
                "PBP_RA1_P3\\data\\meteo2017.xml"          
            };

            // Recorrem tots els fitxers meteorologics
            for (String fitxer : fitxers) {
                // Obrim el fitxer
                Document doc = obrirFitxerXML(fitxer);

                // Obtenim una llista de tots els elements <element>
                NodeList llista = doc.getElementsByTagName("element");

                // Variables per guardar les dades dels fitxers
                double tmaxGlobal = Double.NEGATIVE_INFINITY;
                double tminGlobal = Double.POSITIVE_INFINITY;
                String dataTmax = "", horaTmax = "";
                String dataTmin = "", horaTmin = "";

                // Recorrem tots els elements del fitxer
                for (int i = 0; i < llista.getLength(); i++) {
                    Node node = llista.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

                        String data = getTextSafe(e, "fecha");
                        String horatmax = getTextSafe(e, "horatmax");
                        String horatmin = getTextSafe(e, "horatmin");
                        String tmaxStr = getTextSafe(e, "tmax");
                        String tminStr = getTextSafe(e, "tmin");

                        if (tmaxStr.isEmpty() || tminStr.isEmpty() || data.isEmpty()) {
                            System.out.println("Dada incompleta el: " + data + " - s'omet");
                            continue;
                        }

                        // Els valors poden tenir comes, aixi que els substituim per punts abans de parsejar
                        double tmax = Double.parseDouble(tmaxStr.replace(",", "."));

                        double tmin = Double.parseDouble(tminStr.replace(",", "."));


                        // Actualitzem maxim i minim si cal
                        if (tmax > tmaxGlobal) {
                            tmaxGlobal = tmax;
                            dataTmax = data;
                            horaTmax = horatmax;
                        }

                        if (tmin < tminGlobal) {
                            tminGlobal = tmin;
                            dataTmin = data;
                            horaTmin = horatmin;
                        }
                    }
                }
                
                // Mostrem els resultats
                System.out.println("./" + new File(fitxer).getName());
                System.out.println("Tmax [" + dataTmax + " " + horaTmax + "] = " + tmaxGlobal);
                System.out.println("Tmin [" + dataTmin + " " + horaTmin + "] = " + tminGlobal);
                System.out.println("\n");
            }

        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }

    }

    public static void exercici5(Scanner teclat) {
        try {
            Document doc = obrirFitxerXML("PBP_RA1_P3\\data\\clash.xml");
            Node elementRoot = doc.getDocumentElement();

            // Demana a l'usuari un nom de jugador
            System.out.println("Entra el nom del jugador: ");
            String nomIn = teclat.nextLine();

            // Crea l'element jugador
            Element jugador = doc.createElement("jugador");

            // Afegeix els elements creats dins de jugador
            jugador.appendChild(createElement(doc, "nom", nomIn));
            jugador.appendChild(createElement(doc, "nivell", "1"));
            jugador.appendChild(createElement(doc, "copes", "0"));
            jugador.appendChild(createElement(doc, "or", "0"));
            jugador.appendChild(createElement(doc, "gems", "0"));
            jugador.appendChild(createElement(doc, "estrelles", "0"));

            // Afegeix l'element <jugador> dins del element pare del fitxer
            elementRoot.appendChild(jugador);

            // Guarda el fitxer XML
            guardarXML(doc, "PBP_RA1_P3\\data\\clash.xml");
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    public static void exercici6(Scanner teclat) {
        try {
            Document doc = obrirFitxerXML("PBP_RA1_P3\\data\\clash.xml");

            NodeList jugadors = doc.getElementsByTagName("jugador");

            System.out.println("Quantes partides vols simular? ");
            int numPartides = teclat.nextInt();
            teclat.nextLine();

            Random rand = new Random();

            for (int partidaNum = 1; partidaNum <= numPartides; partidaNum++) {
                // Selecciona dos jugador aleatoris
                int idx1 = rand.nextInt(jugadors.getLength());
                int idx2;

                do {
                    idx2 = rand.nextInt(jugadors.getLength());
                } while (idx2 == idx1);

                Element jugador1 = (Element) jugadors.item(idx1);
                Element jugador2 = (Element) jugadors.item(idx2);

                String nom1 = jugador1.getElementsByTagName("nom").item(0).getTextContent();
                String nom2 = jugador2.getElementsByTagName("nom").item(0).getTextContent();

                // Genera el resultat aleatori
                int torres1 = rand.nextInt(4);
                int torres2 = rand.nextInt(4);

                String resultat = torres1 + "-" + torres2;

                // Crea la partida per tots dos jugadors
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String data = LocalDateTime.now().format(dtf);
                String durada = (2 + rand.nextInt(5)) + ":" + String.format("%02d", rand.nextInt(60));

                // Afegir partida a jugador1
                afegirPartida(jugador1, doc, data, resultat, durada, "Lliga");

                // Afegir partida a jugador2 amb resultat invertit
                String resultat2 = torres2 + "-" + torres1;
                afegirPartida(jugador2, doc, data, resultat2, durada, "Lliga");

                // Actualitza copes del guanyador
                if (torres1 > torres2) {
                    sumarCopes(jugador1, 3);
                } else if (torres2 > torres1) {
                    sumarCopes(jugador2, 3);
                }

                // Suma 1 copa a tots dos en cas d'empat
                if (torres1 == torres2) {
                    sumarCopes(jugador1, 1);
                    sumarCopes(jugador2, 1);
                }

                System.out.println("Partida " + partidaNum + ": " + nom1 + " vs " + nom2 + "->" + resultat);
            }

            guardarXML(doc, "PBP_RA1_P3\\data\\clash.xml");
            System.out.println("S'han simulat " + numPartides + " partides i s'ha actualitzat el fitxer XML.");
            
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    private static String getTextSafe(Element e, String tag) {
        NodeList list = e.getElementsByTagName(tag);

        if (list == null || list.getLength() == 0 || list.item(0) == null) return "";

        return list.item(0).getTextContent().trim().replace(",", ".");
    }
}
