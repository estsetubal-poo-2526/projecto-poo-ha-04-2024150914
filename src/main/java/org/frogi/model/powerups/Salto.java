package org.frogi.model.powerups;

import org.frogi.model.Partida;
import org.frogi.model.entidades.Sapo;

public class Salto extends PowerUp {

    public Salto(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        // Salto grande para frente
        partida.moverSapo(3, 0); // move 3 casas para cima (ajustar conforme orientação)
        System.out.println("Salto ativado!");
    }

}