package org.rsacri.junit5app.ejemplos;

import jdk.jfr.Enabled;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.rsacri.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.rsacri.junit5app.ejemplos.models.Banco;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Ruben", new BigDecimal("1000.12345"));
        System.out.println(" Iniciando el metodo de prueba. ");
    }

    @AfterEach
    void tearDown() {
        System.out.println(" Finalizando el metodo de prueba.");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println(" Inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test ");
    }

    @Nested
    @DisplayName("probando atributos de la cuenta corriente")
    class CuentaTestNombreySaldo {
        @Test
        @DisplayName("el nombre")
        void testNombreCuenta() {

            //  cuenta.setPersona("Ruben");
            String esperado = "Ruben";
            String real = cuenta.getPersona();
            assertNotNull(real, () -> "la cuenta no puede ser un valor nulo");
            assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba, que es:  "
                    + esperado + " sin embargo fue: " + real);
            assertTrue(real.equals("Ruben"), () -> "nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        @DisplayName("el saldo, que no sea null, mayor que zero, y valor esperado.")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        //Test driven development TDD con JUnit
        @Test
        @DisplayName("Probando referencias que sean iguales con el método equals.")
        void testReferenciaCuenta() {
            cuenta = new Cuenta("Raul", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("Raul", new BigDecimal("8900.9997"));

            // assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);

        }

    }

    @Nested
    class CuentaOperacionesTest {
        @Test
        @DisplayName("Probando la cuenta debito.")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            //devuelve el string plano con el valor del saldo
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Probando la cuenta credito.")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            //devuelve el string plano con el valor del saldo
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Probando la transferencia de Dinero")
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Raul", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Ruben", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());

        }

    }


    @Test
    @DisplayName("Probando el Dinero Insuficiente")
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);

    }


    @Test
    @DisplayName("Probando la Relación entre las cuentas y el Banco con el método asserAll")
    void testRelacionBancoCuentas() {
        Cuenta cuenta1 = new Cuenta("Raul", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Ruben", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

        assertAll(() -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta2 no es el esperado."),
                () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta1 no es el esperado."),

                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> "el banco no tiene las cuentas esperadas."),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre(),
                        () -> "el nombre del banco no es el esperado."),
                () -> assertEquals("Ruben", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Ruben"))
                        .findFirst()
                        .get().getPersona()),

                () -> assertTrue(banco.getCuentas().stream()
                        .anyMatch(c -> c.getPersona().equals("Ruben"))));


    }

    //Nested indica que se trata de una clase que esta organizada
    @Nested
    class SistemaOperativoTest {
        //Ejemplo de pruebas exclusivas en diferentes S.0
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {

        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void SoloJKdk8() {

        }

        @Test
        @EnabledOnJre(JRE.JAVA_16)
        void soloJDK16() {

        }

    }

    @Nested
    class SistemProperties {

        //Habilitar o deshabilitar si existe cierta propiedad del sistema

        //Pirmero ver la propiedades del sistema
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + ":" + v));
        }

        // Ejemplo habilitar JDK 16
        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "16.0.2")
        void testJavaVersion() {

        }

        //Ejemplo desabilitar en arquitectura s.0 32BITS
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {

        }

        //Crear nuestras propias property del sistema
        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
        //Habilitar o Desabilitar según una variable de ambiente del S.0


    }

    @Nested
    class VariableAmbienteTest {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + " = " + v));

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-19.*")
        void testJavaHome() {
        }

        // Habilitar según el número de núcleos del procesador
        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

    }


    //Ejecución de test condicional con Assumptions
    @Test
    @DisplayName("test Saldo Cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("test Saldo Cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> {

            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });
    }

    //Ejemplo para Repetir un test varias veces y personalizar mensaje de info
    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value = 5, name = "Repeticion numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir() {
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        //devuelve el string plano con el valor del saldo
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    //Pruebas parametrizadas con @ParametizedTest
    @ParameterizedTest(name = "numero{index} ejecutando con valor {0} {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300","500","700","1000"})
    void testDebitoCuenta(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assumeTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);

    }



}