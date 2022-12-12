package com.beestracker.data.service;

import com.beestracker.data.entity.BeeHive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BeeHiveService {
    private final BeeHiveRepository repository;

    @Autowired
    public BeeHiveService(BeeHiveRepository repository) {
        this.repository = repository;
    }

    public Optional<BeeHive> get(UUID id) {
        return repository.findById(id);
    }

    public BeeHive update(BeeHive entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<BeeHive> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
