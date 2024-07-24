package com.example.demo.repo;

import java.util.List;

import com.example.demo.models.Banco;

public interface BancoRepo {
    
    List<Banco> findAll();
    Banco findById(Long id);
    void update(Banco Banco);
}
