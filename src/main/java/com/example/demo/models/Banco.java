package com.example.demo.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name ="banco")
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Column(name = "total_transferencias")
    private int totalTransferencias;

    public Banco(Long id, String nombre, int totalTransferencias){
        this.id = id;
        this.nombre = nombre;
        this.totalTransferencias = totalTransferencias;
    }

    public void transferencia(Cuenta origen, Cuenta destino, BigDecimal monto){
        origen.debito(monto);
        destino.credito(monto);
    }

}
