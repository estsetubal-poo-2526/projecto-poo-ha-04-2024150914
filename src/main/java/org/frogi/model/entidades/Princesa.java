package org.frogi.model.entidades;

import org.frogi.model.Partida;

public class Princesa extends EntidadeJogo {

    public Princesa(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        // Vitória
        if (partida == null) {
            throw new IllegalArgumentException("A partida não pode ser nula.");
        }
        partida.setVenceu(true);
    }
}