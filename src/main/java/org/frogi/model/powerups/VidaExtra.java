package org.frogi.model.powerups;

import org.frogi.controller.GestorSom;
import org.frogi.model.entidades.Sapo;
import org.frogi.model.Partida;

public class VidaExtra extends PowerUp {

    public VidaExtra(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        partida.adicionarVida();
        GestorSom.getInstance().tocarPowerUp();
    }
}