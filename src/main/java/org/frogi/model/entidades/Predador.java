package org.frogi.model.entidades;

import org.frogi.model.Partida;

public class Predador extends EntidadeJogo {

    private final int velocidade; // para movimento automático

    public Predador(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
        this.velocidade = 1;
    }

    @Override
    public void interagir(Partida partida) {
        partida.perderVida();
    }

    public void moverAutomatico() {
        // lógica de IA simples (ex: mover em direção ao sapo)
    }

    public int getVelocidade() {
        return velocidade;
    }
}