package com.beestracker.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class Note extends AbstractEntity{
    @NotNull(message = "Моля изберете дата!")
    private LocalDate addedDate;
    @ManyToOne
    @JoinColumn(name = "bee_hive_id")
    @NotNull(message = "Изберете кошер!")
    private BeeHive beeHive;
    @NotNull(message = "Моля въведете загалвие!")
    private String title;
    @NotNull(message = "Моля въведете описание!")
    private String description;
}
