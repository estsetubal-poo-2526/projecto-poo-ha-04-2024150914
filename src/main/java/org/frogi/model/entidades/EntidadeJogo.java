package org.frogi.model.entidades;

import org.frogi.model.Partida;
import org.frogi.model.exceptions.PosicaoInvalidaException;

public abstract class EntidadeJogo {

    private int posicaoX;
    private int posicaoY;

    public EntidadeJogo(int posicaoX, int posicaoY) {
        if (posicaoX < 0 || posicaoY < 0) {
            throw new IllegalArgumentException(
                    "As coordenadas de uma entidade não podem ser negativas!"
            );
        }
        this.posicaoX = posicaoX;
        this.posicaoY = posicaoY;
    }

    public int getPosicaoX() {
        return posicaoX;
    }

    public int getPosicaoY() {
        return posicaoY;
    }

    public void setPosicao(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException(
                    "Não é permitido mover uma entidade para coordenadas negativas!"
            );
        }
        this.posicaoX = x;
        this.posicaoY = y;
    }

    public abstract void interagir(Partida partida);

    @Override
    public String toString() {
        return "(" + posicaoX + ", " + posicaoY + ")";
    }
}
