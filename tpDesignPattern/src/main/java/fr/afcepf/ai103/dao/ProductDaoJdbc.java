package fr.afcepf.ai103.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import fr.afcepf.ai103.data.Produit;
import fr.afcepf.ai103.ds.SimpleDataSource;

public class ProductDaoJdbc implements ProductDao {
	
	private Connection etablirConnection() throws SQLException {
     //version temporaire sans factory:
	 DataSource ds = new SimpleDataSource();	
     return ds.getConnection();
	}
	
	public static Long getAutoIncrPk(PreparedStatement pst ) {
		Long pk= null;
		try {
			ResultSet rsKeys = pst.getGeneratedKeys();//r�cup�rer valeur clef primaire 
			if(rsKeys.next()){ pk= rsKeys.getLong(1);
			}
			rsKeys.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pk;
	}
	

	@Override
	public Produit insererNouveauProduit(Produit p) {
		Connection cn = null;
		try {
			cn = etablirConnection();
			/*
			Statement st = cn.createStatement();
			String reqSQL="insert into Produit(label,prix) values('"+p.getLabel()+"'," + p.getPrix()+")";
			System.out.println(reqSQL);
			st.executeUpdate(reqSQL);*/
			PreparedStatement pst = cn.prepareStatement("insert into Produit(label,prix) values(?,?)",
					                                     PreparedStatement.RETURN_GENERATED_KEYS);
			pst.setString(1, p.getLabel()); //valeur de remplacement pour le ? numero 1
			pst.setDouble(2, p.getPrix());  //valeur de remplacement pour le ? numero 2
			pst.executeUpdate();
			p.setNumero(getAutoIncrPk(pst));//mettre � jour la partie numero du produit ( null --> auto_increment pk)
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeCn(cn);
		}
		return p;
	}

	@Override
	public List<Produit> rechercherProduits() {
		List<Produit> listeProduits =  new ArrayList<Produit>();
		Connection cn = null;
		try {
			cn = etablirConnection();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery("select * from Produit");
			while(rs.next()) {
				Produit prod = new Produit();
				prod.setNumero(rs.getLong("numero"));
				prod.setLabel(rs.getString("label"));
				prod.setPrix(rs.getDouble("prix"));
				listeProduits.add(prod);
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeCn(cn);
		}
		return listeProduits;
	}
	
	public static void closeCn(Connection cn) {
		try { cn.close(); } catch  (SQLException e) {	e.printStackTrace(); }
	}

	@Override
	public void mettreAjourProduit(Produit p) {
		Connection cn = null;
		try {
			cn = etablirConnection();
			PreparedStatement pst = cn.prepareStatement("update Produit set label=?,prix=? where numero=?");
			pst.setString(1, p.getLabel()); //valeur de remplacement pour le ? numero 1
			pst.setDouble(2, p.getPrix());  //valeur de remplacement pour le ? numero 2
			pst.setLong(3, p.getNumero());  //valeur de remplacement pour le ? numero 3
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeCn(cn);
		}
	}

	@Override
	public void supprimerProduit(Long numero) {
		Connection cn = null;
		try {
			cn = etablirConnection();
			PreparedStatement pst = cn.prepareStatement("delete from Produit where numero=?");
			pst.setLong(1, numero);  //valeur de remplacement pour le ? numero 1
			pst.executeUpdate();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeCn(cn);
		}
	}

}
