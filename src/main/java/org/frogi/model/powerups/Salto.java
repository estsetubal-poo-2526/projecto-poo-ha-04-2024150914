package org.frogi.model.powerups;

import org.frogi.model.entidades.Sapo;

public class Salto extends PowerUp {

    public Salto(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Sapo sapo) {
        // Salto grande para frente (exemplo)
        sapo.mover(0, -3); // move 3 casas para cima (ajusta conforme tua orientação)
        System.out.println("Salto ativado!");
    }
}