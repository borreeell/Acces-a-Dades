package Model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "jugador")
public class jugador implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Basic
    @Column(name = "nom", length = 50, nullable = true)
    private String Nom;
    
    @Basic
    @Column(name = "copes", nullable = true)
    private int copes;
    
    @Basic
    @Column(name = "gemes", nullable = true)
    private int gemes;
    
    @Basic
    @Column(name = "nivell", nullable = true)
    private int nivell;
    
    @Basic 
    @Column(name = "oro", nullable = true)
    private int oro;
    
    public jugador() {}

    public jugador(String Nom, int id) {
        this.Nom = Nom;
        this.id = id;
        this.copes = 0;
        this.nivell = 1;
        this.oro = 100;
        this.gemes = 10;
    }
    
    public jugador(String Nom, int id, int copes, int gemes, int nivell, int oro) {
        this.Nom = Nom;
        this.id = id;
        this.copes = copes;
        this.gemes = gemes;
        this.nivell = nivell;
        this.oro = oro;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCopes() {
        return copes;
    }

    public void setCopes(int copes) {
        this.copes = copes;
    }

    public int getGemes() {
        return gemes;
    }

    public void setGemes(int gemes) {
        this.gemes = gemes;
    }

    public int getNivell() {
        return nivell;
    }

    public void setNivell(int nivell) {
        if (nivell > 14) {
            System.out.println("El nivell no pot ser superior a 14");
        } else {        
            this.nivell = nivell;
        }
    }

    public int getOro() {
        return oro;
    }

    public void setOro(int oro) {
        this.oro = oro;
    }  
}
