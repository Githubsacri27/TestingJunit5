package org.rsacri.junit5app.ejemplos.exceptions;

public class DineroInsuficienteException extends RuntimeException {
    //constructor para personalizar el mensaje
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
