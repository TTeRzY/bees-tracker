package com.beestracker.data.service;

import com.beestracker.data.entity.BeeHive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeeHiveRepository extends JpaRepository<BeeHive, UUID> {
}
