package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.models.Examen;
import com.example.demo.repo.ExamenRepoImpl;
import com.example.demo.repo.PreguntasRepoImpl;
import com.example.demo.services.ExamenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceSpyTest {

    @Spy
    ExamenRepoImpl examenRepo;
    
    @Spy
    PreguntasRepoImpl preguntasRepo;
    
    @InjectMocks
    ExamenServiceImpl service;

    @Test
    void spyTest(){
        Examen examen = service.findExamenPorNombrePreguntas("math");
        assertEquals(1L, examen.getId());
        assertEquals(5, examen.getPreguntas().size());
    
        verify(examenRepo).findAll();
        verify(preguntasRepo).findPreguntaExamenId(anyLong());
    }


}


