package org.rsacri.junit5app.ejemplos;

import org.rsacri.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.rsacri.junit5app.ejemplos.models.Banco;

import java.math.BigDecimal;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.saldo = saldo;
        this.persona = persona;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    // Sino hay dinero suficiente lanza excepcion
    public void debito(BigDecimal monto) {
        BigDecimal nuevosaldo = this.saldo.subtract(monto);
        if (nuevosaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new DineroInsuficienteException("Dinero Insuficiente");
        }
        //si hay dinero se actualiza
        this.saldo = nuevosaldo;

    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);

    }


    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta c = (Cuenta) obj;
        if (this.persona == null || this.saldo == null) {
            return false;
        }
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());

    }
}