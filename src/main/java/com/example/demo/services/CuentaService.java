package com.example.demo.services;

import java.math.BigDecimal;

import com.example.demo.models.Cuenta;

public interface CuentaService {
    
    Cuenta findById(Long id);
    
    int revisarTotalTransferencia(Long idBanco);
    
    BigDecimal revisarSaldo(Long idCuenta);
    
    void transferir(Long idBanco, Long idCuentaOrigen, Long idCuentaDestino, BigDecimal monto);
}
