package org.frogi.model.powerups;

import org.frogi.model.entidades.EntidadeJogo;
import org.frogi.model.entidades.Sapo;

public abstract class PowerUp extends EntidadeJogo {

    public PowerUp(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public abstract void interagir(Sapo sapo);
}