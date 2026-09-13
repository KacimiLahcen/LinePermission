package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;

public class ControleAcces {

    public static boolean estAutorise(String currentLogin, FichierProtege fichier, char droit) {
        if (currentLogin == null || fichier == null) return false;

        boolean isOwner = currentLogin.equals(fichier.getProprietaire());

        switch (droit) {
            case 'r':
                return isOwner ? fichier.isPropR() : fichier.isOtherR();
            case 'w':
                return isOwner ? fichier.isPropW() : fichier.isOtherW();
            case 'd':
                return isOwner ? fichier.isPropD() : fichier.isOtherD();
            default:
                return false;
        }
    }
}