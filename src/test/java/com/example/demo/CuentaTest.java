package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.demo.models.Banco;
import com.example.demo.models.Cuenta;

// @TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaTest {
    
    Cuenta cuenta;

    @BeforeEach
    void initMethodTest(){
        this.cuenta = new Cuenta("Juan", new BigDecimal(100.21));
        System.out.println("iniciando el metodo");
    }

    @AfterEach
    void tearDown(){
        System.out.println("finalizando el metodo de pruebas");
    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("inicializando el test");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("finalizando el test");
    }

    @Nested
    @DisplayName("test atributos de cuenta")
    class nombreCuentaTest {
        @Test
        @DisplayName("Prueba nombre de la cuenta!")
        void testNombrePrueba(){
            String esperado = "Juan";
            String real = cuenta.getPersona();
            assertEquals(esperado, real);
        }
    
        @Test
        void testSaldoCuenta(){
            assertEquals(cuenta.getSaldo(), new BigDecimal(100.21));
        }
    
        @Test
        void referenciaCuenta(){
            Cuenta cuenta1 = new Cuenta("Juan", new BigDecimal(100.21));
            Cuenta cuenta2 = new Cuenta("Juan", new BigDecimal(100.21));
    
            assertEquals(cuenta2, cuenta1);
        }
    }

    @Nested
    class transactionTest {
        @ParameterizedTest
        @ValueSource(strings = {"100","10","20"})
        void testDebito(String monto){
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo(), ()->"el valor no debe ser nulo");
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }
    
        
        @Test
        void testCredito(){
            cuenta.credito(new BigDecimal(10));
            assertNotNull(cuenta.getSaldo());
            assertEquals(cuenta.getSaldo().intValue(), 110);
        }
    
        @Test
        void testDineroInsuficiente(){
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
            banco.setNombre("Atlantida");
            banco.transferencia(cuentaOrigen, cuentaDestino, new BigDecimal(100));
    
            assertAll(
                () -> assertEquals(cuentaOrigen.getSaldo().toPlainString(), "900"),
                () -> assertEquals(cuentaDestino.getSaldo().toPlainString(), "2100")   
            );
        }
    }


    @Nested
    class SystemaOperativoTest{
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testWindows(){}
    
        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        void testOnMacAndLinux(){}
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void justJDK17(){}
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void printVariablesEnv(){
            Map<String, String> getEnv = System.getenv();
            getEnv.forEach((k, v) -> System.out.println(k + " = " + v));
        }
    
        @Test
        void testSaldoCuentaDev(){
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev);
            assertNotNull(cuenta.getSaldo());
            assertEquals(cuenta.getSaldo(), new BigDecimal(100.21));
        }
    
        @Test
        void testSaldoCuentaDevAssuming(){
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, ()-> {
                assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)> 0);
                assertEquals(cuenta.getSaldo(), new BigDecimal(100.21));
            });
            assertNotNull(cuenta.getSaldo());
        }
    }

    @Nested
    @Tag("timeout")
    class ejemploTimeOutTest{
        @Test
        @Timeout(5)
        void pruebaTimeOut() throws InterruptedException {
            TimeUnit.SECONDS.sleep(4);
        }

        @Test
        @Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeOut2() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}


