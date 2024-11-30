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
public class classe { //plutot le numero de l'année souhaité
    private int id;
    private int annee;
    private int appartient_spe;

    public classe(int annee, int appartient_spe) {
        this(-1, annee, appartient_spe);
    }

    public classe(int id, int annee, int appartient_spe) {
        this.id = id;
        this.annee = annee;
        this.appartient_spe = appartient_spe;
    }

    public static List<classe> toutesLesClasse(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,annee, appartient_spe from classe")) {
            ResultSet rs = pst.executeQuery();
            List<classe> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new classe(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
            }
            return res;
        }
    }

    @Override
    public String toString() {
        return "classe{" + "id =" + this.getId() + " ; annee=" + annee +
                " ;appartient_spe=" + appartient_spe + '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into classe (appartient_spe) values (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            insert.setInt(1, this.appartient_spe);
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
