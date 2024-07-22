package com.example.demo.exceptions;

public class DineroInsuficienteException extends RuntimeException {
    public DineroInsuficienteException(String msg){
        super(msg);
    }
}
