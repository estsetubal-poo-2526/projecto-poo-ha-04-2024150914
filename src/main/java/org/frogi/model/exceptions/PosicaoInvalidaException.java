package org.frogi.model.exceptions;

public class PosicaoInvalidaException extends RuntimeException{
    public PosicaoInvalidaException(int x, int y) {
        super("A posição (" + x + ", " + y + ") está fora dos limites do mapa ou é inacessível.");
    }
}
