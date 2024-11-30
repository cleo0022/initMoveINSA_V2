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

/**
 * @author cleoh
 */
public class Specialite1 {
    private int id;
    private String nom;
    private int appartient_dep;

    public Specialite1(String nom, int appartient_dep) {
        this(-1, nom, appartient_dep);
    }

    public Specialite1(int id, String nom, int appartient_dep) {
        this.id = id;
        this.nom = nom;
        this.appartient_dep = appartient_dep;
    }

    public static List<Specialite1> toutesLesSpe(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,nom, appartient_dep from Specialite1")) {
            ResultSet rs = pst.executeQuery();
            List<Specialite1> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Specialite1(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            return res;
        }
    }

    @Override
    public String toString() {
        return "Specialite1{" + "id =" + this.getId() + " ; nom=" + nom + " ;appartient_dep=" + appartient_dep + '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into Specialite1 (appartient_dep) values (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, this.appartient_dep);
            insert.executeUpdate();
            try (ResultSet rid = insert.getGeneratedKeys()) {
                rid.next();
                this.id = rid.getInt(1);
                return this.getId();
            }
        }
    }

    public int getId() {
        return id;
    }
}
