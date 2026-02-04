package dao;

import java.util.List;
import Model.jugador;
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
    public boolean delete(int id) {
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
}

