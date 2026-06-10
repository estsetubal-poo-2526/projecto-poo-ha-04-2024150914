package org.frogi;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.frogi.model.*;
import org.frogi.model.entidades.*;
import org.frogi.model.powerups.Salto;
import org.frogi.model.powerups.VidaExtra;
import org.frogi.view.HowToPlayScreen;
import org.frogi.view.JogoScreen;
import org.frogi.view.LeaderboardScreen;
import org.frogi.view.MenuScreen;

import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private Jogador jogador;
    private Leaderboard leaderboard;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        this.leaderboard = new Leaderboard();

        // Arranca mostrando o Menu Principal
        mostrarMenu();
    }

    private void mostrarMenu() {
        // Criamos o menu passando o que cada botão deve fazer quando for clicado
        MenuScreen menu = new MenuScreen(
                this::iniciarJogo,      // Ao carregar New Game
                this::mostrarHowToPlay, // Ao carregar no "?"
                this::mostrarLeaderboard // Ao carregar no "LD"
        );

        // Usa o tamanho padrão do tabuleiro (15 colunas * 70px = 1050 largura por ~ 750 altura com o topo)
        Scene cenaMenu = new Scene(menu.getRoot(), 1050, 750);
        primaryStage.setTitle("Frogi - Menu Principal");
        primaryStage.setScene(cenaMenu);
        primaryStage.show();
    }

    private void mostrarHowToPlay() {
        HowToPlayScreen htp = new HowToPlayScreen(this::mostrarMenu);
        Scene cena = new Scene(htp.getRoot(), 1050, 750);
        primaryStage.setTitle("Frogi - Como Jogar");
        primaryStage.setScene(cena);
    }

    private void mostrarLeaderboard() {
        LeaderboardScreen lb = new LeaderboardScreen(this::mostrarMenu, this.leaderboard);
        Scene cena = new Scene(lb.getRoot(), 1050, 750);
        primaryStage.setTitle("Frogi - Classificações");
        primaryStage.setScene(cena);
    }

    private void iniciarJogo() {
        // 1. Criar a caixa de diálogo para pedir o nome
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("Jogador 1");
        dialog.setTitle("Novo Jogo - Frogi");
        dialog.setHeaderText("Prepara-te para entrar no pântano!");
        dialog.setContentText("Por favor, introduz o teu nome:");

        // Customização visual básica para combinar com o teu estilo escuro/madeira (opcional)
        dialog.getDialogPane().setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        dialog.getDialogPane().lookup(".content.label").setStyle("-fx-text-fill: white;");

        // 2. Mostrar a janela e esperar que o utilizador responda
        java.util.Optional<String> resultado = dialog.showAndWait();

        // 3. Verificar se o jogador escreveu o nome e clicou em "OK"
        if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
            String nomeIntroduzido = resultado.get().trim();

            // Instanciamos o jogador com o nome real introduzido!
            this.jogador = new Jogador(nomeIntroduzido);

            System.out.println("O jogador " + nomeIntroduzido + " iniciou a partida.");

            // Arranca os níveis com o jogador atualizado
            Niveis(primaryStage, this.jogador);
        } else {
            // Se fechou ou clicou em cancelar, não fazemos nada (ele continua no menu)
            System.out.println("Criação de jogo cancelada.");
        }
    }

    public void Niveis(Stage primaryStage, Jogador jogador){
        Nivel nivel1 = nivel1();
        Nivel nivel2 = nivel2();
        //Nivel nivel3 = nivel3();

        Partida partida = new Partida(jogador, nivel1);
        partida.iniciarPartida();

        // 2. Criar a View
        JogoScreen janelaJogo = new JogoScreen(partida, nivel1.getNumero());

        // 3. Criar a Scene
        Scene scene = new Scene(janelaJogo.getContentorPrincipal());

        // Capturar as teclas do teclado
        scene.setOnKeyPressed(event -> {
            // Se o sapo estiver morto, não faz nada
            if (!partida.getSapo().isVivo()) return;

            switch (event.getCode()) {
                case W, UP ->    partida.moverSapo(0, -1); // Cima (Y diminui)
                case S, DOWN ->  partida.moverSapo(0, 1);  // Baixo (Y aumenta)
                case A, LEFT ->  partida.moverSapo(-1, 0); // Esquerda (X diminui)
                case D, RIGHT -> partida.moverSapo(1, 0);  // Direita (X aumenta)
                default -> { return; } // Ignora outras teclas
            }

            // Depois de mover o modelo, redesenha o mapa na interface gráfica
            janelaJogo.atualizarMapa();
        });

        final Timeline[] gameLoopHolder = new Timeline[1];

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(1000), event -> {
            if (partida.getVidasRestantes() <= 0) {
                partida.setNivel(nivel1()); // Recria o nível 1 limpo
                partida.reiniciarPartida();
                primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());
                janelaJogo.atualizarMapa();
                return;
            }

            if (partida.isNivelCompleto()) {
                if (partida.getNivelAtual().getNumero() == 1) {

                    // Altera o modelo para o nível 2
                    partida.avancarParaProximoNivel(nivel2());
                    primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());

                    // Redesenha a janela (o mapa novo vai aparecer sozinho)
                    janelaJogo.atualizarMapa();
                } else if (partida.getNivelAtual().getNumero() == 3) {
                    // Carregar o nivel3()
                } else if(partida.getNivelAtual().getNumero() == 2){
                    // Mostrar ecrã de vitoria
                    partida.setVenceu(true);
                    ResultadoPartida resultadoFinal = partida.terminarPartida();
                    leaderboard.adicionarResultado(resultadoFinal);

                    if (gameLoopHolder[0] != null) {
                        gameLoopHolder[0].stop();
                    }

                    mostrarMenu();
                }
                return; // Interrompe para não mover monstros no frame de transição
            }

            if (!partida.isTerminada()) {

                // Atualiza o relógio do topo mesmo que o jogador esteja parado
                janelaJogo.atualizarMapa();

                // Mover os predadores e grilos aleatoriamente
                moverEntidadesDoMapa(partida.getNivelAtual(), partida);

                // Após mover os inimigos, processa interações (ver se algum predador pisou o sapo)
                partida.processarInteracoes();

                // Redesenha tudo com as novas posições
                janelaJogo.atualizarMapa();
            }
        }));

        // Define que a Timeline corre para sempre até fechar o jogo
        gameLoopHolder[0] = gameLoop;
        gameLoop.setCycleCount(Animation.INDEFINITE);
        gameLoop.play(); // Arranca o temporizador!

        primaryStage.setTitle("Frogi "+ partida.getNivelAtual().getNome());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
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
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Salto(2, 6));
        mapaNivel.adicionarEntidade(new Predador(9, 9));

        Nivel nivel = new Nivel(1, mapaNivel, "Nível 1 - Fase Inicial");

        return nivel;
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
        mapaNivel.adicionarEntidade(new Salto(2, 6));
        mapaNivel.adicionarEntidade(new VidaExtra(8, 9));

        Nivel nivel = new Nivel(2, mapaNivel, "Nível 2 - Mais Dificil");

        return nivel;
    }



    private void moverEntidadesDoMapa(Nivel nivel, Partida partida) {
        for (EntidadeJogo entidade : nivel.getMapa().getEntidades()) {
            // Se for um predador, passa-lhe o nível atual e ele resolve o movimento sozinho
            if (entidade instanceof Predador) {
                ((Predador) entidade).moverAutomatico(nivel, partida);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}