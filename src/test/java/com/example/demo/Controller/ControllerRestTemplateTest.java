package com.example.demo.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.demo.models.Cuenta;
import com.example.demo.models.TransaccionDto;
import com.fasterxml.jackson.databind.ObjectMapper;


@Tag("test_rt")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerRestTemplateTest {
    
    @Autowired
    private TestRestTemplate client;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir(){
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigenId(1);
        transaccionDto.setCuentaDestinoId(2);
        transaccionDto.setIdBanco(1);
        transaccionDto.setMonto(new BigDecimal("10"));

        ResponseEntity<String> response = client.postForEntity("/api/cuentas/transaction", 
            transaccionDto, String.class);
        
        String json = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertNotNull(json);
        assertTrue(json.contains("transferencia realizada con exito"));
    }

    @Test
    @Order(2)
    void testDetalle(){
        ResponseEntity<Cuenta> response = client.getForEntity("/api/cuentas/1", Cuenta.class);
        Cuenta cuenta = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());

        assertNotNull(cuenta);
        assertEquals(1, cuenta.getId());
        assertEquals("190.00", cuenta.getSaldo().toPlainString());
    }

    @Test
    @Order(3)
    void testListar(){
        ResponseEntity<Cuenta[]> response = client.getForEntity("/api/cuentas", Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(response.getBody());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, cuentas.size());
    }

    @Test
    @Order(4)
    void testGuardar(){
        Cuenta cuenta = new Cuenta("Ramon", new BigDecimal("500.00"));
        ResponseEntity<Cuenta> response = client.postForEntity("/api/cuentas/create-account", cuenta, Cuenta.class);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        Cuenta cuentaCreada = response.getBody();
        assertEquals(3, cuentaCreada.getId());
        assertEquals("Ramon", cuentaCreada.getPersona());
        assertEquals("500.00", cuentaCreada.getSaldo().toPlainString());
    }

}
