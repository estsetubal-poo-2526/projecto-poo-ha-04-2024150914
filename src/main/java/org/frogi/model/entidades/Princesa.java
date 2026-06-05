package org.frogi.model.entidades;

public class Princesa extends EntidadeJogo {

    public Princesa(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Sapo sapo) {
        // Vitória!
        // A Partida deve ser notificada disto
    }
}