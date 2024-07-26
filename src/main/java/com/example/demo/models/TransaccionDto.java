package com.example.demo.models;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransaccionDto {
    
    private Integer idBanco;
    private Integer cuentaOrigenId;
    private Integer cuentaDestinoId;
    private BigDecimal monto;

}
