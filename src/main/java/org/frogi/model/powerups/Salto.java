package org.frogi.model.powerups;

import org.frogi.model.entidades.Sapo;

public class Salto extends PowerUp {

    public Salto(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Sapo sapo) {
        // Avança automaticamente uma secção do nível
        // Exemplo: mover sapo para frente grande distância
    }
}