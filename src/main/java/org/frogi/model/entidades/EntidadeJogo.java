package org.frogi.model.entidades;

public abstract class EntidadeJogo {

    protected int posicaoX;
    protected int posicaoY;

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

    public abstract void interagir(Sapo sapo);
}
