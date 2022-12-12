package com.beestracker.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class BeeHive extends AbstractEntity{
    @NotNull(message = "Въведете рег. номер на кошер!")
    private String beeHiveId;
    @ManyToOne
    @NotNull(message = "Изберете пчелин!")
    private Apiary apiary;
    @NotNull(message = "Въведете модел!")
    private String model;
    @NotNull(message = "Въведете брой рамки!")
    private String frames;
    private LocalDate registerDate;
    @NotNull(message = "Въведете сила на кошера от 1 до 10!")
    private int strength;
}
