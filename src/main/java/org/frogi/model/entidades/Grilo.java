package org.frogi.model.entidades;

import org.frogi.model.Partida;

public class Grilo extends EntidadeJogo {

    public Grilo(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("A partida não pode ser nula.");
        }
        partida.adicionarGrilo();
    }
}
