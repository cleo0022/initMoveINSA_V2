/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.list.ListUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author cleoh
 */
public class Etudiants {
    
    private int id ;
    private String nom ;
    private int affecte ;
    private int appartient_classe;
    private String loginEtu;
    private String mdpEtu;
    /**
    * @param nom
    */ 
    public Etudiants (String nom, int affecte, int appartient_classe, String loginEtu, String mdpEtu) {
        this(-1,nom, affecte, appartient_classe, loginEtu, mdpEtu);
    }
    
    public Etudiants (int id, String nom, int affecte, int appartient_classe, String loginEtu, String mdpEtu){
        this.id = id;
        this.nom = nom ;
        this.affecte = affecte ;
        this.appartient_classe = appartient_classe ;
        this.loginEtu=loginEtu;
        this.mdpEtu=mdpEtu;
    }
    @Override
    public String toString() {
        return "Etudiants{" + "id =" + this.getId() + " ; nom=" + nom + "; affecte=" +affecte +
                " ;appartient_classe=" +appartient_classe+ "; loginEtu=" + loginEtu+
                ";mdpEtu=" +mdpEtu+'}';
       
    }
    
    
    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into Etudiants (affecte,appartient_classe) values (?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, this.affecte);
            insert.setInt(2, this.appartient_classe);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }
    
    public static List<Etudiants> tousLesEtudiants(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nom,affecte,appartient_classe, loginEtu, mdpEtu from Etudiants")) {
            ResultSet rs = pst.executeQuery();
            List<Etudiants> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Etudiants(rs.getInt(1), rs.getString(2), 
                        rs.getInt(3), rs.getInt(4),rs.getString(5),rs.getString(6)));
            }
            return res;
        }
    }
    
    public int getId() {
        return id;
    }
    
}
