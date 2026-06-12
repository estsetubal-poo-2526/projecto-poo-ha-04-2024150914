package org.frogi.model.exceptions;

public class NomeInvalidoException extends IllegalArgumentException {
    public NomeInvalidoException(String mensagem) {
        super(mensagem);
    }
}
