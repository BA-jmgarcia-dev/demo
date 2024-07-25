package com.example.demo.services;

import java.math.BigDecimal;

import com.example.demo.models.Cuenta;

public interface CuentaService {
    
    Cuenta findById(Integer id);
    
    int revisarTotalTransferencia(Integer idBanco);
    
    BigDecimal revisarSaldo(Integer idCuenta);
    
    void transferir(Integer idBanco, Integer idCuentaOrigen, Integer idCuentaDestino, BigDecimal monto);
}
