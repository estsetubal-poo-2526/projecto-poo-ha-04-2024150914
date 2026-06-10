package org.frogi.model.entidades;

import org.frogi.model.FaseSapo;
import org.frogi.model.Partida;



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
        setPosicao(
                getPosicaoX() + deltaX,
                getPosicaoY() + deltaY
        );
    }

    public void consumirGrilo() {
        this.grilosConsumidos++;
        evoluir();
    }

    public void perderGrilos(int quantidade){
        if(quantidade < 0)
            throw new IllegalArgumentException("A quantidade é inválida!");
        grilosConsumidos = Math.max(0, grilosConsumidos - quantidade);
        evoluir();
    }

    private void evoluir() {
        if (grilosConsumidos >= 15) {
            faseAtual = FaseSapo.GRANDE;
        } else if (grilosConsumidos >= 7) {
            faseAtual = FaseSapo.MEDIO;
        } else {
            faseAtual = FaseSapo.PEQUENO;
        }
    }

    public void morrer() {
        this.vivo = false;
    }

    public void reviver() {
        this.vivo = true;
        setPosicao(0,1);
    }


    @Override
    public void interagir(Partida partida) {
        // O sapo não interage consigo mesmo
    }

    // Getters
    public FaseSapo getFaseAtual() { return faseAtual; }
    public int getGrilosConsumidos() { return grilosConsumidos; }
    public boolean isVivo() { return vivo; }
}