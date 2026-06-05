package org.frogi.model;

public class Partida {

    private boolean terminada;
    private boolean venceu;

    private int tempoDecorrido;
    private int grilosApanhados;
    private int vidasRestantes;

    private Nivel nivelAtual;
    private Jogador jogador;

    public Partida(Jogador jogador, Nivel nivelInicial) {
        this.jogador = jogador;
        this.nivelAtual = nivelInicial;

        this.terminada = false;
        this.venceu = false;

        this.tempoDecorrido = 0;
        this.grilosApanhados = 0;
        this.vidasRestantes = 3;
    }

    public void iniciarPartida() {
        terminada = false;
        venceu = false;
    }

    public void atualizarPartida() {
        tempoDecorrido++;
    }

    public boolean verificarVitoria() {
        return venceu;
    }

    public boolean verificarDerrota() {
        return vidasRestantes <= 0;
    }

    public void avancarNivel(Nivel proximoNivel) {
        this.nivelAtual = proximoNivel;
    }

    public void reiniciarNivel() {
        // TODO: Implementar quando o Sapo existir
    }

    public void reiniciarPartida() {
        vidasRestantes = 3;
        grilosApanhados = 0;
        tempoDecorrido = 0;
    }

    public void adicionarGrilo() {
        grilosApanhados++;
    }

    public void adicionarVida() {
        vidasRestantes++;
    }

    public void perderVida() {

        vidasRestantes--;

        grilosApanhados -= grilosApanhados / 3;

        if (vidasRestantes <= 0) {
            reiniciarPartida();
        }
    }

    public ResultadoPartida terminarPartida() {
    terminada = true;
    return new ResultadoPartida(
        jogador,
        grilosApanhados,
        tempoDecorrido,
        venceu,
        nivelAtual.getNumero()   
    );
}

    // GETTERS

    public boolean isTerminada() {
        return terminada;
    }

    public boolean isVenceu() {
        return venceu;
    }

    public int getTempoDecorrido() {
        return tempoDecorrido;
    }

    public int getGrilosApanhados() {
        return grilosApanhados;
    }

    public int getVidasRestantes() {
        return vidasRestantes;
    }

    public Nivel getNivelAtual() {
        return nivelAtual;
    }

    public Jogador getJogador() {
        return jogador;
    }

    // SETTERS

    public void setVenceu(boolean venceu) {
        this.venceu = venceu;
    }

    public void setNivelAtual(Nivel nivelAtual) {
        this.nivelAtual = nivelAtual;
    }
}