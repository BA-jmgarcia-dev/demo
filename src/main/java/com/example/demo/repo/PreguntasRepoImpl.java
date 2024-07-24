package com.example.demo.repo;

import java.util.List;

import com.example.demo.services.Datos;

public class PreguntasRepoImpl implements PreguntasRepo{

    @Override
    public List<String> findPreguntaExamenId(Long id) {
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarPreguntas(List<String> preguntas) {
       
    }
    
}
