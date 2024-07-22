package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;

public class CuentaTest {
    
    @Test
    @DisplayName("Prueba nombre de la cuenta!")
    void testNombrePrueba(){
        Cuenta cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        String esperado = "Juan";
        String real = cuenta.getPersona();
        assertEquals(esperado, real);
    }

    @Test
    void testSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        assertEquals(cuenta.getSaldo(), new BigDecimal(100.21));
    }

    @Test
    void referenciaCuenta(){
        Cuenta cuenta1 = new Cuenta("Juan", new BigDecimal(100.21));
        Cuenta cuenta2 = new Cuenta("Juan", new BigDecimal(100.21));

        assertEquals(cuenta2, cuenta1);
    }

    @Test
    void testDebito(){
        Cuenta cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        cuenta.debito(new BigDecimal(10));
        assertNotNull(cuenta.getSaldo(), ()->"el valor no debe ser nulo");
        assertEquals(cuenta.getSaldo().intValue(), 90, ()->"El valor actual no es igual");
    }

    
    @Test
    void testCredito(){
        Cuenta cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        cuenta.credito(new BigDecimal(10));
        assertNotNull(cuenta.getSaldo());
        assertEquals(cuenta.getSaldo().intValue(), 110);
    }

    @Test
    void testDineroInsuficiente(){
        Cuenta cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        Exception exception = assertThrows(Exception.class, () -> {
            cuenta.debito(new BigDecimal(1000));
        });

        String resultado = exception.getMessage();
        String esperado = "Dinero insuficiente en cuenta";
        assertEquals(resultado, esperado);
    }

    @Test
    void testTransferenciaDinero(){
        Cuenta cuentaOrigen = new Cuenta("Juan", new BigDecimal(1000));
        Cuenta cuentaDestino = new Cuenta("Roberto", new BigDecimal(2000));

        Banco banco = new Banco();
        banco.setNombre("Atlantida");
        banco.transferencia(cuentaOrigen, cuentaDestino, new BigDecimal(100));

        assertEquals(cuentaOrigen.getSaldo().toPlainString(), "900");
        assertEquals(cuentaDestino.getSaldo().toPlainString(), "2100");
    }

    @Test
    void testRelacionCuentasBanco(){
        Cuenta cuentaOrigen = new Cuenta("Juan", new BigDecimal(1000));
        Cuenta cuentaDestino = new Cuenta("Roberto", new BigDecimal(2000));

        Banco banco = new Banco();
        banco.addCuenta(cuentaDestino);
        banco.addCuenta(cuentaOrigen);
        banco.setNombre("Atlantida");
        banco.transferencia(cuentaOrigen, cuentaDestino, new BigDecimal(100));

        assertAll(
            () -> assertEquals(cuentaOrigen.getSaldo().toPlainString(), "900"),
            () -> assertEquals(cuentaDestino.getSaldo().toPlainString(), "2100"),
            () -> assertEquals(2, banco.getCuentas().size()),
            () -> assertEquals(cuentaDestino.getBanco().getNombre(), "Atlantida"),
            () -> assertEquals(cuentaDestino.getPersona(), banco.getCuentas().stream()
                .filter(c -> c.getPersona().equals("Roberto"))
                .findFirst()
                .get().getPersona()),
            () -> assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Juan")))    
        );
    }
}


