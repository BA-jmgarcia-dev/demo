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
import com.example.demo.services.Datos;


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
        when(cuentaRepo.findById(1L)).thenReturn(Datos.CUENTA_001);
        when(cuentaRepo.findById(2L)).thenReturn(Datos.CUENTA_002);
        when(bancoRepo.findById(1L)).thenReturn(Datos.BANCO);

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        cuentaService.transferir(1L, 1L, 2L, new BigDecimal("100"));
        saldoOrigen = cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("100", saldoOrigen.toPlainString());
        assertEquals("400", saldoDestino.toPlainString());

        int totalTransferencias = cuentaService.revisarTotalTransferencia(1L);
        assertEquals(1, totalTransferencias);
    
        verify(cuentaRepo, times(3)).findById(1L);
        verify(cuentaRepo, times(3)).findById(2L);
        verify(cuentaRepo, times(2)).update(any(Cuenta.class));

        verify(bancoRepo, times(2)).findById(1L);
        verify(bancoRepo).update(any(Banco.class));
    }
    

    @Test
    void contextLoads2(){
        when(cuentaRepo.findById(1L)).thenReturn(Datos.CUENTA_001);
        when(cuentaRepo.findById(2L)).thenReturn(Datos.CUENTA_002);
        when(bancoRepo.findById(1L)).thenReturn(Datos.BANCO);

        BigDecimal saldoOrigen = cuentaService.revisarSaldo(1L);
        BigDecimal saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        assertThrows(DineroInsuficienteException.class, ()-> {
            cuentaService.transferir(1L, 1L, 2L, new BigDecimal("1000"));
        });

        saldoOrigen = cuentaService.revisarSaldo(1L);
        saldoDestino = cuentaService.revisarSaldo(2L);

        assertEquals("200", saldoOrigen.toPlainString());
        assertEquals("300", saldoDestino.toPlainString());

        int totalTransferencias = cuentaService.revisarTotalTransferencia(1L);
        assertEquals(0, totalTransferencias);
    
        verify(cuentaRepo, times(3)).findById(1L);
        verify(cuentaRepo, times(2)).findById(2L);
        verify(cuentaRepo, never()).update(any(Cuenta.class));

        verify(bancoRepo, times(1)).findById(1L);
        verify(bancoRepo, never()).update(any(Banco.class));

        verify(cuentaRepo, times(5)).findById(anyLong());
        verify(cuentaRepo, never()).findAll();
    }

    @Test
    void contextLoad(){
        when(cuentaRepo.findById(1L)).thenReturn(Datos.CUENTA_001);

        Cuenta cuenta1 = cuentaRepo.findById(1L);
        Cuenta cuenta2 = cuentaRepo.findById(1L);

        assertSame(cuenta1, cuenta2);
        assertTrue(cuenta1 == cuenta2);
        assertEquals("Juan", cuenta1.getPersona());
        assertEquals("Juan", cuenta2.getPersona());
    }
}
