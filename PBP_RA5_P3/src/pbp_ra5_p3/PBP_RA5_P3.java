/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pbp_ra5_p3;

import generated.CartaType;
import generated.ClashType;
import generated.JugadorType;
import java.io.File;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author AluCiclesGS1
 */
public class PBP_RA5_P3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            JAXBContext context = JAXBContext.newInstance(ClashType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClashType clash = (ClashType) unmarshaller.unmarshal(new File("./clash_1.xml"));

            System.out.println("Versio: " + clash.getVersio());

            ArrayList<JugadorType> jugadors = (ArrayList<JugadorType>) clash.getJugador();

            for (JugadorType j : jugadors) {
                System.out.println(j.getNom() + " " + j.getCopes() + " " + j.getNivell());
                ArrayList<CartaType> cartes = (ArrayList<CartaType>) j.getCarta();
                for (CartaType c : cartes) {
                    System.out.println(c.getNomcart() + " " + c.getNivellcart() + " " + c.getRaresa());

                }
            }
        } catch(JAXBException ex)  {
           
        }
    }
    
}
