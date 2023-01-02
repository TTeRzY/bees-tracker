package com.beestracker.views.beehives;

import com.beestracker.data.entity.Apiary;
import com.beestracker.data.entity.BeeHive;
import com.beestracker.data.service.ApiaryService;
import com.beestracker.data.service.BeeHiveService;
import com.beestracker.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.springframework.data.domain.PageRequest;

import javax.annotation.security.PermitAll;

@PageTitle("Beehives")
@Route(value = "beehives", layout = MainLayout.class)
@PermitAll
public class BeehivesView extends VerticalLayout {
    private final BeeHiveService beeHiveService;
    private final ApiaryService apiaryService;
    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private final Grid<BeeHive> grid;
    public BeehivesView(BeeHiveService beeHiveService, ApiaryService apiaryService) {
        this.beeHiveService = beeHiveService;
        this.apiaryService = apiaryService;

        addButtonsLayout();
        grid = new Grid<>(BeeHive.class, false);
        grid.addColumn(BeeHive::getBeeHiveId).setHeader("Рег. номер на кошер");
        grid.addColumn(beeHive -> {
            final Apiary beeHiveApiary = beeHive.getApiary();
            if (beeHiveApiary != null)
            {
                return beeHiveApiary.getName();
            }
            return "";
        }).setHeader("Пчелин");
        grid.addColumn(BeeHive::getModel).setHeader("Модел на кошера");
        grid.addColumn(BeeHive::getFrames).setHeader("Брой рамки");
        grid.addColumn(BeeHive::getStrength).setHeader("Сила на кошера");
        grid.addColumn(BeeHive::getRegisterDate).setHeader("Дата на регистрация");

        refreshGridData();
        grid.asSingleSelect().addValueChangeListener(l -> {
            final BeeHive value = l.getValue();
            editButton.setEnabled(value != null);
            deleteButton.setEnabled(value != null);

        });
        add(grid);
    }

    private void addButtonsLayout() {
        addButton = new Button("Добави", l -> {
            final BeeHive beeHive = new BeeHive();
            openBeeHiveForm(beeHive);
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton = new Button("Промени", l -> {
            final BeeHive beeHive = grid.asSingleSelect().getValue();
            openBeeHiveForm(beeHive);
        });
        editButton.setEnabled(false);
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton = new Button("Изтрий", l -> {
            final Dialog dialog = new Dialog();
            final Button yes = new Button("Да", removeL -> {
                beeHiveService.delete(grid.asSingleSelect().getValue().getId());
                dialog.close();
                refreshGridData();
            });
            final Button no = new Button("Не", removeL -> dialog.close());
            final HorizontalLayout removeDialogButtons = new HorizontalLayout(yes, no);
            dialog.setWidth("350px");
            dialog.setHeight("200px");
            dialog.add(new VerticalLayout(new H3("Сигурен ли сте ?"), removeDialogButtons));
            dialog.open();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.setEnabled(false);
        final HorizontalLayout buttons = new HorizontalLayout(addButton, editButton, deleteButton);
        add(buttons);
    }

    private void openBeeHiveForm(BeeHive beeHive) {
        final Dialog dialog = new Dialog();
        final BeeHiveForm beeHiveForm = new BeeHiveForm(beeHive, apiaryService);
        beeHiveForm.addCancelClickListener(closeL -> dialog.close());
        beeHiveForm.addSaveClickListener(saveL -> {
            if (beeHiveForm.isSaved())
            {
                beeHiveService.update(beeHive);
                dialog.close();
                refreshGridData();
            }
        });
        dialog.add(beeHiveForm);
        dialog.setHeight("450px");
        dialog.setWidth("300px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.open();
    }

    private void refreshGridData() {
        grid.setItems(query -> beeHiveService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );
    }

}
