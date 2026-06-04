package org.frogi.model.entidades;

import org.frogi.model.FaseSapo;

public class Sapo extends EntidadeJogo{
    private FaseSapo faseAtual;
    private boolean vivo;

    public Sapo(int posicaoX, int posicaoY, FaseSapo faseAtual, boolean vivo){
        super(posicaoX, posicaoY);

        this.faseAtual = faseAtual;
        this.vivo = vivo;
    }

    public void mover(int x, int y){
        this.posicaoX += x;
        this.posicaoY += y;
    }

    public void evoluir(){
        //switch(this.faseAtual)
    }
}
