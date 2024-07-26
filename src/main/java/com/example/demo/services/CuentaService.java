package com.example.demo.services;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.models.Cuenta;

public interface CuentaService {
    
    Cuenta findById(Integer id);

    Cuenta createCuenta(Cuenta cuenta);

    List<Cuenta> findAll();
    
    int revisarTotalTransferencia(Integer idBanco);
    
    BigDecimal revisarSaldo(Integer idCuenta);
    
    void transferir(Integer idBanco, Integer idCuentaOrigen, Integer idCuentaDestino, BigDecimal monto);
}
