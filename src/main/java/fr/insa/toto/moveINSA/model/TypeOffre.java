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
public class TypeOffre {
    private int id;
    private String Type; //erasmus, DDiplome, ect

    public TypeOffre(String Type) {
        this(-1, Type);
    }

    public TypeOffre(int id, String Type) {
        this.id = id;
        this.Type = Type;
    }

    public static List<TypeOffre> toutesLesTypeOffre(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,Type from TypeOffre")) {
            ResultSet rs = pst.executeQuery();
            List<TypeOffre> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new TypeOffre(rs.getInt(1), rs.getString(2)));
            }
            return res;
        }
    }

    @Override
    public String toString() {
        return "TypeOffre{" + "id =" + this.getId() + " ; Type=" + Type + '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into TypeOffre values (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
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
