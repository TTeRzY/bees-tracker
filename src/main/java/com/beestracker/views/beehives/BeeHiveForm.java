package com.beestracker.views.beehives;

import com.beestracker.data.entity.Apiary;
import com.beestracker.data.entity.BeeHive;
import com.beestracker.data.service.ApiaryService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import org.hibernate.sql.Select;
import org.springframework.data.domain.PageRequest;


public class BeeHiveForm extends VerticalLayout {
    private final TextField beeHiveId = new TextField("Идентификатор на кошер");
    private final ComboBox<Apiary> apiary = new ComboBox<>("Пчелини");
    private final TextField model = new TextField("Модел");
    private final TextField frames = new TextField("Брой рамки");
    private final TextField strength = new TextField("Сила");
    private final DatePicker registerDate = new DatePicker("Дата");
    private boolean saved;

    private final BeeHive beeHive;
    private final BeanValidationBinder<BeeHive> binder = new BeanValidationBinder<>(BeeHive.class);
    private Button cancel;
    private Button save;

    private final ApiaryService apiaryService;


    public BeeHiveForm(BeeHive beeHive, ApiaryService apiaryService) {
        this.beeHive = beeHive;
        this.apiaryService = apiaryService;
        init();
    }


    private void init() {
        setSizeFull();
        final FormLayout form = new FormLayout();
        form.add(beeHiveId);
        apiary.setItems(query -> apiaryService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );
        apiary.setItemLabelGenerator(Apiary::getName);
        form.add(apiary);
        form.add(model);
        form.add(frames);
        form.add(strength);
        form.add(registerDate);
        binder.bindInstanceFields(this);
        binder.readBean(this.beeHive);
        HorizontalLayout buttons = configureButtons();
        add(form, buttons);
    }


    private HorizontalLayout configureButtons() {
        save = new Button("Създай", l -> {
            if (binder.isValid()) {
                try {
                    binder.writeBean(beeHive);
                    saved = true;
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }
        });
        cancel = new Button("Откажи");
        return new HorizontalLayout(save, cancel);
    }


    public void addCancelClickListener(ComponentEventListener<ClickEvent<Button>> clickListener) {
        cancel.addClickListener(clickListener);
    }


    public void addSaveClickListener(ComponentEventListener<ClickEvent<Button>> clickListener) {
        save.addClickListener(clickListener);
    }


    public boolean isSaved() {
        return saved;
    }
}