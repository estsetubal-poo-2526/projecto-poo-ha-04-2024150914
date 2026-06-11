package org.frogi.model.entidades;

import org.frogi.model.Partida;

public class Princesa extends EntidadeJogo {

    public Princesa(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        // Vitória
        partida.setVenceu(true);
    }
}