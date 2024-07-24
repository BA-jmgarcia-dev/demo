package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.example.demo.models.Examen;
import com.example.demo.repo.ExamenRepo;
import com.example.demo.repo.ExamenRepoImpl;
import com.example.demo.repo.PreguntasRepo;
import com.example.demo.repo.PreguntasRepoImpl;
import com.example.demo.services.Datos;
import com.example.demo.services.ExamenService;
import com.example.demo.services.ExamenServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ExamenServiceTest {

    @Mock
    ExamenRepoImpl examenRepo;
    
    @Mock
    PreguntasRepoImpl preguntasRepo;
    
    @InjectMocks
    ExamenServiceImpl service;

    @Captor
    ArgumentCaptor<Long> captor;

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
        verify(preguntasRepo).findPreguntaExamenId(argThat(x -> x != null && x.equals(1L)));
    }

    @Test
    void testArgumentsMatchersPersonalizado(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        when(preguntasRepo.findPreguntaExamenId(anyLong())).thenReturn(Datos.PREGUNTAS);
        service.findExamenPorNombrePreguntas("math");

        verify(examenRepo).findAll();
        verify(preguntasRepo).findPreguntaExamenId(argThat(new MisMatchers()));
    }

    public static class MisMatchers implements ArgumentMatcher<Long> {        
        private Long argument;

        @Override
        public boolean matches(Long argument){
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString(){
            return "mensaje de error personalizado " + argument + " debe ser un entero positivo";
        }
    }

    @Test
    void argumentCaptorTest(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        service.findExamenPorNombrePreguntas("math");
        
        verify(preguntasRepo).findPreguntaExamenId(captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void doThrowTest(){
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);

        doThrow(IllegalArgumentException.class).when(preguntasRepo).guardarPreguntas(anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            service.guardar(examen);
        });
    }

    @Test
    void testDoAsnwer(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);

        doAnswer(invocation -> {
          Long id = invocation.getArgument(0);
          return id == 1L ? Datos.PREGUNTAS : Collections.emptyList(); 
        }).when(preguntasRepo).findPreguntaExamenId(anyLong());

        Examen examen = service.findExamenPorNombrePreguntas("math");
        assertEquals(1L, examen.getId());
        assertEquals(5, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("integrales"));
    }

    @Test
    void guardarExamenTestDoAsnwer(){
        // given 
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);
        doAnswer(new Answer<Examen>() {
            Long secuencia = 8L;
            @Override
            public Examen answer(InvocationOnMock invocation) throws Throwable {
                Examen examen = invocation.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(examenRepo).guardar(any(Examen.class));

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
    void doCallRealMethodTest(){
        when(examenRepo.findAll()).thenReturn(Datos.EXAMENES);
        doCallRealMethod().when(preguntasRepo).findPreguntaExamenId(anyLong());
        Examen examen = service.findExamenPorNombrePreguntas("math");
        assertEquals(1L, examen.getId());
        assertEquals("math", examen.getNombre());
    }

    @Test
    void spyTest(){
        ExamenRepo examenRepo = spy(ExamenRepoImpl.class);
        PreguntasRepo preguntaRepo = spy(PreguntasRepoImpl.class);
        ExamenService examenService = new ExamenServiceImpl(examenRepo, preguntaRepo);

        Examen examen = examenService.findExamenPorNombrePreguntas("math");
        assertEquals(1L, examen.getId());
        assertEquals(5, examen.getPreguntas().size());
    
        verify(examenRepo).findAll();
        verify(preguntaRepo).findPreguntaExamenId(anyLong());
    }


}


