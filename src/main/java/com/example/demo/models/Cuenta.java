package com.example.demo.models;

import java.math.BigDecimal;

import com.example.demo.exceptions.DineroInsuficienteException;

import lombok.Data;

@Data
public class Cuenta {
    
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo){
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto){
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new DineroInsuficienteException("Dinero insuficiente en cuenta");
        }
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }
}
