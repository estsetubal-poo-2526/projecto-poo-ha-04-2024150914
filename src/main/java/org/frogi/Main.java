package org.frogi;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.frogi.controller.GestorSom;
import org.frogi.model.*;
import org.frogi.model.entidades.*;
import org.frogi.model.powerups.Salto;
import org.frogi.model.powerups.VidaExtra;
import org.frogi.view.*;

import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private Scene cenaPrincipal;
    private Jogador jogador;
    private Leaderboard leaderboard;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        this.leaderboard = new Leaderboard();

        GestorSom.getInstance().tocarMusicaFundo();

        // Cena com um painel vazio inicial e o tamanho padrão
        this.cenaPrincipal = new Scene(new javafx.scene.layout.StackPane(), 1280, 720);

        primaryStage.setTitle("Frogi");
        primaryStage.setScene(cenaPrincipal);
        primaryStage.show();

        // Arranca mostrando o Menu Principal
        mostrarMenu();
    }

    private void mostrarMenu() {
        // Cria o menu passando o que cada botão deve fazer quando for clicado
        MenuScreen menu = new MenuScreen(
                this::iniciarJogo,
                this::mostrarHowToPlay,
                this::mostrarLeaderboard,
                this::mostrarOptions
        );

        cenaPrincipal.setRoot(menu.getRoot());
        primaryStage.setTitle("Frogi - Menu Principal");
    }

    private void mostrarOptions() {
        OpcoesScreen opcoesScreen = new OpcoesScreen(this::mostrarMenu, this.primaryStage);
        cenaPrincipal.setRoot(opcoesScreen.getRoot());
        primaryStage.setTitle("Frogi - Opções");
    }

    private void mostrarHowToPlay() {
        ComoJogarScreen htp = new ComoJogarScreen(this::mostrarMenu);
        cenaPrincipal.setRoot(htp.getRoot());
        primaryStage.setTitle("Frogi - Como Jogar");
    }

    private void mostrarLeaderboard() {
        LeaderboardScreen lb = new LeaderboardScreen(this::mostrarMenu, this.leaderboard);
        cenaPrincipal.setRoot(lb.getRoot());
        primaryStage.setTitle("Frogi - Pontuações");
    }

    private void iniciarJogo() {
        //Pede o nome ao jogador
        NomeScreen ecraNome = new NomeScreen(
                this::mostrarMenu, // Se clicar em Cancelar
                (String nomeIntroduzido) -> {
                    // Se o nome válido for submetido
                    this.jogador = new Jogador(nomeIntroduzido);
                    Niveis(primaryStage, this.jogador);
                }
        );

        cenaPrincipal.setRoot(ecraNome.getRoot());
        primaryStage.setTitle("Frogi - Jogador");
    }

    public void Niveis(Stage primaryStage, Jogador jogador){

        Partida partida = new Partida(jogador, nivel1());
        partida.iniciarPartida();

        // Cria a View
        JogoScreen janelaJogo = new JogoScreen(partida);

        // Altera a Scene
        cenaPrincipal.setRoot(janelaJogo.getContentorPrincipal());
        final Timeline[] gameLoopHolder = new Timeline[1];

        // Captura as teclas do teclado
        cenaPrincipal.setOnKeyPressed(event -> {
            // Se o sapo estiver morto, não faz nada
            if (!partida.getSapo().isVivo()) return;

            switch (event.getCode()) {
                case W, UP ->    partida.moverSapo(0, -1);
                case S, DOWN ->  partida.moverSapo(0, 1);
                case A, LEFT ->  partida.moverSapo(-1, 0);
                case D, RIGHT -> partida.moverSapo(1, 0);

                case ESCAPE -> {
                    if (gameLoopHolder[0] != null) {
                        janelaJogo.alternarPausa(gameLoopHolder[0], this::mostrarMenu);
                    }
                }

                default -> { return; }
            }

            janelaJogo.atualizarMapa();
        });

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            if (partida.getVidasRestantes() <= 0) {
                partida.setNivel(nivel1()); // Recria o nível 1
                partida.reiniciarPartida();
                primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());

                janelaJogo.atualizarMapa();
                return;
            }

            if (partida.isVenceu()) {
                // Para o temporizador do jogo imediatamente para congelar relógios e monstros
                if (gameLoopHolder[0] != null) {
                    gameLoopHolder[0].stop();
                }

                // Bloqueia o teclado para o jogador não mover o boneco durante os ecrãs de vitoria
                cenaPrincipal.setOnKeyPressed(null);

                // Recolhe o resultado final calculado pela partida
                ResultadoPartida resultadoFinal = partida.terminarPartida();

                VitoriaScreen ecraVitoria = new VitoriaScreen(() -> {
                    leaderboard.adicionarResultado(resultadoFinal);
                    mostrarLeaderboard();
                });

                // Troca o ecrã para as imagens de vitória
                cenaPrincipal.setRoot(ecraVitoria.getRoot());
                primaryStage.setTitle("Frogi - Salvaste o Principe!");
                return;
            }

            if (partida.isNivelCompleto()) {
                if (partida.getNivelAtual().getNumero() == 1) {
                    // Altera o modelo para o nível 2
                    partida.avancarParaProximoNivel(nivel2());
                    primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());
                    janelaJogo.atualizarMapa();

                } else if (partida.getNivelAtual().getNumero() == 2) {
                    // Altera o modelo para o nível 3
                    partida.avancarParaProximoNivel(nivel3());
                    primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());
                    janelaJogo.atualizarMapa();

                }
                return; // Interrompe para não mover monstros no momento de transição
            }

            if (!partida.isTerminada()) {

                // Atualiza o relógio do topo mesmo que o jogador esteja parado
                janelaJogo.atualizarMapa();

                // Move os predadores e grilos aleatoriamente
                moverEntidadesDoMapa(partida.getNivelAtual(), partida);

                // Após mover os inimigos, processa interações (vê se algum predador tocou no sapo)
                partida.processarInteracoes();

                // Redesenha tudo com as novas posições
                janelaJogo.atualizarMapa();
            }
        }));

        // Define que a Timeline corre para sempre até fechar o jogo
        gameLoopHolder[0] = gameLoop;
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play();

        primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());
    }

    private Nivel nivel1(){
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = {
                {2, 2}, {2, 6}, {2,9}, {4, 3}, {4, 9}, {4,6}, {6, 0}, {6, 2}, {6,7},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        Mapa mapaNivel = new Mapa(riosNivel, nenufaresNivel);

        mapaNivel.adicionarEntidade(new Grilo(3, 2));
        mapaNivel.adicionarEntidade(new Grilo(5, 5));
        mapaNivel.adicionarEntidade(new Grilo(9, 9));
        mapaNivel.adicionarEntidade(new Grilo(11, 1));
        mapaNivel.adicionarEntidade(new Grilo(13, 1));
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Salto(2, 6));
        mapaNivel.adicionarEntidade(new Predador(9, 9));

        return new Nivel(1, mapaNivel, "Nível 1 - Fase Inicial");
    }

    private Nivel nivel2(){
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

    private Nivel nivel3(){
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
        for (EntidadeJogo entidade : nivel.getMapa().getEntidades()) {
            if (entidade instanceof Predador) {
                ((Predador) entidade).moverAutomatico(nivel, partida);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}