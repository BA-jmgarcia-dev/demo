package com.example.demo.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Examen {
    
    private Long id;
    private String nombre;
    private List<String> preguntas;

    public Examen(Long id, String nombre){
        this.id = id;
        this.nombre = nombre;
        this.preguntas = new ArrayList<>();
    }
}

