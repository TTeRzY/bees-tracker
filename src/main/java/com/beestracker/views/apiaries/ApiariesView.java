package com.beestracker.views.apiaries;

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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

import javax.annotation.security.PermitAll;

@PageTitle("Пчелини")
@Route(value = "apiaries", layout = MainLayout.class)
@PermitAll
public class ApiariesView extends VerticalLayout {

    private final ApiaryService apiaryService;
    private Button editButton;
    private Button deleteButton;
    private final Grid<Apiary> grid;

    public ApiariesView(ApiaryService apiaryService) {
        this.apiaryService = apiaryService;

        addButtonsLayout();
        grid = new Grid<>(Apiary.class, false);
        grid.addColumn(Apiary::getApiaryId).setHeader("Рег. номер на пчелин");
        grid.addColumn(Apiary::getName).setHeader("Име");
        grid.addColumn(Apiary::getAddress).setHeader("Адрес");

        TextField searchName = new TextField();
        searchName.getElement().setAttribute("aria-label", "Име");
        searchName.setPlaceholder("Име");
        searchName.setClearButtonVisible(true);
        searchName.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchName.addValueChangeListener(event -> grid.setItems(query -> apiaryService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
                .filter(item -> {
                    final String name = item.getName().toLowerCase();
                    final String searchTerm = event.getValue().trim().toLowerCase();
                    if (StringUtils.isNotBlank(searchTerm))
                    {
                        return name.contains(searchTerm);
                    }
                    return true;
                })
        ));

        TextField searchAddress = new TextField();
        searchAddress.getElement().setAttribute("aria-label", "Адрес");
        searchAddress.setPlaceholder("Адрес");
        searchAddress.setClearButtonVisible(true);
        searchAddress.setPrefixComponent(VaadinIcon.SEARCH.create());
        searchAddress.addValueChangeListener(event -> grid.setItems(query -> apiaryService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
                .filter(item -> {
                    final String address = item.getAddress().toLowerCase();
                    final String searchTerm = event.getValue().trim().toLowerCase();
                    if (StringUtils.isNotBlank(searchTerm))
                    {
                        return address.contains(searchTerm);
                    }
                    return true;
                })
        ));

        final HorizontalLayout searchFields = new HorizontalLayout(searchName, searchAddress);
        add(searchFields);

        refreshGridData();
        grid.asSingleSelect().addValueChangeListener(l -> {
            final Apiary value = l.getValue();
            editButton.setEnabled(value != null);
            deleteButton.setEnabled(value != null);
        });
        add(grid);
    }

    private void addButtonsLayout()
    {
        Button addButton = new Button("Добави", l -> {
            final Apiary apiary = new Apiary();
            openApiaryForm(apiary);
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton = new Button("Промени", l -> {
            final Apiary owner = grid.asSingleSelect().getValue();
            openApiaryForm(owner);
        });
        editButton.setEnabled(false);
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton = new Button("Изтрий", l -> {
            final Dialog dialog = new Dialog();
            final Button yes = new Button("Да", removeL -> {
                apiaryService.delete(grid.asSingleSelect().getValue().getId());
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
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.setEnabled(false);
        final HorizontalLayout buttons = new HorizontalLayout(addButton, editButton, deleteButton);
        add(buttons);
    }

    private void refreshGridData() {
        grid.setItems(query -> apiaryService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );
    }

    private void openApiaryForm(Apiary apiary)
    {
        final Dialog dialog = new Dialog();
        final ApiaryForm apiaryForm = new ApiaryForm(apiary);
        apiaryForm.addCancelClickListener(closeL -> dialog.close());
        apiaryForm.addSaveClickListener(saveL -> {
            if (apiaryForm.isSaved())
            {
                apiaryService.update(apiary);
                dialog.close();
                refreshGridData();
            }
        });
        dialog.add(apiaryForm);
        dialog.setHeight("450px");
        dialog.setWidth("300px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.open();
    }


}
