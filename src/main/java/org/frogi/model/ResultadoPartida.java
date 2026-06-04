package org.frogi.model;

public class ResultadoPartida {

    private int grilosApanhados;
    private int tempoDecorrido;
    private boolean venceu;
    private int nivelAlcancado;
    private Jogador jogador;

    public ResultadoPartida(Jogador jogador, int grilosApanhados, int tempoDecorrido,
                            boolean venceu, int nivelAlcancado) {
        this.jogador = jogador;
        this.grilosApanhados = grilosApanhados;
        this.tempoDecorrido = tempoDecorrido;
        this.venceu = venceu;
        this.nivelAlcancado = nivelAlcancado;
    }

    public int calcularPontuacao() {

        int pontuacao = grilosApanhados * 10;

        if (venceu) {
            pontuacao += 500;
        }

        pontuacao += nivelAlcancado * 100;

        return pontuacao;
    }

    public int getGrilosApanhados() {
        return grilosApanhados;
    }

    public int getTempoDecorrido() {
        return tempoDecorrido;
    }

    public boolean isVenceu() {
        return venceu;
    }

    public int getNivelAlcancado() {
        return nivelAlcancado;
    }

    @Override
    public String toString() {
        return jogador.getNome() +
                " | Pontuação: " + calcularPontuacao() +
                " | Nível: " + nivelAlcancado;
    }
}