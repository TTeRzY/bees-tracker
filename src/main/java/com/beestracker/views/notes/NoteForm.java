package com.beestracker.views.notes;

import com.beestracker.data.entity.BeeHive;
import com.beestracker.data.entity.Note;
import com.beestracker.data.service.BeeHiveService;
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
import org.springframework.data.domain.PageRequest;


public class NoteForm extends VerticalLayout {
    private final DatePicker addedDate = new DatePicker("Дата");
    private final ComboBox<BeeHive> beeHive = new ComboBox<>("Избери кошер");
    private final TextField title = new TextField("Заглавие");
    private final TextField description = new TextField("Описание");
    private boolean saved;

    private final Note note;
    private final BeanValidationBinder<Note> binder = new BeanValidationBinder<>(Note.class);
    private Button cancel;
    private Button save;

    private final BeeHiveService beeHiveService;


    public NoteForm(Note note, BeeHiveService beeHiveService) {
        this.note = note;
        this.beeHiveService = beeHiveService;
        init();
    }


    private void init() {
        setSizeFull();
        final FormLayout form = new FormLayout();
        form.add(addedDate);
        beeHive.setItems(query -> beeHiveService
                .list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );
        beeHive.setItemLabelGenerator(BeeHive::getBeeHiveId);
        form.add(beeHive);
        form.add(title);
        form.add(description);
        binder.bindInstanceFields(this);
        binder.readBean(this.note);
        HorizontalLayout buttons = configureButtons();
        add(form, buttons);
    }


    private HorizontalLayout configureButtons() {
        save = new Button("Запази", l -> {
            if (binder.isValid()) {
                try {
                    binder.writeBean(note);
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