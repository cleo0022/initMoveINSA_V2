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
package fr.insa.toto.moveINSA.gui;

/**
 *
 * @author cleoh
 */
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;

public class EnteteProfil extends HorizontalLayout {

    public EnteteProfil() {

        // Image de profil ronde
        Image profileImage = new Image("https://cdn-icons-png.flaticon.com/512/149/149071.png", "Profil");
        profileImage.setWidth("40px");
        profileImage.setHeight("40px");
        profileImage.getStyle().set("border-radius", "50%"); // Rendre l'image ronde

        // Bouton de profil
        Button profileButton = new Button(profileImage);
        profileButton.getStyle().set("background", "transparent");
        profileButton.getStyle().set("border", "none");
        profileButton.getStyle().set("padding", "0");

        // Menu déroulant
        MenuBar menuBar = new MenuBar();
        MenuItem profileMenu = menuBar.addItem(profileButton);

        // Sous-menu pour les informations personnelles
        profileMenu.getSubMenu().addItem("Nom : " + "ZAOUTER"); //A modifier pour obtenir directement via la base de données
        profileMenu.getSubMenu().addItem("Prénom : " + "Eden");
        profileMenu.getSubMenu().addItem("Identifiant : " + "ezaouter01");

        // Sous-menu pour les actions
        profileMenu.getSubMenu().add(new RouterLink("Mes Vœux", MesVoeuxPage.class));
        profileMenu.getSubMenu().add(new RouterLink("Mes Coups de Cœur", MesCoupCoeurPage.class));

        // Mise en page
        this.setWidthFull(); // Étend sur toute la largeur
        this.setPadding(true);
        this.setSpacing(true);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
     
    }
}
