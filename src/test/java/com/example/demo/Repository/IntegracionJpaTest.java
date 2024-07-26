package com.example.demo.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.models.Cuenta;
import com.example.demo.repo.CuentaRepo;

@DataJpaTest
public class IntegracionJpaTest {

    @Autowired
    CuentaRepo cuentaRepo;

    @Test
    void findByIdTest(){
        Optional<Cuenta> cuenta =  cuentaRepo.findById(1);
        assertTrue(cuenta.isPresent());
        assertEquals("Juan", cuenta.orElseThrow().getPersona());        
    }
    
    @Test
    void findByPersonaTest(){
        Optional<Cuenta> cuenta =  cuentaRepo.findByPersona("Jose");
        assertTrue(cuenta.isPresent());
        assertEquals("Jose", cuenta.orElseThrow().getPersona());        
    }

    @Test
    void findByPersonaThrowExceptionTest(){
        Optional<Cuenta> cuenta =  cuentaRepo.findByPersona("Jose R");
        assertFalse(cuenta.isPresent());
        assertThrows(NoSuchElementException.class, cuenta::orElseThrow);       
    }

    @Test
    void findAllTest(){
        List<Cuenta> cuenta =  cuentaRepo.findAll();      
        assertFalse(cuenta.isEmpty());
        assertEquals(2, cuenta.size());       
    }

    @Test
    void saveTest(){
        // Given
        Cuenta newCuenta = new Cuenta("Pepe", new BigDecimal("200"));
        cuentaRepo.save(newCuenta);

        // When
        Cuenta cuenta = cuentaRepo.findById(newCuenta.getId()).orElseThrow();

        // Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("200", cuenta.getSaldo().toPlainString());
        assertEquals(3, cuenta.getId());
    }

    @Test
    void updateTest(){
        // Given
        Cuenta newCuenta = new Cuenta("Pepe", new BigDecimal("200"));
        cuentaRepo.save(newCuenta);

        // When
        Cuenta cuenta = cuentaRepo.findById(newCuenta.getId()).orElseThrow();

        // Then
        assertEquals("Pepe", cuenta.getPersona());
        assertEquals("200", cuenta.getSaldo().toPlainString());
        // assertEquals(3, cuenta.getId());

        // When
        newCuenta.setSaldo(new BigDecimal("300"));
        Cuenta cuentaActualizada = cuentaRepo.save(newCuenta);

        // Then
        assertEquals("300", cuentaActualizada.getSaldo().toPlainString());
    }

    @Test
    void deleteTest(){
        Cuenta cuenta = cuentaRepo.findById(1).orElseThrow();
        assertEquals("Juan", cuenta.getPersona());

        cuentaRepo.delete(cuenta);

        assertThrows(NoSuchElementException.class, ()-> cuentaRepo.findById(1).orElseThrow());
        assertEquals(1, cuentaRepo.findAll().size());
    }

}
