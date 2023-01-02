package com.beestracker.views.notes;

import com.beestracker.data.entity.BeeHive;
import com.beestracker.data.entity.Note;
import com.beestracker.data.service.BeeHiveService;
import com.beestracker.data.service.NoteService;
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

@PageTitle("Notes")
@Route(value = "notes", layout = MainLayout.class)
@PermitAll
public class NotesView extends VerticalLayout {
    private final NoteService noteService;
    private final BeeHiveService beeHiveService;
    private final Grid<Note> noteGrid;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;

    public NotesView(NoteService noteService, BeeHiveService beeHiveService) {
        this.noteService = noteService;
        this.beeHiveService = beeHiveService;

        addButtonsLayout();
        noteGrid = new Grid<>(Note.class, false);
        noteGrid.addColumn(Note::getAddedDate).setHeader("Дата");
        noteGrid.addColumn(note -> {
            final BeeHive beeHive = note.getBeeHive();
            if (beeHive != null)
            {
                return beeHive.getBeeHiveId();
            }
            return "";
        }).setHeader("Кошер");
        noteGrid.addColumn(Note::getTitle).setHeader("Заглавие");
        noteGrid.addColumn(Note::getDescription).setHeader("Описание");
        refreshGridData();
        noteGrid.asSingleSelect().addValueChangeListener(l -> {
            final Note value = l.getValue();
            editButton.setEnabled(value != null);
            deleteButton.setEnabled(value != null);

        });
        add(noteGrid);

    }
    private void addButtonsLayout() {
        addButton = new Button("Добави", l -> {
            final Note note = new Note();
            openNoteForm(note);
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton = new Button("Промени", l -> {
            final Note note = noteGrid.asSingleSelect().getValue();
            openNoteForm(note);
        });
        editButton.setEnabled(false);
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton = new Button("Изтрий", l -> {
            final Dialog dialog = new Dialog();
            final Button yes = new Button("Да", removeL -> {
                noteService.delete(noteGrid.asSingleSelect().getValue().getId());
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

    private void openNoteForm(Note note) {
        final Dialog dialog = new Dialog();
        final NoteForm noteForm = new NoteForm(note, beeHiveService);
        noteForm.addCancelClickListener(closeL -> dialog.close());
        noteForm.addSaveClickListener(saveL -> {
            if (noteForm.isSaved())
            {
                noteService.update(note);
                dialog.close();
                refreshGridData();
            }
        });
        dialog.add(noteForm);
        dialog.setHeight("450px");
        dialog.setWidth("300px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.open();
    }

    private void refreshGridData() {
        noteGrid.setItems(query -> noteService
                .list(PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream()
        );
    }

}
