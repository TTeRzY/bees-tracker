package com.beestracker.views.home;

import com.beestracker.views.MainLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.PermitAll;

@PageTitle("Табло")
@Route(value = "home", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSpacing(false);
       Div mainWrapper = new Div();
       mainWrapper.add(apiaryCard());
       mainWrapper.add(beeHiveCard());
       mainWrapper.add(noteCard());
       mainWrapper.setClassName("main-wrapper");
       add(mainWrapper);
        setSizeFull();
    }

    private Div noteCard() {
        Div noteCard = new Div();
        Image img = new Image("images/notebook.png", "notebook");
        noteCard.add(img);
        Anchor noteLink = new Anchor();
        noteLink.setHref("/notes");
        noteLink.setText("Добави записка");
        noteCard.addClassName("card-wrapper");
        noteCard.add(noteLink);
        return noteCard;
    }

    private Div beeHiveCard() {
        Div beeHiveCard = new Div();
        Image img = new Image("images/hives.png", "hives");
        beeHiveCard.add(img);
        Anchor beeHiveLink = new Anchor();
        beeHiveLink.setHref("/beehives");
        beeHiveLink.setText("Добави кошер");
        beeHiveCard.addClassName("card-wrapper");
        beeHiveCard.add(beeHiveLink);
        return beeHiveCard;
    }

    private Div apiaryCard() {
        Div apiaryCard = new Div();
        Image img = new Image("images/apiary.png", "apiary area");
        apiaryCard.add(img);
        Anchor apiaryLink = new Anchor();
        apiaryLink.setHref("/apiaries");
        apiaryLink.setText("Добави пчелин");
        apiaryCard.addClassName("card-wrapper");
        apiaryCard.add(apiaryLink);
        return apiaryCard;
    }

}
