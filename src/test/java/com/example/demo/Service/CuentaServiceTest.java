package com.example.demo.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;
import com.example.demo.repo.BancoRepo;
import com.example.demo.repo.CuentaRepo;
import com.example.demo.services.CuentaService;
import com.example.demo.services.CuentaServiceImpl;
import com.example.demo.services.Datos;

public class CuentaServiceTest {

    private CuentaRepo cuentaRepo;
    private BancoRepo bancoRepo;
    private CuentaService cuentaService;

    @BeforeEach
    void setUp(){
        cuentaRepo = mock(CuentaRepo.class);
        bancoRepo = mock(BancoRepo.class);
        cuentaService = new CuentaServiceImpl(cuentaRepo, bancoRepo);
    }

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
    
}
