package org.frogi.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.frogi.model.*;
import org.frogi.model.entidades.*;
import org.frogi.model.exceptions.PosicaoInvalidaException;
import org.frogi.model.entidades.powerups.Salto;
import org.frogi.model.entidades.powerups.VidaExtra;
import org.frogi.view.GameOverScreen;
import org.frogi.view.JogoScreen;
import org.frogi.view.VitoriaScreen;

import java.util.List;

public class JogoController {

    private final Leaderboard leaderboard;

    private final Stage primaryStage;
    private final Jogador jogador;
    private final Scene cenaPrincipal;
    private final Runnable aoTerminarJogo;
    private final Runnable irMenu;

    private Partida partida;
    private JogoScreen janelaJogo;
    private Timeline gameLoop;

    public JogoController(Stage primaryStage, Scene cenaPrincipal, Jogador jogador, Leaderboard leaderboard, Runnable aoTerminarJogo, Runnable irMenu) {
        this.primaryStage = primaryStage;
        this.cenaPrincipal = cenaPrincipal;
        this.jogador = jogador;
        this.leaderboard = leaderboard;
        this.aoTerminarJogo = aoTerminarJogo;
        this.irMenu = irMenu;
    }

    public void iniciar() {
        this.partida = new Partida(jogador, criarNivel1());
        this.partida.iniciarPartida();

        // Cria a View
        this.janelaJogo = new JogoScreen(partida);

        // Altera a Scene para mostrar o jogo
        cenaPrincipal.setRoot(janelaJogo.getContentorPrincipal());

        configurarControlos();
        configurarGameLoop();

        primaryStage.setTitle("Frogi - " + partida.getNivelAtual().getNome());
    }

    public void tentarNovamente() {
        this.partida.reiniciarPartida();
        this.partida.setNivel(criarNivel1()); // Garante que volta ao nível 1

        // Recria apenas a Scene
        this.janelaJogo = new JogoScreen(partida);
        cenaPrincipal.setRoot(janelaJogo.getContentorPrincipal());

        configurarControlos();
        //gameLoop.playFromStart();
        gameLoop.play();

        primaryStage.setTitle("Frogi - " + partida.getNivelAtual().getNome());
    }

    private void configurarControlos() {
        cenaPrincipal.setOnKeyPressed(event -> {
            if (!partida.getSapo().isVivo()) return;

            try {
                switch (event.getCode()) {
                    case W, UP ->    partida.moverSapo(0, -1);
                    case S, DOWN ->  partida.moverSapo(0, 1);
                    case A, LEFT ->  partida.moverSapo(-1, 0);
                    case D, RIGHT -> partida.moverSapo(1, 0);
                    case ESCAPE ->   janelaJogo.alternarPausa(gameLoop, irMenu);
                    default -> { return; }
                }

                // Processa interações imediatamente após o passo do sapo
                partida.processarInteracoes();
                janelaJogo.atualizarMapa();

                if (partida.isVenceu()) {
                    tratarVitoria();
                }

            } catch (PosicaoInvalidaException e) {
                // PosicaoInvalida
            }
        });
    }

    private void configurarGameLoop() {
        gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            // Quando fica sem vidas
            if (partida.getVidasRestantes() <= 0) {
                gameLoop.stop();
                cenaPrincipal.setOnKeyPressed(null);

                GameOverScreen ecraDerrota = new GameOverScreen(
                        this::tentarNovamente,
                        irMenu
                );
                cenaPrincipal.setRoot(ecraDerrota.getRoot());
                primaryStage.setTitle("Frogi - Game Over!");
                return;
            }

            if (partida.isVenceu()) {
                tratarVitoria();
                return;
            }

            // Passa de nivel
            if (partida.isNivelCompleto()) {
                if (partida.getNivelAtual().getNumero() == 1) {
                    partida.avancarParaProximoNivel(criarNivel2());
                } else if (partida.getNivelAtual().getNumero() == 2) {
                    partida.avancarParaProximoNivel(criarNivel3());
                }
                primaryStage.setTitle("Frogi " + partida.getNivelAtual().getNome());
                janelaJogo.atualizarMapa();
                return;
            }

            // Movimento dos rios e predadores
            if (!partida.isTerminada()) {
                executarCicloFisica();

                if (partida.isVenceu()) {
                    tratarVitoria();
                }
            }
        }));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();
    }

    // Gerar e Mostrar o ecrã de Vitória
    private void tratarVitoria() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        cenaPrincipal.setOnKeyPressed(null);

        // Termina a partida no modelo e recolhe as pontuações
        ResultadoPartida resultadoFinal = partida.terminarPartida();

        // Cria a View de Vitória
        VitoriaScreen ecraVitoria = new VitoriaScreen(() -> {
            if (this.leaderboard != null) {
                this.leaderboard.adicionarResultado(resultadoFinal);
            }
            aoTerminarJogo.run();
        });

        cenaPrincipal.setRoot(ecraVitoria.getRoot());
        primaryStage.setTitle("Frogi - Salvaste o Príncipe!");
    }

    private void executarCicloFisica() {
        Mapa mapa = partida.getNivelAtual().getMapa();
        int sapoX = partida.getXSapo();
        int sapoY = partida.getYSapo();

        boolean estavaNoNenufar = false;
        for (int[] par : mapa.getCoordenadasNenufares()) {
            if (par[0] == sapoX && par[1] == sapoY) {
                estavaNoNenufar = true;
                break;
            }
        }

        mapa.moverNenufaresParaBaixo();

        if (mapa.getColunasRio().contains(sapoX)) {
            if (estavaNoNenufar) {
                int novoYSapo = sapoY + 1;
                if (partida.getNivelAtual().isPosicaoValida(sapoX, novoYSapo)) {
                    partida.getSapo().mover(0, 1);
                } else {
                    partida.perderVida();
                }
            } else {
                partida.perderVida();
            }
        }

        moverEntidadesDoMapa(partida.getNivelAtual(), partida);

        // Ajuste dos predadores
        for (EntidadeJogo entidade : mapa.getEntidades()) {
            if (entidade instanceof Predador) {
                int pX = entidade.getPosicaoX();
                int pY = entidade.getPosicaoY();
                if (mapa.getColunasRio().contains(pX)) {
                    boolean predadorSeguro = false;
                    for (int[] par : mapa.getCoordenadasNenufares()) {
                        if (par[0] == pX && par[1] == pY) {
                            predadorSeguro = true;
                            break;
                        }
                    }
                    if (!predadorSeguro) {
                        entidade.setPosicao(pX - 1, pY);
                    }
                }
            }
        }

        partida.processarInteracoes();
        janelaJogo.atualizarMapa();
    }

    private Nivel criarNivel1(){
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = {
                {2, 2}, {2, 6}, {2,9}, {4, 3}, {4, 9}, {4,6}, {6, 0}, {6, 2}, {6,7},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        Mapa mapaNivel = new Mapa(riosNivel, nenufaresNivel);

        mapaNivel.adicionarEntidade(new Grilo(3, 2));
        mapaNivel.adicionarEntidade(new Grilo(5, 5));
        mapaNivel.adicionarEntidade(new Grilo(7, 1));
        mapaNivel.adicionarEntidade(new Grilo(9, 9));
        mapaNivel.adicionarEntidade(new Grilo(10, 2));
        mapaNivel.adicionarEntidade(new Grilo(11, 1));
        mapaNivel.adicionarEntidade(new Grilo(13, 1));
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Salto(2, 6));
        mapaNivel.adicionarEntidade(new Predador(9, 9));

        return new Nivel(1, mapaNivel, "Nível 1 - Fase Inicial");
    }

    private Nivel criarNivel2(){
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = {
                {2, 3}, {2, 5}, {2,7}, {4, 1}, {4, 2}, {6, 0}, {6,7},
                {8, 5}, {8, 9}, {10, 2}, {10, 7}, {12, 3}
        };
        Mapa mapaNivel = new Mapa(riosNivel, nenufaresNivel);

        mapaNivel.adicionarEntidade(new Grilo(3, 2));
        mapaNivel.adicionarEntidade(new Grilo(5, 5));
        mapaNivel.adicionarEntidade(new Grilo(3, 5));
        mapaNivel.adicionarEntidade(new Grilo(10, 2));
        mapaNivel.adicionarEntidade(new Grilo(0, 9));
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Predador(7, 0));
        mapaNivel.adicionarEntidade(new Predador(9, 9));
        mapaNivel.adicionarEntidade(new Salto(2, 5));
        mapaNivel.adicionarEntidade(new VidaExtra(8, 9));

        return new Nivel(2, mapaNivel, "Nível 2 - Mais Dificil");
    }

    private Nivel criarNivel3(){
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = {
                {2, 0}, {2, 5}, {2,7}, {4, 2}, {4, 9}, {6, 0}, {6,7},
                {8, 2}, {8, 9}, {10, 5}, {10, 7}, {12, 3}
        };
        Mapa mapaNivel = new Mapa(riosNivel, nenufaresNivel);

        mapaNivel.adicionarEntidade(new Grilo(3, 2));
        mapaNivel.adicionarEntidade(new Grilo(5, 5));
        mapaNivel.adicionarEntidade(new Grilo(3, 5));
        mapaNivel.adicionarEntidade(new Grilo(10, 2));
        mapaNivel.adicionarEntidade(new Grilo(0, 9));
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Predador(9, 0));
        mapaNivel.adicionarEntidade(new Predador(11, 9));
        mapaNivel.adicionarEntidade(new Salto(2, 7));
        mapaNivel.adicionarEntidade(new Salto(6, 0));
        mapaNivel.adicionarEntidade(new VidaExtra(9, 1));
        mapaNivel.adicionarEntidade(new VidaExtra(8, 9));
        mapaNivel.adicionarEntidade(new Princesa(14, 9));

        return new Nivel(3, mapaNivel, "Nível 3 - Não vais conseguir...");
    }

    private void moverEntidadesDoMapa(Nivel nivel, Partida partida) {
        Mapa mapa = nivel.getMapa();

        for (EntidadeJogo entidade : mapa.getEntidades()) {
            if (entidade instanceof Predador) {
                int predadorX = entidade.getPosicaoX();
                int predadorY = entidade.getPosicaoY();

                // Se o predador estiver no rio, ele também sofre o efeito do arrasto vertical
                if (mapa.getColunasRio().contains(predadorX)) {
                    // Verifica se o arrasto não o joga para fora do mapa
                    if (nivel.isPosicaoValida(predadorX, predadorY + 1)) {
                        entidade.setPosicao(predadorX, predadorY + 1);
                    } else {
                        // Se o predador cair no fim do rio, faz "wrap-around" para o topo também
                        entidade.setPosicao(predadorX, 0);
                    }
                }

                ((Predador) entidade).moverAutomatico(nivel, partida);

                // Se o movimento aleatório do predador o deixou na água sem nenufar desfazer
                int novoPredadorX = entidade.getPosicaoX();
                int novoPredadorY = entidade.getPosicaoY();

                if (mapa.getColunasRio().contains(novoPredadorX)) {
                    boolean emCimaDeNenufar = false;
                    for (int[] par : mapa.getCoordenadasNenufares()) {
                        if (par[0] == novoPredadorX && par[1] == novoPredadorY) {
                            emCimaDeNenufar = true;
                            break;
                        }
                    }
                    // Se o predador tentou saltar para a água volta à posição anterior
                    if (!emCimaDeNenufar) {
                        entidade.setPosicao(predadorX, predadorY);
                    }
                }
            }
        }
    }
}