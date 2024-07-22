package com.example.demo.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Banco {
    
    private String nombre;
    private List<Cuenta> cuentas;

    public Banco(){
        this.cuentas = new ArrayList<>();
    }

    public void transferencia(Cuenta origen, Cuenta destino, BigDecimal monto){
        origen.debito(monto);
        destino.credito(monto);
    }

    public void addCuenta(Cuenta cuenta){
        this.cuentas.add(cuenta);
        cuenta.setBanco(this);
    }
}
