package org.frogi.model;

import org.frogi.model.entidades.Sapo;

import java.time.Duration;
import java.time.Instant;

public class Partida {

    private boolean terminada;
    private boolean venceu;

    private Instant instanteInicial;
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
        this.instanteInicial = null;
        this.vidasRestantes = 3;
    }

    public void iniciarPartida() {
        terminada = false;
        venceu = false;
        this.instanteInicial = Instant.now();
        sapo.reviver();
    }

    public long getTempoDecorrido() {
        if (instanteInicial == null)
            return 0;

        return Duration.between(instanteInicial, Instant.now())
                .toSeconds();    }

    public int getGrilosApanhados() {
        return sapo.getGrilosConsumidos();
    }

    public void processarInteracoes() {
        nivelAtual.processarInteracoes(this);
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
        sapo.morrer();
        vidasRestantes--;
        int perda = sapo.getGrilosConsumidos() / 3;
        sapo.perderGrilos(perda);

        if (vidasRestantes <= 0) {
            reiniciarPartida();
        } else {
            // Respawn no início do nível
            sapo.setPosicao(1, 1);
            sapo.reviver();
        }
    }

    public void adicionarGrilo() {
        sapo.consumirGrilo();
    }

    public void removerGrilos(int grilos) {
        sapo.perderGrilos(grilos);
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
        sapo.perderGrilos(sapo.getGrilosConsumidos());
        instanteInicial = Instant.now();
        terminada = false;
        venceu = false;
        sapo.reviver();
       /*  nivelAtual = primeiro nível - a definir */
        sapo.setPosicao(1, 1);
    }

    public ResultadoPartida terminarPartida() {
        terminada = true;
        return new ResultadoPartida(
                jogador,
                getGrilosApanhados(),
                (int)getTempoDecorrido(),
                venceu,
                nivelAtual.getNumero());
    }

    // Getters
    public Sapo getSapo() { return sapo; }
    public int getXSapo() { return sapo.getPosicaoX(); }
    public int getYSapo() { return sapo.getPosicaoY(); }
    public Nivel getNivelAtual() { return nivelAtual; }
    public int getVidasRestantes() { return vidasRestantes; }
    public boolean isTerminada() { return terminada; }
    public boolean isVenceu() { return venceu; }

    public void setVenceu(boolean venceu) {
        this.venceu = venceu;
    }
}