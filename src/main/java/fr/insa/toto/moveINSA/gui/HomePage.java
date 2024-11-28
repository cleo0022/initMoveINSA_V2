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

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 *
 * @author cleoh
 */
@Route(value="home", layout = MainLayout.class)
public class HomePage extends VerticalLayout {
 //private MenuGauche.MenuGauche menuGauche;
    public HomePage() {
        String user = (String) VaadinSession.getCurrent().getAttribute("user");
          //this.menuGauche = new MenuGauche();

        if (user == null) {
            // Redirection si l'utilisateur n'est pas authentifié
            getUI().ifPresent(ui -> ui.navigate(""));
        } else {
            // Contenu de la page
            add(new H1("Bienvenue sur Move INSA, votre site préféré pour préparer votre mobilité !"));
        }
    }
}
