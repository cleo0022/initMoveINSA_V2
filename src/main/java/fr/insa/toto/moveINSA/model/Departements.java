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
along with CoursBeuhhhhvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.toto.moveINSA.model;

import fr.insa.beuvron.utils.ConsoleFdB;
import fr.insa.beuvron.utils.exceptions.ExceptionsUtils;
import fr.insa.beuvron.utils.list.ListUtils;
import fr.insa.beuvron.utils.database.ResultSetUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author cleoh
 */
public class Departements {
    private int id ;
    private String nomDep ;
    
    public Departements (String nomDep){
        this (-1, nomDep);
    }
    
    public Departements (int id, String nomDep) {
        this.id =id;
        this.nomDep = nomDep ;
    }
    
     @Override
    public String toString() {
        return "Departements{" + "id =" + this.getId() + " ; nomDep=" + nomDep + '}';
    }
    
    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into Departements (nomDep) values (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setString(1, this.getnomDep());
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }
    
    public static List<Departements> tousLesDep(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nomDep from Departements")) {
            ResultSet rs = pst.executeQuery();
            List<Departements> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Departements(rs.getInt(1), rs.getString(2)));
            }
            return res;
        }
    }
    
    public static int creeConsole(Connection con) throws SQLException {
        String idDep = ConsoleFdB.entreeString("nomDep : ");
        Departements nouveau = new Departements(idDep);
        return nouveau.saveInDB(con);
    }

    public static Departements selectInConsole(Connection con) throws SQLException {
        return ListUtils.selectOne("choisissez un departement :",
                tousLesDep(con), (elem) -> elem.getnomDep());
    }
    
    public int getId() {
        return id;
    }
    
    public String getnomDep(){
        return nomDep;
    }
    
}
