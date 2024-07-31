package com.example.demo.Controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.models.TransaccionDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerWebTestClient {

    @Autowired
    private WebTestClient client;

    @Test
    void testTransferir(){
        TransaccionDto transaccionDto = new TransaccionDto();
        transaccionDto.setCuentaOrigenId(1);
        transaccionDto.setCuentaDestinoId(2);
        transaccionDto.setIdBanco(1);
        transaccionDto.setMonto(new BigDecimal("10"));
    
        client.post().uri("http://localhost:8080/api/cuentas/transaction")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(transaccionDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.mensaje").isNotEmpty()
            .jsonPath("$.date").isEqualTo(LocalDate.now().toString());
    }


}