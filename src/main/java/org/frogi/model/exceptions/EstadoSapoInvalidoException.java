package org.frogi.model.exceptions;

public class EstadoSapoInvalidoException extends IllegalStateException {
    public EstadoSapoInvalidoException(String mensagem) {
        super(mensagem);
    }
}