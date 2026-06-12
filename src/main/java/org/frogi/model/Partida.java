package org.frogi.model;

import org.frogi.controller.SomController;
import org.frogi.model.entidades.Sapo;
import org.frogi.model.exceptions.PosicaoInvalidaException;

import java.time.Duration;
import java.time.Instant;

public class Partida {

    private static final int SPAWN_X = 0;
    private static final int SPAWN_Y = 1;

    private static final int FIM_X = 14;
    private static final int FIM_Y = 9;

    private boolean terminada;
    private boolean venceu;

    private Instant instanteInicial;
    private Instant instantePausa;
    private int vidasRestantes;

    private Nivel nivelAtual;
    private final Jogador jogador;
    private final Sapo sapo;

    public Partida(Jogador jogador, Nivel nivelInicial) {
        if (jogador == null) {
            throw new IllegalArgumentException("O jogador não pode ser nulo.");
        }
        if (nivelInicial == null) {
            throw new IllegalArgumentException("O nível inicial não pode ser nulo.");
        }

        this.jogador = jogador;
        this.nivelAtual = nivelInicial;
        this.sapo = new Sapo(SPAWN_X, SPAWN_Y);

        this.terminada = false;
        this.venceu = false;
        this.instanteInicial = null;
        this.vidasRestantes = 3;
    }

    public void iniciarPartida() {
        terminada = false;
        venceu = false;
        this.instanteInicial = Instant.now();

        if (!sapo.isVivo()) {
            sapo.reviver(SPAWN_X, SPAWN_Y);
        } else {
            sapo.setPosicao(SPAWN_X, SPAWN_Y);
        }
    }

    public long getTempoDecorrido() {
        if (instanteInicial == null)
            return 0;

        return Duration.between(instanteInicial, Instant.now()).toSeconds();
    }

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

        if (!nivelAtual.isPosicaoValida(novoX, novoY)) {
            throw new PosicaoInvalidaException(novoX, novoY);
        }
        sapo.mover(deltaX, deltaY);
        processarInteracoes();

        // Se o sapo morreu ou venceu na colisão de interações, não é preciso de validar o rio
        if (!sapo.isVivo() || venceu) return;

        Mapa mapa = nivelAtual.getMapa();

        // Verificar se o sapo caiu no rio
        if (mapa.getColunasRio().contains(novoX)) {

            boolean estaEmCimaDeNenufar = false;

            for (int[] par : mapa.getCoordenadasNenufares()) {
                if (par[0] == novoX && par[1] == novoY) {
                    estaEmCimaDeNenufar = true;
                    break;
                }
            }

            if (!estaEmCimaDeNenufar) {
                perderVida();
            }
        }
    }

    public void perderVida() {
        sapo.morrer();
        vidasRestantes--;
        int perda = sapo.getGrilosConsumidos() / 3;
        sapo.perderGrilos(perda);

        if (vidasRestantes > 0) {
            sapo.reviver(SPAWN_X, SPAWN_Y);
        }
        SomController.getInstance().tocarMorte();
    }

    public void adicionarGrilo() {
        sapo.consumirGrilo();
        SomController.getInstance().tocarComerGrilo();
    }

    public void removerGrilos(int grilos) {
        sapo.perderGrilos(grilos);
    }

    public void adicionarVida() {
        vidasRestantes++;
        SomController.getInstance().tocarPowerUp();
    }

    public void avancarParaProximoNivel(Nivel novoNivel) {
        if (novoNivel == null) {
            throw new IllegalArgumentException("O próximo nível não pode ser nulo.");
        }
        this.nivelAtual = novoNivel;
        sapo.setPosicao(SPAWN_X, SPAWN_Y);
    }

    public boolean isNivelCompleto(){
        return sapo.getPosicaoX() == FIM_X && sapo.getPosicaoY() == FIM_Y;
    }

    public void reiniciarPartida() {
        vidasRestantes = 3;
        sapo.perderGrilos(sapo.getGrilosConsumidos());
        instanteInicial = Instant.now();
        terminada = false;
        venceu = false;
        if (!sapo.isVivo()) {
            sapo.reviver(SPAWN_X, SPAWN_Y);
        } else {
            sapo.setPosicao(SPAWN_X, SPAWN_Y);
        }
    }

    public ResultadoPartida terminarPartida() {
        terminada = true;
        return new ResultadoPartida(
                jogador,
                getGrilosApanhados(),
                (int)getTempoDecorrido());
    }

    public void registarInicioPausa() {
        this.instantePausa = Instant.now();
    }

    public void ajustarTempoPosPausa() {
        if (this.instantePausa != null && this.instanteInicial != null) {
            // Calcula quantos segundos o jogo esteve em pausa
            long segundosEmPausa = Duration.between(this.instantePausa, Instant.now()).toSeconds();
            // Empurra o instante inicial para a frente para ignorar o tempo de pausa
            this.instanteInicial = this.instanteInicial.plusSeconds(segundosEmPausa);
            this.instantePausa = null; // Reseta para a próxima pausa
        }
    }

    public void setNivel(Nivel novoNivel){
        if (novoNivel == null) {
            throw new IllegalArgumentException("O nível não pode ser nulo.");
        }
        this.nivelAtual = novoNivel;
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