package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.exceptions.DineroInsuficienteException;
import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;
import com.example.demo.repo.BancoRepo;
import com.example.demo.repo.CuentaRepo;
import com.example.demo.services.CuentaService;
import static com.example.demo.services.Datos.BANCO;
import static com.example.demo.services.Datos.CUENTA_001;
import static com.example.demo.services.Datos.CUENTA_002;



@SpringBootTest
public class CuentaServiceTest {

    @MockBean
    CuentaRepo cuentaRepo;
    
    @MockBean
    BancoRepo bancoRepo;
    
    @Autowired
    CuentaService cuentaService;

    @Test
    void contextLoads(){
        when(cuentaRepo.findById(1)).thenReturn(CUENTA_001());
        when(cuentaRepo.findById(2)).thenReturn(CUENTA_002());
        when(bancoRepo.findById(1)).thenReturn(BANCO());

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        cuentaService.transferir(1, 1, 2, new BigDecimal("100"));
        saldoOrigen = cuentaService.revisarSaldo(1);
        saldoDestino = cuentaService.revisarSaldo(2);

        assertEquals("100", saldoOrigen.toPlainString());
        assertEquals("400", saldoDestino.toPlainString());

        int totalTransferencias = cuentaService.revisarTotalTransferencia(1);
        assertEquals(1, totalTransferencias);
    
        verify(cuentaRepo, times(3)).findById(1);
        verify(cuentaRepo, times(3)).findById(2);
        verify(cuentaRepo, times(2)).save(any(Cuenta.class));

        verify(bancoRepo, times(2)).findById(1);
        verify(bancoRepo).save(any(Banco.class));
    }
    

    @Test
    void contextLoads2(){
        when(cuentaRepo.findById(1)).thenReturn(CUENTA_001());
        when(cuentaRepo.findById(2)).thenReturn(CUENTA_002());
        when(bancoRepo.findById(1)).thenReturn(BANCO());

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, ()-> {
            cuentaService.transferir(1, 1, 2, new BigDecimal("1000"));
        });

        saldoOrigen = cuentaService.revisarSaldo(1);
        saldoDestino = cuentaService.revisarSaldo(2);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        int totalTransferencias = cuentaService.revisarTotalTransferencia(1);
        assertEquals(0, totalTransferencias);
    
        verify(cuentaRepo, times(3)).findById(1);
        verify(cuentaRepo, times(2)).findById(2);
        verify(cuentaRepo, never()).save(any(Cuenta.class));

        verify(bancoRepo, times(1)).findById(1);
        verify(bancoRepo, never()).save(any(Banco.class));

        verify(cuentaRepo, times(5)).findById(any());
        verify(cuentaRepo, never()).findAll();
    }

    @Test
    void contextLoad(){
        when(cuentaRepo.findById(1)).thenReturn(CUENTA_001());

        Cuenta cuenta1 = cuentaRepo.findById(1).orElseThrow();
        Cuenta cuenta2 = cuentaRepo.findById(1).orElseThrow();

        assertSame(cuenta1, cuenta2);
        assertTrue(cuenta1 == cuenta2);
        assertEquals("Juan", cuenta1.getPersona());
        assertEquals("Juan", cuenta2.getPersona());
    }
}
