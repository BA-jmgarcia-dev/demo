package com.example.demo.repo;

import java.util.List;

import com.example.demo.models.Cuenta;

public interface CuentaRepo {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    void update(Cuenta cuenta);
    
}
