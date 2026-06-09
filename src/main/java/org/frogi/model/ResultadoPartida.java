package org.frogi.model;

public class ResultadoPartida {

    private int grilosApanhados;
    private int tempoDecorrido;
    private boolean venceu;
    private int nivelAlcancado;
    private Jogador jogador;
    private int pontosTotais;

    public ResultadoPartida(Jogador jogador, int grilosApanhados, int tempoDecorrido,
                            boolean venceu, int nivelAlcancado) {
        this.jogador = jogador;
        this.grilosApanhados = grilosApanhados;
        this.tempoDecorrido = tempoDecorrido;
        this.venceu = venceu;
        this.nivelAlcancado = nivelAlcancado;
        this.pontosTotais = calcularPontuacao();
    }

    public int calcularPontuacao() {
        int pontuacao = grilosApanhados * 10 + nivelAlcancado * 100;
        if (venceu) pontuacao += 500;
        return pontuacao;
    }

    // Getters
    public Jogador getJogador() { return jogador; }
    public int getGrilosApanhados() { return grilosApanhados; }
    public int getTempoDecorrido() { return tempoDecorrido; }
    public boolean isVenceu() { return venceu; }
    public int getNivelAlcancado() { return nivelAlcancado; }

    @Override
    public String toString() {
        return jogador.getNome() + " | Pontuação: " + calcularPontuacao() +
               " | Nível: " + nivelAlcancado + " | Grilos: " + grilosApanhados;
    }
}