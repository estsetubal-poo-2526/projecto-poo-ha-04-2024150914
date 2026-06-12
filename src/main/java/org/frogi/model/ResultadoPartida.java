package org.frogi.model;

public class ResultadoPartida {

    private int grilosApanhados;
    private int tempoDecorrido;
    private Jogador jogador;

    public ResultadoPartida(Jogador jogador, int grilosApanhados, int tempoDecorrido) {
        if (jogador == null) {
            throw new IllegalArgumentException("O jogador associado ao resultado não pode ser nulo.");
        }
        if (grilosApanhados < 0) {
            throw new IllegalArgumentException("A quantidade de grilos apanhados não pode ser negativa.");
        }
        if (tempoDecorrido < 0) {
            throw new IllegalArgumentException("O tempo decorrido de partida não pode ser negativo.");
        }

        this.jogador = jogador;
        this.grilosApanhados = grilosApanhados;
        this.tempoDecorrido = tempoDecorrido;
    }

    public int calcularPontuacao() {
        int pontuacao = grilosApanhados * 10 - tempoDecorrido;
        return Math.max(0, pontuacao);
    }

    // Getters
    public Jogador getJogador() { return jogador; }
    public int getGrilosApanhados() { return grilosApanhados; }
    public int getTempoDecorrido() { return tempoDecorrido; }

    @Override
    public String toString() {
        return jogador.getNome() + " | Pontuação: " + calcularPontuacao() + " | Grilos: " + grilosApanhados;
    }
}