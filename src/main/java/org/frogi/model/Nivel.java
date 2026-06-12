package org.frogi.model;

public class Nivel {

    private final int numero;
    private final Mapa mapa;
    private final String nome;

    public Nivel(int numero, Mapa mapa, String nome) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número do nível deve ser maior do que zero.");
        }
        if (mapa == null) {
            throw new IllegalArgumentException("O mapa associado ao nível não pode ser nulo.");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do nível não pode ser nulo ou vazio.");
        }

        this.numero = numero;
        this.mapa = mapa;
        this.nome = nome;
    }

    public Nivel(int numero, Mapa mapa) {
        this(numero, mapa, "Nível " + numero);
    }

    public void processarInteracoes(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("A partida não pode ser nula para processar as interações do nível.");
        }
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