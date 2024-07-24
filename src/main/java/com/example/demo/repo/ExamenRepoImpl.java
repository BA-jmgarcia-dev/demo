package com.example.demo.repo;

import java.util.List;

import com.example.demo.models.Examen;
import com.example.demo.services.Datos;

public class ExamenRepoImpl implements ExamenRepo{
    @Override
    public Examen guardar(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> findAll() {
        return Datos.EXAMENES;
    }
}
