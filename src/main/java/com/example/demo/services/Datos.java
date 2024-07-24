package com.example.demo.services;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;
import com.example.demo.models.Examen;

public class Datos {

    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(1L, "math"),
            new Examen(2L, "Language"),
            new Examen(3L, "History"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmética","integrales",
            "derivadas", "trigonometría", "geometría");
    public final static Examen EXAMEN = new Examen(4L, "Fisica");

    public static final Cuenta CUENTA_001 = new Cuenta("Juan", new BigDecimal("200"));

    public static final Cuenta CUENTA_002 = new Cuenta("Jose", new BigDecimal("300"));

    public static final Banco BANCO = new Banco(1L,"Atlantida", 0);


}
