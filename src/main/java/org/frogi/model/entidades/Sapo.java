package org.frogi.model.entidades;

import org.frogi.model.FaseSapo;
import org.frogi.model.Partida;
import org.frogi.model.exceptions.EstadoSapoInvalidoException;


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
        if (!this.vivo) {
            throw new EstadoSapoInvalidoException("Não é possível mover um sapo que está morto.");
        }
        setPosicao(
                getPosicaoX() + deltaX,
                getPosicaoY() + deltaY
        );
    }

    public void consumirGrilo() {
        if (!this.vivo) {
            throw new EstadoSapoInvalidoException("Um sapo morto não pode consumir grilos!");
        }
        this.grilosConsumidos++;
        evoluir();
    }

    public void perderGrilos(int quantidade){
        if(quantidade < 0)
            throw new IllegalArgumentException("A quantidade de grilos a perder não pode ser negativa!");
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
        if(this.vivo) {
            this.vivo = false;
        }else{
            throw new EstadoSapoInvalidoException("O sapo já está morto!");
        }
    }

    public void reviver(int x, int y) {
        if (this.vivo) {
            throw new EstadoSapoInvalidoException("O sapo já se encontra vivo.");
        }
        this.vivo = true;
        setPosicao(x,y);
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