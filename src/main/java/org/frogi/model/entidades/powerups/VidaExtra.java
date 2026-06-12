package org.frogi.model.entidades.powerups;

import org.frogi.controller.SomController;
import org.frogi.model.Partida;

public class VidaExtra extends PowerUp {

    public VidaExtra(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        partida.adicionarVida();
        SomController.getInstance().tocarPowerUp();
    }
}