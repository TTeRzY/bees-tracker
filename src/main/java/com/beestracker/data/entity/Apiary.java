package com.beestracker.data.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Apiary extends AbstractEntity{
    @NotNull(message = "Моля въведете идентификатор на пчелина!")
    private String apiaryId;
    private String name;
    private String address;
}
