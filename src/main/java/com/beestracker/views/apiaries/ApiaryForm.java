package com.beestracker.views.apiaries;

import com.beestracker.data.entity.Apiary;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;


public class ApiaryForm extends VerticalLayout
{
    private final TextField apiaryId = new TextField("Идентификатор на пчелин");
    private final TextField name = new TextField("Име");
    private final TextField address = new TextField("Адрес");
    private boolean saved;

    private final Apiary apiary;
    private final BeanValidationBinder<Apiary> binder = new BeanValidationBinder<>(Apiary.class);
    private Button cancel;
    private Button save;


    public ApiaryForm(Apiary apiary)
    {
        this.apiary = apiary;
        init();
    }


    private void init()
    {
        setSizeFull();
        final FormLayout form = new FormLayout();
        form.add(apiaryId);
        form.add(name);
        form.add(address);
        binder.bindInstanceFields(this);
        binder.readBean(this.apiary);
        HorizontalLayout buttons = configureButtons();
        add(form, buttons);
    }


    private HorizontalLayout configureButtons()
    {
        save = new Button("Запази", l -> {
            if (binder.isValid())
            {
                try
                {
                    binder.writeBean(apiary);
                    saved = true;
                }
                catch (ValidationException e)
                {
                    e.printStackTrace();
                }
            }
        });
        cancel = new Button("Откажи");
        return new HorizontalLayout(save, cancel);
    }


    public void addCancelClickListener(ComponentEventListener<ClickEvent<Button>> clickListener)
    {
        cancel.addClickListener(clickListener);
    }


    public void addSaveClickListener(ComponentEventListener<ClickEvent<Button>> clickListener)
    {
        save.addClickListener(clickListener);
    }


    public boolean isSaved()
    {
        return saved;
    }
}