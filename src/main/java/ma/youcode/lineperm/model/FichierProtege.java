package ma.youcode.lineperm.model;

public class FichierProtege {
    private String nom;
    private String proprietaire;

    private boolean propR;
    private boolean propW;
    private boolean propD;

    private boolean otherR;
    private boolean otherW;
    private boolean otherD;

    public FichierProtege(String nom, String proprietaire, boolean propR, boolean propW, boolean propD,
                          boolean otherR, boolean otherW, boolean otherD) {
        this.nom = nom;
        this.proprietaire = proprietaire;
        this.propR = propR;
        this.propW = propW;
        this.propD = propD;
        this.otherR = otherR;
        this.otherW = otherW;
        this.otherD = otherD;
    }

    public FichierProtege(String nom, String proprietaire) {
        this(nom, proprietaire, true, true, true, false, false, false);
    }

    public String getNom() { return nom; }
    public String getProprietaire() { return proprietaire; }

    public boolean isPropR() { return propR; }
    public boolean isPropW() { return propW; }
    public boolean isPropD() { return propD; }

    public boolean isOtherR() { return otherR; }
    public boolean isOtherW() { return otherW; }
    public boolean isOtherD() { return otherD; }

    public void setOtherR(boolean otherR) { this.otherR = otherR; }
    public void setOtherW(boolean otherW) { this.otherW = otherW; }
    public void setOtherD(boolean otherD) { this.otherD = otherD; }

    public String getPermissionsFormat() {
        String prop = (propR ? "r" : "-") + (propW ? "w" : "-") + (propD ? "d" : "-");
        String other = (otherR ? "r" : "-") + (otherW ? "w" : "-") + (otherD ? "d" : "-");
        return prop + "|" + other;
    }
}