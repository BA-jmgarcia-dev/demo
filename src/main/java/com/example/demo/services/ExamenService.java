package com.example.demo.services;

import java.util.Optional;

import com.example.demo.models.Examen;

public interface ExamenService {
    Optional<Examen> findExamenPorNombre(String nombre);
    Examen findExamenPorNombrePreguntas(String nombre);
    Examen guardar(Examen examen);
}
