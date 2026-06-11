package org.frogi.model;

public class ResultadoPartida {

    private int grilosApanhados;
    private int tempoDecorrido;
    private Jogador jogador;

    public ResultadoPartida(Jogador jogador, int grilosApanhados, int tempoDecorrido) {
        this.jogador = jogador;
        this.grilosApanhados = grilosApanhados;
        this.tempoDecorrido = tempoDecorrido;
    }

    public int calcularPontuacao() {
        int pontuacao = grilosApanhados * 10 - tempoDecorrido;
        return pontuacao;
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