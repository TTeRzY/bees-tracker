package com.beestracker.data.service;

import com.beestracker.data.entity.Apiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApiaryRepository extends JpaRepository<Apiary, UUID> {
}
