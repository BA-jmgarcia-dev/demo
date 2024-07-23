package com.example.demo.repo;

import java.util.List;

import com.example.demo.models.Examen;

public interface ExamenRepo {
    List<Examen> findAll();
    Examen guardar(Examen examen);
}
