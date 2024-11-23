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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author cleoh
 */
public class Personnel {
    private int id;
    private String nomPers;
    private String loginPers;
    private String mdpPers;
    
    public Personnel(String nomPers, String loginPers, String mdpPers){
            this(-1,nomPers, loginPers, mdpPers);
    }
    
    public Personnel (int id,String nomPers, String loginPers, String mdpPers){
        this.id=id;
        this.nomPers=nomPers;
        this.loginPers=loginPers;
        this.mdpPers=mdpPers;
    }
    
    @Override
    public String toString() {
        return "Personnel{" + "id =" + this.getId() + " ; nomPers=" + nomPers  +
                " ;loginPers=" +loginPers+";mdpPers=" + mdpPers +'}'; 
    }
    
    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into Personnel values (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }
    public static List<Personnel> tousLePersonnel(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id, nomPers, loginPers, mdpPers from Personnel")) {
            ResultSet rs = pst.executeQuery();
            List<Personnel> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Personnel(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getString(4)));
            }
               return res;
        }
    }
    
    public static Optional<Personnel> cherchePesonnel(Connection con, String ref) throws SQLException {
        try (PreparedStatement pers = con.prepareStatement(
                "select id,nomPers,loginPers,mdpPers from nomPers where nomPers = ?")) {
            pers.setString(1,ref); // le premier est le 1 pas le 0
            ResultSet rs = pers.executeQuery();
            if (rs.next()) {
                return Optional.of(new Personnel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
            else {
                return Optional.empty();
            }
        }
    }
    
    public int getId(){
        return id;
    }
}
