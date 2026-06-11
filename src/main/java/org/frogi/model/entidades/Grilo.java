package org.frogi.model.entidades;

import org.frogi.model.Partida;

public class Grilo extends EntidadeJogo {

    public Grilo(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        partida.adicionarGrilo();
    }
}
