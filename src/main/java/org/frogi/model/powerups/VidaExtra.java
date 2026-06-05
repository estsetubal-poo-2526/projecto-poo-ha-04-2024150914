package org.frogi.model.powerups;

import org.frogi.model.entidades.Sapo;

public class VidaExtra extends PowerUp {

    public VidaExtra(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Sapo sapo) {
        // A Partida deve ganhar +1 vida
        // Este método pode ser expandido ou a Partida escuta a interação
    }
}