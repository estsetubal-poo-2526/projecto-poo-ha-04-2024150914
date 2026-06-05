package org.frogi.model.powerups;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.Partida;

public class VidaExtra extends PowerUp {

    public VidaExtra(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Sapo sapo) {
        // Partida é que deve gerir vidas
        System.out.println("Vida Extra coletada!");
    }

    // Método para ser chamado pela Partida
    public void aplicarEfeito(Partida partida) {
        partida.adicionarVida();
    }
}