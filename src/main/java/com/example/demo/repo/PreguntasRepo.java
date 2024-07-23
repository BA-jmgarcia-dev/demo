package com.example.demo.repo;

import java.util.List;

public interface PreguntasRepo {
    List<String> findPreguntaExamenId(Long id);
    void guardarPreguntas(List<String> preguntas);
}
