package org.frogi.model.entidades;

import org.frogi.model.FaseSapo;

public class Sapo extends EntidadeJogo {

    private FaseSapo faseAtual;
    private int grilosConsumidos;
    private boolean vivo;

    public Sapo(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
        this.faseAtual = FaseSapo.PEQUENO;
        this.grilosConsumidos = 0;
        this.vivo = true;
    }

    public void mover(int deltaX, int deltaY) {
        this.posicaoX += deltaX;
        this.posicaoY += deltaY;
    }

    public void consumirGrilo(int quantidade) {
        this.grilosConsumidos += quantidade;
        evoluir();
    }

    private void evoluir() {
        if (grilosConsumidos >= 15) {
            faseAtual = FaseSapo.GRANDE;
        } else if (grilosConsumidos >= 7) {
            faseAtual = FaseSapo.MEDIO;
        }
    }

    public void morrer() {
        this.vivo = false;
    }

    public void reviver() {
        this.vivo = true;
    }

    public void setPosicao(int x, int y) {
        this.posicaoX = x;
        this.posicaoY = y;
    }

    /**
     * Implementação obrigatória do método abstrato
     */
    @Override
    public void interagir(Sapo sapo) {
        // O sapo não interage consigo mesmo
    }

    // Getters
    public FaseSapo getFaseAtual() { return faseAtual; }
    public int getGrilosConsumidos() { return grilosConsumidos; }
    public boolean isVivo() { return vivo; }
}