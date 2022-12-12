package com.beestracker.data.service;

import com.beestracker.data.entity.Apiary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiaryService {
    private final ApiaryRepository repository;

    @Autowired
    public ApiaryService(ApiaryRepository repository) {
        this.repository = repository;
    }

    public Optional<Apiary> get(UUID id) {
        return repository.findById(id);
    }

    public Apiary update(Apiary entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Apiary> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
