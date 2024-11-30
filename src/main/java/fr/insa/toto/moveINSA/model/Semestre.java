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

import fr.insa.beuvron.utils.list.ListUtils;

/**
 * @author cleoh
 */
public class Semestre {
    private int id;
    private int numero;

    public Semestre(int numero) {
        this(-1, numero);
    }

    public Semestre(int id, int numero) {
        this.id = id;
        this.numero = numero;
    }

    public static List<Semestre> tousLesSemestre(Connection con) throws SQLException {
        try (PreparedStatement pst = con.prepareStatement(
                "select id,numero from Semestre")) {
            ResultSet rs = pst.executeQuery();
            List<Semestre> res = new ArrayList<>();
            while (rs.next()) {
                res.add(new Semestre(rs.getInt(1), rs.getInt(2)));
            }
            return res;
        }
    }

    @Override
    public String toString() {
        return "Semestre{" + "id =" + this.getId() + " ; numero=" + numero + '}';
    }

    public int saveInDB(Connection con) throws SQLException {
        if (this.getId() != -1) {
            throw new EntiteDejaSauvegardee();
        }
        try (PreparedStatement insert = con.prepareStatement(
                "insert into Semestre values (?)",
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
