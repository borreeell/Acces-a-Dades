package Model;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "clan")
public class clan implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Basic
    @Column(name = "nom", length = 50, nullable = false)
    private String Nom;
    
    @Basic
    @Column(name = "nivell", nullable = true)
    private int nivell;
    
    @Basic
    @Column(name = "copes", nullable = true)
    private int copes;
    
    @Basic
    @Column(name = "jugadorAdmin", nullable = false)
    private int JugadorAdmin;
    
    @Basic
    @Column(name = "tipus", length = 50, nullable = false)
    private String tipus;
    
    @Basic
    @Column(name = "idClanAssociat", nullable = true)
    private int idClanAssociat;
    
    public clan() { }
    
    public clan(int id, String Nom, int nivell, int copes, int JugadorAdmin, String tipus) {
        this.id = id;
        this.Nom = Nom;
        this.nivell = nivell;
        this.copes = 0;
        this.JugadorAdmin = JugadorAdmin;
        this.tipus = tipus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public int getNivell() {
        return nivell;
    }
    
    public void setNivell(int nivell) {
        this.nivell = nivell;
    }
    
    public int getCopes() {
        return copes;
    }

    public void setCopes(int copes) {
        this.copes = copes;
    }

    public int getJugadorAdmin() {
        return JugadorAdmin;
    }

    public void setJugadorAdmin(int JugadorAdmin) {
        this.JugadorAdmin = JugadorAdmin;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public int getIdClanAssociat() {
        return idClanAssociat;
    }

    public void setIdClanAssociat(int idClanAssociat) {
        this.idClanAssociat = idClanAssociat;
    }   
}
