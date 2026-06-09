package org.frogi.model.entidades;

import org.frogi.model.Partida;

public abstract class EntidadeJogo {

    private int posicaoX;
    private int posicaoY;

    public EntidadeJogo(int posicaoX, int posicaoY) {
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
        this.posicaoX = x;
        this.posicaoY = y;
    }

    public abstract void interagir(Partida partida);

    @Override
    public String toString() {
        return "(" + posicaoX + ", " + posicaoY + ")";
    }
}
