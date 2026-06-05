package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.powerups.PowerUp;

public class Nivel {

    private final int numero;
    private final Mapa mapa;
    private final String nome; // opcional: "Nível 1 - Pântano Inicial"

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

    public void processarInteracoes(Sapo sapo) {
        mapa.processarInteracoes(sapo);
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

    public boolean verificarVitoria(Sapo sapo) {
        // Será implementado quando a Princesa estiver bem integrada
        return false;
    }
}