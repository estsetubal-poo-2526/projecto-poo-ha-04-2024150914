package org.frogi.model.entidades;

public class Grilo extends EntidadeJogo {

    private final int valor; // quantos grilos vale (normalmente 1)

    public Grilo(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
        this.valor = 1;
    }

    public Grilo(int posicaoX, int posicaoY, int valor) {
        super(posicaoX, posicaoY);
        this.valor = valor;
    }

    @Override
    public void interagir(Sapo sapo) {
        sapo.consumirGrilo(this.valor);
        // O grilo será removido do mapa depois da interação
    }

    public int getValor() {
        return valor;
    }
}