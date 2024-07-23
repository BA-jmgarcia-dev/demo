package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.example.demo.Datos;
import com.example.demo.models.Examen;
import com.example.demo.repo.ExamenRepo;
import com.example.demo.repo.PreguntasRepo;
import com.example.demo.services.ExamenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceTest {

    @Mock
    ExamenRepo examenRepo;
    
    @Mock
    PreguntasRepo preguntasRepo;
    
    @InjectMocks
    ExamenServiceImpl service;

    @Test
    void findExamenByName(){
        List<Examen> datos = Arrays.asList(new Examen(1L, "math"), new Examen(2L, "chemistry"),
        new Examen(3L, "history"), new Examen(4L, "english"));
        when(examenRepo.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("math");
       

        assertTrue(examen.isPresent());
        assertEquals(1L, examen.orElseThrow().getId());
        assertEquals(examen.orElseThrow().getNombre(), "math");   
    }

    @Test
    void findExamenListaVacia(){
        List<Examen> datos = Collections.emptyList();
        when(examenRepo.findAll()).thenReturn(datos);
        Optional<Examen> examen = service.findExamenPorNombre("math");
        assertFalse(examen.isPresent());  
    }

    @Test
    void examenPreguntasTest(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepo.findPreguntaExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        Examen examen = service.findExamenPorNombrePreguntas("math");
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void examenPreguntasTestVerify(){
        // given
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepo.findPreguntaExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        
        // when
        Examen examen = service.findExamenPorNombrePreguntas("math");

        //then
        assertNotNull(examen);
        verify(examenRepo).findAll();
        verify(preguntasRepo).findPreguntaExamenId(anyLong());
    }

    @Test
    void guardarExamenTest(){
        // given 
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        when(examenRepo.guardar(any(Examen.class))).then(new Answer<Examen>() {
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        });

        // when
        Examen examen = service.guardar(newExamen);

        // then
        assertNotNull(examen.getId());
        assertEquals(8L, examen.getId());
        assertEquals("Fisica", examen.getNombre());

        verify(examenRepo).guardar(any(Examen.class));
        verify(preguntasRepo).guardarPreguntas(anyList());
    }

    @Test
    void manejoException(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepo.findPreguntaExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamenPorNombrePreguntas("math");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        verify(examenRepo).findAll();
        verify(preguntasRepo).findPreguntaExamenId(anyLong());
    }

    @Test
    void testArgumentsMatchers(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepo.findPreguntaExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombrePreguntas("math");

        verify(examenRepo).findAll();
        verify(preguntasRepo).findPreguntaExamenId(argThat(arg -> arg != null && arg.equals(1L)));


    }

}


