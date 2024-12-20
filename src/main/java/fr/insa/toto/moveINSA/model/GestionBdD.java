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
import java.util.List;

import org.h2.jdbc.meta.DatabaseMetaServer;

/**
 * Opération générales sur la base de donnée de gestion des tournois.
 * <p>
 * Les opérations plus spécifiques aux diverses tables sont réparties dans les
 * classes correspondantes.
 * </p>
 *
 * @author francois
 */
public class GestionBdD {

    /**
     * création complète du schéma de la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void creeSchema(Connection con)
            throws SQLException {
        con.setAutoCommit(false);
        try (Statement st = con.createStatement()) {
            // creation des tables
            st.executeUpdate(
                    "create table Partenaire ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " refPartenaire varchar(50) not null unique\n"
                            // + "localisation varchar(50) not null"
                            + ")");
            st.executeUpdate(
                    "create table OffreMobilite ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " nbrplaces int not null,\n"
                            + " specialiteadmis int not null, \n"
                            + " proposepar int not null,\n"
                            + " offretype int not null,\n"
                            + " parsemestre int not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Etudiants ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + "nom varchar(50) not null, \n"
                            + "affecte int, \n"
                            + "appartient_classe int \n"
                            + ")");
            st.executeUpdate(
                    "create table classe ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + "annee int not null, \n"
                            + "appartient_spe int not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Specialite1 ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " nom varchar(50) not null, \n"
                            + "appartient_dep int not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Pays ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " nom varchar(50) not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Departements ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + "nom varchar (50) not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Semestre ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " nom varchar(50) not null unique\n"
                            + ")");
            st.executeUpdate(
                    "create table TypeOffre ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + " type varchar(50) not null unique\n"
                            + ")");
            st.executeUpdate(
                    "create table Role ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + "nom varchar (50) not null, \n"
                            + "description varchar (100) not null \n"
                            + ")");
            st.executeUpdate(
                    "create table Personnel ( \n"
                            + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id") + ",\n"
                            + "nom varchar (50) not null, \n"
                            + "login varchar(50) not null, \n"
                            + "pass varchar(50) not null, \n"
                            + "typerole int not null \n"
                            + ")");
            System.out.println("create OK");

            // création des liens
            st.executeUpdate(
                    """
                            alter table OffreMobilite
                                add constraint fk_offremobilite_proposepar
                                foreign key (proposepar) references Partenaire(id)
                                on delete restrict on update restrict
                            """);
            st.executeUpdate(
                    """
                            alter table Etudiants
                                add constraint fk_etudiant_affecte
                                foreign key (affecte) references OffreMobilite(id)
                                on delete restrict on update restrict
                            """);
            st.executeUpdate(
                    """
                            alter table Etudiants
                                add constraint fk_etudiant_appartient_classe
                                foreign key (appartient_classe) references classe(id)
                                on delete restrict on update restrict
                            """);
            st.executeUpdate(
                    """
                            alter table Specialite1
                                add constraint fk_specialite1_appartient_dep
                                foreign key (appartient_dep) references Departements(id)
                                on delete restrict on update restrict
                            """);
            st.executeUpdate(
                    """
                            alter table classe
                                add constraint fk_classe_appartient_spe
                                foreign key (appartient_spe) references Specialite1(id)
                                on delete restrict on update restrict
                            """);
            /* st.executeUpdate(
                    """
                    alter table Partenaire
                        add constraint fk_partenaire_localisation
                        foreign key (localisation) references pays(id)
                        on delete restrict on update restrict
                    """);*/
            System.out.println("contraintes ok 1");

            st.executeUpdate(
                    """
                            alter table OffreMobilite
                                add constraint fk_offremobilite_parsemestre
                                foreign key (parsemestre) references Semestre(id)
                                on delete restrict on update restrict
                            """);
            System.out.println("contraintes ok 2");

            st.executeUpdate(
                    """
                            alter table OffreMobilite
                                add constraint fk_offremobilite_speadmis
                                foreign key (specialiteadmis) references Specialite1(id)
                                on delete restrict on update restrict
                            """);
            System.out.println("contraintes ok 3");

            st.executeUpdate(
                    """
                            alter table OffreMobilite
                                add constraint fk_offremobilite_offretype
                                foreign key (offretype) references TypeOffre(id)
                                on delete restrict on update restrict
                            """);
            System.out.println("contraintes ok 4");

            st.executeUpdate(
                    """
                            alter table Personnel
                                add constraint fk_personnel_typerole
                                foreign key (typerole) references Role(id)
                                on delete restrict on update restrict
                            """);
            System.out.println("contraintes ok 5");

            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
        }
    }

    /**
     * suppression complete de toute la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void deleteSchema(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            // je supprime d'abord les liens
            try {
                st.executeUpdate(
                        "alter table OffreMobilite drop constraint fk_offremobilite_proposepar");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table OffreMobilite drop constraint fk_offremobilite_speadmis");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            System.out.println("test 5");
            try {
                st.executeUpdate(
                        "alter table Etudiants drop constraint fk_etudiant_affecte");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table classe drop constraint fk_etudiant_appartient_classe");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            System.out.println("test 6");
            try {
                st.executeUpdate(
                        "alter table Specialite1 drop constraint fk_specialite1_appartient_dep");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table classe drop constraint fk_classe_appartient_spe");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            System.out.println("test 7");
            try {
                st.executeUpdate(
                        "alter table OffreMobilite drop constraint fk_offreobilite_parsemestre");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table OffreMobilite drop constraint fk_offremobilite_offretype");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }
            try {
                st.executeUpdate(
                        "alter table Personnel drop constraint fk_personnel_typerole");
            } catch (SQLException ex) {
                // nothing to do : maybe the constraint was not created
            }

            // je peux maintenant supprimer les tables
            try {
                st.executeUpdate("drop table OffreMobilite");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table Partenaire");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table Etudiants");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table classe");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table Specialite1");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table Pays");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table Departements");
            } catch (SQLException ex) {
            }
            try {
                st.executeUpdate("drop table Semestre");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table Role ");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table Personnel ");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate("drop table TypeOffre ");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            System.out.println("test 8");
        }
    }

    /**
     * crée un jeu de test dans la BdD.
     *
     * @param con
     * @throws SQLException
     */
    public static void initBdDTest(Connection con) throws SQLException {
        List<Partenaire> partenaires = List.of(
                new Partenaire("MIT"),
                new Partenaire("Oxford")
        );
        for (var p : partenaires) {
            p.saveInDB(con);
        }
        System.out.println("Create partenaires OK");

        List<OffreMobilite> offres = List.of(
                new OffreMobilite(1, partenaires.get(0).getId()),
                new OffreMobilite(2, partenaires.get(0).getId()),
                new OffreMobilite(5, partenaires.get(1).getId())
        );
        for (var o : offres) {
            o.saveInDB(con);
        }
        System.out.println("Create offres OK");

        List<Departements> depart = List.of(
                new Departements(1, "GT3E"),
                new Departements(2, "Batiment"),
                new Departements(3, "Mecanique")
        );
        for (var d : depart) {
            d.saveInDB(con);
        }
        System.out.println("Create departement OK");

        List<Specialite1> spe = List.of(
                new Specialite1("Plasturgie", 3),
                new Specialite1("Mecatronique", 3),
                new Specialite1("Mecanique", 3)
        );
        for (var s : spe) {
            s.saveInDB(con);
        }
        System.out.println("Create specialite OK");

    }

    public static void razBDD(Connection con) throws SQLException {
        deleteSchema(con);
        creeSchema(con);
        initBdDTest(con);
    }

    public static void menuPartenaire(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu partenaires");
            System.out.println("==================");
            System.out.println((i++) + ") liste de tous les partenaires");
            System.out.println((i++) + ") créer un nouveau partenaire");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Partenaire> users = Partenaire.tousLesPartenaires(con);
                    System.out.println(users.size() + " utilisateurs : ");
                    System.out.println(ListUtils.enumerateList(users, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Partenaire.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuOffre(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu offres mobilité");
            System.out.println("==================");
            System.out.println((i++) + ") liste de toutes les offres");
            System.out.println((i++) + ") créer une nouvelle offre");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<OffreMobilite> offres = OffreMobilite.toutesLesOffres(con);
                    System.out.println(offres.size() + " offres : ");
                    System.out.println(ListUtils.enumerateList(offres, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    OffreMobilite.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuDepartements(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu Departements");
            System.out.println("==================");
            System.out.println((i++) + ") liste de tous les départements");
            System.out.println((i++) + ") créer un nouveaux départements");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    List<Departements> dep = Departements.tousLesDep(con);
                    System.out.println(dep.size() + " departements : ");
                    System.out.println(ListUtils.enumerateList(dep, (elem) -> elem.toString()));
                } else if (rep == j++) {
                    Departements.creeConsole(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void menuBdD(Connection con) {
        int rep = -1;
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu gestion base de données");
            System.out.println("============================");
            System.out.println((i++) + ") RAZ BdD = delete + create + init");
            System.out.println((i++) + ") donner un ordre SQL update quelconque");
            System.out.println((i++) + ") donner un ordre SQL query quelconque");
            System.out.println("0) Retour");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    razBDD(con);
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("ordre SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        pst.executeUpdate();
                    }
                } else if (rep == j++) {
                    String ordre = ConsoleFdB.entreeString("requete SQL : ");
                    try (PreparedStatement pst = con.prepareStatement(ordre)) {
                        try (ResultSet rst = pst.executeQuery()) {
                            System.out.println(ResultSetUtils.formatResultSetAsTxt(rst));
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa.beuvron", 3));
            }
        }
    }

    public static void menuPrincipal() {
        int rep = -1;
        Connection con = null;
        try {
            con = ConnectionSimpleSGBD.defaultCon();
            System.out.println("Connection OK");
        } catch (SQLException ex) {
            System.out.println("Problème de connection : " + ex.getLocalizedMessage());
            throw new Error(ex);
        }
        while (rep != 0) {
            int i = 1;
            System.out.println("Menu principal");
            System.out.println("==================");
            System.out.println((i++) + ") test driver mysql");
            System.out.println((i++) + ") menu gestion BdD");
            System.out.println((i++) + ") menu partenaires");
            System.out.println((i++) + ") menu offres");
            System.out.println((i++) + ") menu departements");
            System.out.println("0) Fin");
            rep = ConsoleFdB.entreeEntier("Votre choix : ");
            try {
                int j = 1;
                if (rep == j++) {
                    try {
                        Class<Driver> mysqlDriver = (Class<Driver>) Class.forName("com.mysql.cj.jdbc.Driver");
                    } catch (ClassNotFoundException ex) {
                        System.out.println("com.mysql.cj.jdbc.Driver not found");
                    }
                    DatabaseMetaData meta = con.getMetaData();
                    System.out.println("jdbc driver version : " + meta.getDriverName() + " ; " + meta.getDriverVersion());
                } else if (rep == j++) {
                    menuBdD(con);
                } else if (rep == j++) {
                    menuPartenaire(con);
                } else if (rep == j++) {
                    menuOffre(con);
                } else if (rep == j++) {
                    menuDepartements(con);
                }
            } catch (Exception ex) {
                System.out.println(ExceptionsUtils.messageEtPremiersAppelsDansPackage(ex, "fr.insa", 3));
            }
        }
    }

    public static void main(String[] args) {
        menuPrincipal();
    }
}
