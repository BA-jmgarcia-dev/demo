package com.example.demo.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Banco {
    
    private Long id;
    private String nombre;
    private List<Cuenta> cuentas;
    private int totalTransferencias;

    public Banco(Long id, String nombre, int totalTransferencias){
        this.id = id;
        this.nombre = nombre;
        this.totalTransferencias = totalTransferencias;
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
