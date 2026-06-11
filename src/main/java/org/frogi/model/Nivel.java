package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.powerups.PowerUp;

public class Nivel {

    private final int numero;
    private final Mapa mapa;
    private final String nome;

    public Nivel(int numero, Mapa mapa) {
        this.numero = numero;
        this.mapa = mapa;
        this.nome = "Nível " + numero;
    }

    public Nivel(int numero, Mapa mapa, String nome) {
        this.numero = numero;
        this.mapa = mapa;
        this.nome = nome;
    }

    public void processarInteracoes(Partida partida) {
        mapa.processarInteracoes(partida);
    }

    public boolean isPosicaoValida(int x, int y) {
        return mapa.isPosicaoValida(x, y);
    }

    // Getters
    public int getNumero() {
        return numero;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public String getNome() {
        return nome;
    }
}