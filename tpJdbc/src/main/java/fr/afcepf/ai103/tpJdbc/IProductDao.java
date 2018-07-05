package fr.afcepf.ai103.tpJdbc;

import java.util.List;

/* DAO  = Data Access Object 
 * = objet de traitement sp�cialis� dans l'acc�s aux donn�es
 * avec m�thodes CRUD : Create , Recherche , Update , Delete
 */
public interface IProductDao {
    public Produit insererNouveauProduit(Produit p);
    public List<Produit> rechercherProduits();
    //... autres recherches ...
    public void mettreAjourProduit(Produit p);
    public void supprimerProduit(Long numero);
}
