package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.powerups.PowerUp;
import org.frogi.model.powerups.VidaExtra;

public class Partida {

    private boolean terminada;
    private boolean venceu;

    private int tempoDecorrido;
    private int grilosApanhados;
    private int vidasRestantes;

    private Nivel nivelAtual;
    private final Jogador jogador;
    private final Sapo sapo;

    public Partida(Jogador jogador, Nivel nivelInicial) {
        this.jogador = jogador;
        this.nivelAtual = nivelInicial;
        this.sapo = new Sapo(1, 1); // posição inicial

        this.terminada = false;
        this.venceu = false;
        this.tempoDecorrido = 0;
        this.grilosApanhados = 0;
        this.vidasRestantes = 3;
    }

    public void iniciarPartida() {
        terminada = false;
        venceu = false;
        sapo.reviver();
    }

    public void atualizarTempo() {
        tempoDecorrido++;
    }

    public void processarInteracoes() {
        nivelAtual.processarInteracoes(sapo);
    }

    public void moverSapo(int deltaX, int deltaY) {
        if (!sapo.isVivo()) return;

        int novoX = sapo.getPosicaoX() + deltaX;
        int novoY = sapo.getPosicaoY() + deltaY;

        if (nivelAtual.isPosicaoValida(novoX, novoY)) {
            sapo.mover(deltaX, deltaY);
            processarInteracoes();
        }
    }

    public void perderVida() {
        vidasRestantes--;
        grilosApanhados = Math.max(0, grilosApanhados - (grilosApanhados / 3));

        if (vidasRestantes <= 0) {
            reiniciarPartida();
        } else {
            // Respawn no início do nível
            sapo.setPosicao(1, 1);
            sapo.reviver();
        }
    }

    public void adicionarGrilo() {
        grilosApanhados++;
    }

    public void adicionarVida() {
        vidasRestantes++;
    }

    public void reiniciarNivel() {
        sapo.setPosicao(1, 1);
        sapo.reviver();
    }

    public void reiniciarPartida() {
        vidasRestantes = 3;
        grilosApanhados = 0;
        tempoDecorrido = 0;
       /*  nivelAtual = primeiro nível - a definir */
        sapo.setPosicao(1, 1);
    }

    public ResultadoPartida terminarPartida() {
        terminada = true;
        return new ResultadoPartida(jogador, grilosApanhados, tempoDecorrido, venceu, nivelAtual.getNumero());
    }

    // Getters
    public Sapo getSapo() { return sapo; }
    public Nivel getNivelAtual() { return nivelAtual; }
    public int getVidasRestantes() { return vidasRestantes; }
    public int getGrilosApanhados() { return grilosApanhados; }
    public int getTempoDecorrido() { return tempoDecorrido; }
    public boolean isTerminada() { return terminada; }
    public boolean isVenceu() { return venceu; }

    public void setVenceu(boolean venceu) {
        this.venceu = venceu;
    }
}