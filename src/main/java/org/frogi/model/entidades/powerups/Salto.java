package org.frogi.model.entidades.powerups;

import org.frogi.controller.SomController;
import org.frogi.model.Partida;

public class Salto extends PowerUp {

    public Salto(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        // Salto grande para a frente
        partida.moverSapo(3, 0); // 3 passos para a direita
        SomController.getInstance().tocarPowerUp();
    }

}