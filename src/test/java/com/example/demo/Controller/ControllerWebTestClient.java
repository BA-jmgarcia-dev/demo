package com.example.demo.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.models.Cuenta;
import com.example.demo.models.TransaccionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerWebTestClient {

    @Autowired
    private WebTestClient client;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransferir() throws JsonProcessingException{
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigenId(1);
        transaccionDto.setCuentaDestinoId(2);
        transaccionDto.setIdBanco(1);
        transaccionDto.setMonto(new BigDecimal("10"));

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "ok");
        response.put("mensaje", "transferencia realizada con exito");
        response.put("transaccion", transaccionDto);

        client.post().uri("/api/cuentas/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(transaccionDto)
            .exchange()
        // then
            .expectStatus().isOk()
            .expectBody()
            .consumeWith(respuesta -> {
                try {
                    JsonNode json = objectMapper.readTree(respuesta.getResponseBody());
                    assertEquals("ok", json.get("status").asText());
                    assertEquals(1, json.get("transaccion").get("cuentaOrigenId").asInt());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })
            .jsonPath("$.mensaje").isNotEmpty()
            .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
            .json(objectMapper.writeValueAsString(response));
    }

    @Test
    @Order(3)
    void testDetalle1(){
        client.get().uri("/api/cuentas/1").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.persona" ).isEqualTo("Juan")
            .jsonPath("$.saldo").isEqualTo(190.00);
    }

    @Test
    @Order(2)
    void testDetalle2(){
        client.get().uri("/api/cuentas/2").exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Cuenta.class)
            .consumeWith(response -> {
                Cuenta cuenta = response.getResponseBody();
                assertEquals("Jose", cuenta.getPersona());
                assertEquals("310.00", cuenta.getSaldo().toPlainString());
            });
    }

    @Test
    @Order(4)
    void testListar(){
        client.get().uri("/api/cuentas").exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Cuenta.class)
        .consumeWith(response -> {
            List<Cuenta> cuentas = response.getResponseBody();
            assertNotNull(cuentas);
            assertEquals(2, cuentas.size());
            assertEquals(1, cuentas.get(0).getId());
            assertEquals("310.00", cuentas.get(1).getSaldo().toPlainString());
        }).hasSize(2);
    }

    @Test
    @Order(5)
    void testGuardar(){
        // given
        Cuenta cuenta = new Cuenta("Ramon", new BigDecimal("500.00"));
        
        // when
        client.post().uri("/api/cuentas/create-account")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(cuenta)
            .exchange()
        // then 
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Cuenta.class)
            .consumeWith(response -> {
                Cuenta cuentaCreada = response.getResponseBody();
                assertEquals(3, cuentaCreada.getId());
                assertEquals("500.00", cuentaCreada.getSaldo().toPlainString());
            });
    }
}