package com.example.demo.Controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.controllers.CuentaController;
import com.example.demo.models.Cuenta;
import com.example.demo.models.TransaccionDto;
import com.example.demo.services.CuentaService;
import com.example.demo.services.Datos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CuentaController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CuentaService cuentaService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    void detalleTest() throws Exception{
        // Given
        when(cuentaService.findById(1)).thenReturn(Datos.CUENTA_001().orElseThrow());
    
        // When
        mvc.perform(get("/api/cuentas/1").contentType(MediaType.APPLICATION_JSON))
        // Then
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.persona").value("Juan"))
            .andExpect(jsonPath("$.saldo").value("200"));

        verify(cuentaService).findById(1);
    
    }

    @Test
    void transferirTest() throws JsonProcessingException, Exception{
        // Given
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaDestinoId(2);
        transaccionDto.setCuentaOrigenId(1);
        transaccionDto.setMonto(new BigDecimal("10"));
        transaccionDto.setIdBanco(1);

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "transferencia realizada con exito");
        response.put("transaccion", transaccionDto);

        // When
        mvc.perform(post("/api/cuentas/transaction").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transaccionDto)))

        // Then
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
            .andExpect(jsonPath("$.mensaje").value("transferencia realizada con exito"))
            .andExpect(jsonPath("$.transaccion.cuentaOrigenId").value(transaccionDto.getCuentaOrigenId()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
    
    @Test
    void listarCuentasTest() throws Exception {
        // Given
        List<Cuenta> cuentas = Arrays.asList(Datos.CUENTA_001().orElseThrow(), 
            Datos.CUENTA_002().orElseThrow());

        when(cuentaService.findAll()).thenReturn(cuentas);

        mvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].persona").value("Juan"))
            .andExpect(jsonPath("$[1].persona").value("Jose"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(content().json(objectMapper.writeValueAsString(cuentas)));
    }

    @Test
    void guardarTest() throws JsonProcessingException, Exception{
        // Given
        Cuenta cuenta = new Cuenta("Roberto", new BigDecimal("500"));
        when(cuentaService.createCuenta(any())).then(invocation -> {
            Cuenta c = invocation.getArgument(0);
            c.setId(3);
            return c;
        });

        // When
        mvc.perform(post("/api/cuentas/create-account").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cuenta)))
        // Then
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(3)))
            .andExpect(jsonPath("$.persona", is("Roberto")));
        verify(cuentaService).createCuenta(any());
        
    }
}
