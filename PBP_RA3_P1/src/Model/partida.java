package Model;

public class partida {
   private int id;
   private int idJug1;
   private int idJug2;
   private String nomJug1;
   private String nomJug2;
   private String resultat;
   private String temps;
   private String tipus;

    public partida() {}
    
    public partida(int id, int idJug1, int idJug2, String nomJug1, String nomJug2, String resultat, String temps, String tipus) {
        this.id = id;
        this.idJug1 = idJug1;
        this.idJug2 = idJug2;
        this.nomJug1 = nomJug1;
        this.nomJug2 = nomJug2;
        this.resultat = resultat;
        this.temps = temps;
        this.tipus = tipus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdJug1() {
        return idJug1;
    }

    public void setIdJug1(int idJug1) {
        this.idJug1 = idJug1;
    }

    public int getIdJug2() {
        return idJug2;
    }

    public void setIdJug2(int idJug2) {
        this.idJug2 = idJug2;
    }

    public String getNomJug1() {
        return nomJug1;
    }

    public void setNomJug1(String nomJug1) {
        this.nomJug1 = nomJug1;
    }

    public String getNomJug2() {
        return nomJug2;
    }

    public void setNomJug2(String nomJug2) {
        this.nomJug2 = nomJug2;
    }

    public String getResultat() {
        return resultat;
    }

    public void setResultat(String resultat) {
        this.resultat = resultat;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }  
}
