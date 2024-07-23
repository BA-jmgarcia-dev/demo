package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.models.Examen;
import com.example.demo.repo.ExamenRepo;
import com.example.demo.repo.PreguntasRepo;

public class ExamenServiceImpl implements ExamenService {

    private ExamenRepo examenRepo;
    private PreguntasRepo preguntasRepo;

    public ExamenServiceImpl(ExamenRepo examenRepo, PreguntasRepo preguntasRepo){
        this.examenRepo = examenRepo;
        this.preguntasRepo = preguntasRepo;
    }


    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepo.findAll()
            .stream().filter(x -> x.getNombre().contains(nombre)).findFirst();
    }


    @Override
    public Examen findExamenPorNombrePreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if(examenOptional.isPresent()){
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntasRepo.findPreguntaExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }


    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty()){
            preguntasRepo.guardarPreguntas(examen.getPreguntas());
        }
        return examenRepo.guardar(examen);
    }
    
}
