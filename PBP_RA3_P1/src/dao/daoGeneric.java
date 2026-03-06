package dao;

import Model.clan;
import java.util.List;
import Model.jugador;
import Model.partida;
import java.util.Arrays;
import net.sf.ehcache.hibernate.HibernateUtil;
import org.hibernate.*;
import util.NewHibernateUtil;


/**
 * @author Pau Borrell
 */
public class daoGeneric {
    
    public static daoGeneric daoGeneric = null;
    
    private SessionFactory factory;
    private Transaction transaction;


    public daoGeneric() {
        factory = NewHibernateUtil.getSessionFactory();
    }
    
    public static daoGeneric getInstance(){
        if(daoGeneric==null){
            daoGeneric=new daoGeneric();
        }
        return daoGeneric;
    }
    
    public boolean create(Object o){
        boolean retorn = false;
        Session sessio = factory.openSession();
        try{
            transaction = sessio.beginTransaction();
            sessio.save(o);
            transaction.commit();
            retorn=true;
            
        }
        catch (Exception e){
            transaction.rollback();
            retorn = false;
            e.printStackTrace();
        } finally{
            sessio.close();
        }
        return retorn;
    }
    
    public boolean deleteJugador(int id) {
       boolean retorn = false;
        Session sessio = factory.openSession();
        try{
            transaction = sessio.beginTransaction();
            
            jugador o = (jugador) sessio.load(jugador.class, id);
       
            //first load() method example
     
            sessio.delete(o);
            transaction.commit();
            retorn=true;
            
        }
        catch (Exception e){
            transaction.rollback();
            retorn = false;
            e.printStackTrace();
        } finally{
            sessio.close();
        }
        return retorn;
    }
    
    public boolean deletePartida(int id) {
        boolean retorn = false;
        Session sessio = factory.openSession();
        
        try {
            transaction = sessio.beginTransaction();
            partida o = (partida) sessio.load(partida.class, id);
            
            sessio.delete(o);
            transaction.commit();
            retorn = true;
        } catch (Exception e) {
            transaction.rollback();
            retorn = false;
            e.printStackTrace();
        } finally {
            sessio.close();
        }
        
        return retorn;
    }
    
    public boolean deleteClan(int id) {
        boolean retorn = false;
        Session sessio = factory.openSession();
        
        try {
            transaction = sessio.beginTransaction();
            clan o = (clan) sessio.load(clan.class, id);
            
            // Esborrar clan de la base de dades:           
            sessio.delete(o);
            transaction.commit();
            retorn = true;
        } catch (Exception e) {
            // Si falla, fer un rollback:
            transaction.rollback();
            retorn = false;
            e.printStackTrace();
        } finally {
            sessio.close();
        }
        
        return retorn;
    }
    
    public List readJugador(){
        List result=null;
        try {
        Session sessio = factory.openSession();
        sessio.beginTransaction();
        Query q = sessio.createQuery("from jugador");
        result = (List) q.list();
        
        //sessio.getTransaction().commit();
    } catch (HibernateException he) {
        he.printStackTrace();
    }
        return result;
    }
    
    public List readPartides() {
        List result = null;
        
        try {
            Session sessio = factory.openSession();
            sessio.beginTransaction();
            Query q = sessio.createQuery("from partida");
            result = (List) q.list();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        
        return result;
    }
    
    public List readClans() {
        List result = null;
        
        try {
            // Obrim una sessio
            Session sessio = factory.openSession();
            sessio.beginTransaction();
            
            // Obtenim tots els clans i els guardem en una llista
            Query q = sessio.createQuery("from clan");
            result = (List) q.list();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        
        return result;
    }
    
    public jugador findJugadorById(int id) {
        Session session = factory.openSession();
        jugador j = (jugador) session.get(jugador.class, id);
        
        session.close();
        return j;
    }
    
    public clan findClanById(int id) {
        Session session = factory.openSession();
        clan c = (clan) session.get(clan.class, id);
        
        session.close();
        return c;
    }
    
    public void updateJugador(jugador j) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        
        session.update(j);
        tx.commit();
        session.close();
    }
    
    public void updateClan(clan c) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        
        // Actualitzem el clan:
        session.update(c);
        tx.commit();
        session.close();
    }
    
    public List<jugador> trobarParellaJugadors() {
        Session session = factory.openSession();
        List<jugador> tots = session.createCriteria(jugador.class).list();
        session.close();
        
        if (tots.size() < 2) return null;
        
        for (int i = 0; i < tots.size(); i++) {
            for (int j = i + 1; j < tots.size(); j++) {
                jugador jug1 = tots.get(i);
                jugador jug2 = tots.get(j);
                
                int diff = Math.abs(jug1.getNivell() - jug2.getNivell());
                
                if (diff <= 2) {
                    return Arrays.asList(jug1, jug2);
                }
            }
        }
        
        return Arrays.asList(tots.get(0), tots.get(1));
    }
}

