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

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

/**
 * Utilisé par toutes les pages comme layout.
 * <p>
 * C'est ici que sont initialisées les infos valables pour l'ensemble de la
 * session, et en particulier la connection à la base de donnée.
 * </p>
 *
 * @author francois
 */
public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private MenuGauche menuGauche;
    private EnteteProfil enteteProfil;

    public MainLayout() {
        // Initialiser le menu gauche
        this.menuGauche = new MenuGauche();
        this.menuGauche.setHeightFull();

        // Initialiser l'entête profil
        this.enteteProfil = new EnteteProfil();

        // Créer le bouton DrawerToggle pour le menu gauche
        DrawerToggle toggle = new DrawerToggle();

        // Ajouter le toggle et le profil dans la navbar
        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.setWidthFull(); // Étendre sur toute la largeur
        navbarLayout.setPadding(false);
        navbarLayout.setSpacing(true);
        navbarLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Ajouter le bouton toggle (à gauche)
        navbarLayout.add(toggle);

        // Ajouter un espace flexible pour forcer le profil à droite
        navbarLayout.addAndExpand(new HorizontalLayout());

        // Ajouter le profil (à droite)
        navbarLayout.add(this.enteteProfil);

        // Appliquer un style de fond temporaire pour la débogue
        navbarLayout.getStyle().set("border", "1px solid black"); // Ligne pour voir les limites du layout
        this.enteteProfil.getStyle().set("background-color", "lightblue"); // Fond bleu pour vérifier sa visibilité

        // Ajouter la barre de navigation
        this.addToNavbar(navbarLayout);

        // Ajouter le menu gauche au Drawer
        this.addToDrawer(this.menuGauche);
       
    }
    //System.out.println("MainLayout constructeur "+this);
//        Scroller scroller = new Scroller(this.menuGauche);
//        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
//        scroller.setHeightFull();
//        VerticalLayout pourScroll = new VerticalLayout(this.menuGauche);
//        pourScroll.setHeightFull();
//        pourScroll.getStyle().set("display", "block");
//        Scroller scroller = new Scroller(pourScroll);
//        scroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
//        scroller.setHeightFull();
    /**
     * Cette méthode est appelée systématiquement par vaadin avant l'affichage
     * de toute page ayant ce layout (donc à priori toutes les pages "normales"
     * sauf pages d'erreurs) de l'application.
     * <p>
     * pour l'instant, je ne m'en sers pas, mais je l'ai gardé pour me souvenir
     * de cette possibilité
     * </p>
     *
     * @param bee
     */
    @Override
    public void beforeEnter(BeforeEnterEvent bee) {
        // permet par exemple de modifier la destination en cas 
        // de problème
//            bee.rerouteTo(NoConnectionToBDDErrorPanel.class);

    }

}
