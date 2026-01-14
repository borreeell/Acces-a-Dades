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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        // Crea un nou element amb l'etiqueta especificada
        Element e = doc.createElement(etiqueta);

        // Assigna el valor de text al element
        e.setTextContent(valor);

        // Retorna l'element creat
        return e;
    }

    private static void afegirPartida(Element jugador, Document doc, String data, String resultat, String durada, String tipus) {
        Element partides;

        // Busca si ja existeix l'element <partides> dins del jugador
        NodeList llistaPartides = jugador.getElementsByTagName("partides");

        // Si no existeix l'element <partides>, el crea
        if (llistaPartides.getLength() == 0) {
            partides = doc.createElement("partides");
            jugador.appendChild(partides);
        } else {
            // Si ja existeix, l'obte
            partides = (Element) llistaPartides.item(0);
        }

        // Crea un nou element <partida>
        Element partida = doc.createElement("partida");

        // Afegeix els elements fills amb les dades de la partida
        partida.appendChild(createElement(doc,"data", data));
        partida.appendChild(createElement(doc,"resultat", resultat));
        partida.appendChild(createElement(doc,"durada", durada));
        partida.appendChild(createElement(doc,"tipus", tipus));

        // Afegeix la partida creada a l'element <partides>
        partides.appendChild(partida);
    }

    private static void sumarCopes(Element jugador, int copes) {
        // Obte l'element <copes> del jugador
        Element nodeCopes = (Element) jugador.getElementsByTagName("copes").item(0);

        // Llegeix el valor actual de copes i el converteix a enter
        int valor = Integer.parseInt(nodeCopes.getTextContent());

        // Suma les noves copes i actualitza el valor
        nodeCopes.setTextContent(String.valueOf(valor + copes));
    }

    private static void guardarXML(Document doc, String ruta) throws TransformerConfigurationException, TransformerException {
        // Crea un transformer per convertir el document en fitxer XML
        Transformer tFormer = TransformerFactory.newInstance().newTransformer();

        // Configura la sortida per tenir indentacio (format llegible)
        tFormer.setOutputProperty(OutputKeys.INDENT, "yes");

        // Defineix la font de dades (el document DOM)
        Source source = new DOMSource(doc);

        // Defineix el destí de la sortida (el fitxer)
        Result result = new StreamResult(new File(ruta));

        // Realitza la transformacio i guarda el fitxer
        tFormer.transform(source, result);
    }

    public static void exercici2(Scanner teclat) {
        try {
            // Obrim el fitxer XML amb les dades dels jugadors
            Document doc = obrirFitxerXML("PBP_RA1_P3\\clash.xml");

            // Demanem a l'usuari el nom del jugador que vol cercar
            System.out.print("Introdueix el nom del jugador: ");
            String nomCercat = teclat.nextLine();

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

                        // Mostrem les dades principals del jugador
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
                    }
                }
            }

            // Si no s'ha trobat cap jugador, mostrem un missatge
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
            Document doc = obrirFitxerXML("PBP_RA1_P3\\clash.xml");

            // Obtenim tots els elements amb l'etiqueta "jugador"
            NodeList nodeList = doc.getElementsByTagName("jugador");

            // Recorrem tots els nodes de jugadors trobats
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                // Comprovem que el node sigui un element (no text o comentari)
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    // Extraiem el nom del jugador
                    String nom = e.getElementsByTagName("nom").item(0).getTextContent();

                    // Extraiem les copes del jugador i les convertim a enter
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

            // Inicialitzem el millor jugador com el primer de la llista
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
                    "PBP_RA1_P3\\meteo2015.xml",
                    "PBP_RA1_P3\\meteo2016.xml",
                    "PBP_RA1_P3\\meteo2017.xml"
            };

            // Recorrem tots els fitxers meteorologics
            for (String fitxer : fitxers) {
                // Obrim el fitxer XML
                Document doc = obrirFitxerXML(fitxer);

                // Obtenim una llista de tots els elements <element>
                NodeList llista = doc.getElementsByTagName("element");

                // Variables per guardar la temperatura maxima i minima globals
                double tmaxGlobal = Double.NEGATIVE_INFINITY;
                double tminGlobal = Double.POSITIVE_INFINITY;

                // Variables per guardar les dates i hores de les temperatures extremes
                String dataTmax = "", horaTmax = "";
                String dataTmin = "", horaTmin = "";

                // Recorrem tots els elements del fitxer
                for (int i = 0; i < llista.getLength(); i++) {
                    Node node = llista.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element e = (Element) node;

                        // Extraiem les dades de cada element de forma segura
                        String data = getTextSafe(e, "fecha");
                        String horatmax = getTextSafe(e, "horatmax");
                        String horatmin = getTextSafe(e, "horatmin");
                        String tmaxStr = getTextSafe(e, "tmax");
                        String tminStr = getTextSafe(e, "tmin");

                        // Si falten dades, mostrem un missatge i saltem aquesta entrada
                        if (tmaxStr.isEmpty() || tminStr.isEmpty() || data.isEmpty()) {
                            System.out.println("Dada incompleta el: " + data + " - s'omet");
                            continue;
                        }

                        // Convertim les temperatures de String a double (substituint comes per punts)
                        double tmax = Double.parseDouble(tmaxStr.replace(",", "."));
                        double tmin = Double.parseDouble(tminStr.replace(",", "."));

                        // Actualitzem la temperatura maxima si trobem una de mes alta
                        if (tmax > tmaxGlobal) {
                            tmaxGlobal = tmax;
                            dataTmax = data;
                            horaTmax = horatmax;
                        }

                        // Actualitzem la temperatura minima si trobem una de mes baixa
                        if (tmin < tminGlobal) {
                            tminGlobal = tmin;
                            dataTmin = data;
                            horaTmin = horatmin;
                        }
                    }
                }

                // Mostrem els resultats per cada fitxer processat
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
            // Obrim el fitxer XML amb les dades dels jugadors
            Document doc = obrirFitxerXML("PBP_RA1_P3\\clash.xml");

            // Obtenim l'element arrel del document
            Node elementRoot = doc.getDocumentElement();

            // Demanem a l'usuari el nom del nou jugador
            System.out.println("Entra el nom del jugador: ");
            String nomIn = teclat.nextLine();

            // Creem un nou element <jugador>
            Element jugador = doc.createElement("jugador");

            // Afegim els elements fills amb els valors inicials del jugador
            jugador.appendChild(createElement(doc, "nom", nomIn));
            jugador.appendChild(createElement(doc, "nivell", "1"));
            jugador.appendChild(createElement(doc, "copes", "0"));
            jugador.appendChild(createElement(doc, "or", "0"));
            jugador.appendChild(createElement(doc, "gems", "0"));
            jugador.appendChild(createElement(doc, "estrelles", "0"));

            // Afegim l'element <jugador> dins de l'element arrel del document
            elementRoot.appendChild(jugador);

            // Guardem els canvis al fitxer XML
            guardarXML(doc, "clash.xml");

            System.out.println("Jugador afegit correctament!");
        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    public static void exercici6(Scanner teclat) {
        try {
            // Obrim el fitxer XML amb les dades dels jugadors
            Document doc = obrirFitxerXML("PBP_RA1_P3\\clash.xml");

            // Obtenim la llista de tots els jugadors
            NodeList jugadors = doc.getElementsByTagName("jugador");

            // Demanem a l'usuari quantes partides vol simular
            System.out.println("Quantes partides vols simular? ");
            int numPartides = teclat.nextInt();
            teclat.nextLine();

            // Creem un objecte Random per generar valors aleatoris
            Random rand = new Random();

            // Simulem el nombre de partides especificat
            for (int partidaNum = 1; partidaNum <= numPartides; partidaNum++) {
                // Seleccionem un primer jugador aleatori
                int idx1 = rand.nextInt(jugadors.getLength());
                int idx2;

                // Seleccionem un segon jugador aleatori diferent del primer
                do {
                    idx2 = rand.nextInt(jugadors.getLength());
                } while (idx2 == idx1);

                // Obtenim els elements dels dos jugadors seleccionats
                Element jugador1 = (Element) jugadors.item(idx1);
                Element jugador2 = (Element) jugadors.item(idx2);

                // Extraiem els noms dels jugadors
                String nom1 = jugador1.getElementsByTagName("nom").item(0).getTextContent();
                String nom2 = jugador2.getElementsByTagName("nom").item(0).getTextContent();

                // Generem un resultat aleatori (torres destruides: 0-3)
                int torres1 = rand.nextInt(4);
                int torres2 = rand.nextInt(4);

                // Formem la cadena del resultat
                String resultat = torres1 + "-" + torres2;

                // Generem la data actual i una durada aleatoria per la partida
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String data = LocalDateTime.now().format(dtf);
                String durada = (2 + rand.nextInt(5)) + ":" + String.format("%02d", rand.nextInt(60));

                // Afegim la partida al primer jugador
                afegirPartida(jugador1, doc, data, resultat, durada, "Lliga");

                // Afegim la partida al segon jugador amb el resultat invertit
                String resultat2 = torres2 + "-" + torres1;
                afegirPartida(jugador2, doc, data, resultat2, durada, "Lliga");

                // Determinem el guanyador i actualitzem les copes
                Element jugadorGuanyador = null;
                String nomGuanyador = "Empat";

                if (torres1 > torres2) {
                    // Jugador1 guanya: obte 3 copes
                    jugadorGuanyador = jugador1;
                    nomGuanyador = nom1;
                    sumarCopes(jugador1, 3);
                } else if (torres2 > torres1) {
                    // Jugador2 guanya: obte 3 copes
                    jugadorGuanyador = jugador2;
                    nomGuanyador = nom2;
                    sumarCopes(jugador2, 3);
                } else {
                    // Empat: tots dos obtenen 1 copa
                    sumarCopes(jugador1, 1);
                    sumarCopes(jugador2, 1);
                }

                // Obtenim les noves copes del guanyador si n'hi ha
                String novesCopes = "";
                if (jugadorGuanyador != null) {
                    novesCopes = jugadorGuanyador.getElementsByTagName("copes").item(0).getTextContent();
                }

                // Mostrem un resum de la partida simulada
                System.out.println("\n=== Simulació de partida ===");
                System.out.println("Jugador1: " + nom1);
                System.out.println("Jugador2: " + nom2);
                System.out.println("Resultat: " + resultat);
                System.out.println("Guanyador: " + nomGuanyador);
                System.out.println("Noves copes de guanyador: " + novesCopes);

                System.out.println("Partida " + partidaNum + ": " + nom1 + " vs " + nom2 + " -> " + resultat);
            }

            // Guardem tots els canvis al fitxer XML
            guardarXML(doc, "clash.xml");
            System.out.println("S'han simulat " + numPartides + " partides i s'ha actualitzat el fitxer XML.");

        } catch (Exception e) {
            System.out.println("Hi ha hagut un error: " + e);
        }
    }

    private static String getTextSafe(Element e, String tag) {
        // Busca l'element amb l'etiqueta especificada
        NodeList list = e.getElementsByTagName(tag);

        // Si no existeix o esta buit, retorna una cadena buida
        if (list == null || list.getLength() == 0 || list.item(0) == null) return "";

        // Retorna el contingut de text, eliminant espais i substituint comes per punts
        return list.item(0).getTextContent().trim().replace(",", ".");
    }
}