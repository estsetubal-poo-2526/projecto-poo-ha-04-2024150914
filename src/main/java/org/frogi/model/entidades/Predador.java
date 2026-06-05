package org.frogi.model.entidades;

public class Predador extends EntidadeJogo {

    private final int velocidade; // para futuro movimento automático

    public Predador(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
        this.velocidade = 1;
    }

    @Override
    public void interagir(Sapo sapo) {
        if (sapo.isVivo()) {
            sapo.morrer();
        }
    }

    public void moverAutomatico() {
        // TODO: lógica de IA simples (ex: mover em direção ao sapo)
    }

    public int getVelocidade() {
        return velocidade;
    }
}