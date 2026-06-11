package org.frogi;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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

import java.io.*;
import java.util.List;
import java.util.Optional;

public class Main extends Application {

    private Stage primaryStage;
    private Jogador jogador;
    private Leaderboard leaderboard;
    private Timeline gameLoopAtual;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        this.leaderboard = new Leaderboard();

        mostrarMenu();
    }

    private void mostrarMenu() {
        MenuScreen menu = new MenuScreen(
                this::iniciarJogo,
                this::mostrarHowToPlay,
                this::mostrarLeaderboard,
                this::carregarJogo);

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
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog("Jogador 1");
        dialog.setTitle("Novo Jogo - Frogi");
        dialog.setHeaderText("Prepara-te para entrar no pântano!");
        dialog.setContentText("Por favor, introduz o teu nome:");

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent() && !resultado.get().trim().isEmpty()) {
            this.jogador = new Jogador(resultado.get().trim());
            Niveis(primaryStage, this.jogador);
        }
    }

    public void Niveis(Stage primaryStage, Jogador jogador) {
        Nivel nivel1 = nivel1();
        Nivel nivel2 = nivel2();

        Partida partida = new Partida(jogador, nivel1);
        partida.iniciarPartida();

        JogoScreen janelaJogo = new JogoScreen(partida, nivel1.getNumero());

        Scene scene = new Scene(janelaJogo.getContentorPrincipal());

        // Controlo de teclas
        scene.setOnKeyPressed(event -> {
            if (!partida.getSapo().isVivo())
                return;

            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                pausarJogo(partida, primaryStage, janelaJogo);
                return;
            }

            switch (event.getCode()) {
                case W, UP -> partida.moverSapo(0, -1);
                case S, DOWN -> partida.moverSapo(0, 1);
                case A, LEFT -> partida.moverSapo(-1, 0);
                case D, RIGHT -> partida.moverSapo(1, 0);
            }
            janelaJogo.atualizarMapa();
        });

        // Game Loop
        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (partida.getVidasRestantes() <= 0) {
                partida.setNivel(nivel1());
                partida.reiniciarPartida();
                janelaJogo.atualizarMapa();
                return;
            }

            if (partida.isNivelCompleto()) {
                if (partida.getNivelAtual().getNumero() == 1) {
                    partida.avancarParaProximoNivel(nivel2());
                    janelaJogo.atualizarMapa();
                } else if (partida.getNivelAtual().getNumero() == 2) {
                    partida.setVenceu(true);
                    ResultadoPartida resultadoFinal = partida.terminarPartida();
                    leaderboard.adicionarResultado(resultadoFinal);
                    if (gameLoopAtual != null)
                        gameLoopAtual.stop();
                    mostrarMenu();
                }
                return;
            }

            if (!partida.isTerminada()) {
                janelaJogo.atualizarMapa();
                moverEntidadesDoMapa(partida.getNivelAtual(), partida);
                partida.processarInteracoes();
                janelaJogo.atualizarMapa();
            }
        }));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        this.gameLoopAtual = gameLoop;
        gameLoop.play();

        primaryStage.setTitle("Frogi - " + partida.getNivelAtual().getNome());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Nivel nivel1() {
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = { { 2, 2 }, { 2, 6 }, { 2, 9 }, { 4, 3 }, { 4, 9 }, { 4, 6 }, { 6, 0 }, { 6, 2 },
                { 6, 7 },
                { 8, 5 }, { 8, 9 }, { 10, 0 }, { 10, 2 }, { 10, 7 }, { 12, 1 }, { 12, 3 } };
        Mapa mapaNivel = new Mapa(riosNivel, nenufaresNivel);

        mapaNivel.adicionarEntidade(new Grilo(3, 2));
        mapaNivel.adicionarEntidade(new Grilo(5, 5));
        mapaNivel.adicionarEntidade(new Predador(7, 4));
        mapaNivel.adicionarEntidade(new Salto(2, 6));
        mapaNivel.adicionarEntidade(new Predador(9, 9));

        return new Nivel(1, mapaNivel, "Nível 1 - Fase Inicial");
    }

    private Nivel nivel2() {
        List<Integer> riosNivel = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufaresNivel = { { 2, 3 }, { 2, 5 }, { 2, 7 }, { 4, 1 }, { 4, 2 }, { 6, 0 }, { 6, 7 }, { 8, 5 },
                { 8, 9 },
                { 10, 2 }, { 10, 7 }, { 12, 3 } };
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

        return new Nivel(2, mapaNivel, "Nível 2 - Mais Difícil");
    }

    private void moverEntidadesDoMapa(Nivel nivel, Partida partida) {
        for (EntidadeJogo entidade : nivel.getMapa().getEntidades()) {
            if (entidade instanceof Predador predador) {
                predador.moverAutomatico(nivel, partida);
            }
        }
    }

    private void pausarJogo(Partida partida, Stage stage, JogoScreen jogoScreen) {
        if (gameLoopAtual != null)
            gameLoopAtual.pause();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Jogo Pausado");
        alert.setHeaderText("Frogi Pausado");
        alert.setContentText("O que queres fazer?");

        ButtonType continuar = new ButtonType("Continuar");
        ButtonType guardarSair = new ButtonType("Guardar e Sair");
        ButtonType sairSemGuardar = new ButtonType("Sair sem guardar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(continuar, guardarSair, sairSemGuardar);

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent()) {
            if (resultado.get() == guardarSair) {
                guardarProgresso(partida);
                mostrarMenu();
            } else if (resultado.get() == sairSemGuardar) {
                mostrarMenu();
            } else {
                if (gameLoopAtual != null)
                    gameLoopAtual.play();
            }
        }
    }

    private void guardarProgresso(Partida partida) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("save_frogi.txt"))) {
            pw.println(partida.getJogador().getNome());
            pw.println(partida.getNivelAtual().getNumero());
            pw.println(partida.getGrilosApanhados());
            pw.println(partida.getVidasRestantes());
            pw.println(partida.getTempoDecorrido());
            pw.println(partida.getXSapo());
            pw.println(partida.getYSapo());

            System.out.println("✅ Guardado com sucesso!"); // ← vê se aparece no console

            Alert sucesso = new Alert(Alert.AlertType.INFORMATION);
            sucesso.setTitle("Sucesso");
            sucesso.setContentText("Jogo guardado!");
            sucesso.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarJogo() {
        File saveFile = new File("save_frogi.txt");
        if (!saveFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Sem Save");
            alert.setContentText("Não existe jogo guardado.");
            alert.showAndWait();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(saveFile))) {
            String nomeJogador = br.readLine().trim();
            int nivelNumero = Integer.parseInt(br.readLine().trim());
            int grilosSalvos = Integer.parseInt(br.readLine().trim());
            int vidasSalvas = Integer.parseInt(br.readLine().trim());
            long tempoSalvo = Long.parseLong(br.readLine().trim());
            int xSapo = Integer.parseInt(br.readLine().trim());
            int ySapo = Integer.parseInt(br.readLine().trim());

            this.jogador = new Jogador(nomeJogador);
            Nivel nivelCarregado = (nivelNumero == 1) ? nivel1() : nivel2();

            Partida partida = new Partida(this.jogador, nivelCarregado);
            partida.iniciarPartida();

            // Restaurar estado
            for (int i = 0; i < grilosSalvos; i++) {
                partida.adicionarGrilo();
            }

            while (partida.getVidasRestantes() < vidasSalvas) {
                partida.adicionarVida();
            }

            partida.getSapo().setPosicao(xSapo, ySapo);

            // === RESTAURAR TEMPO ===
            partida.setTempoDecorrido(tempoSalvo);

            System.out.println("✅ Jogo carregado com sucesso! Tempo restaurado: " + tempoSalvo + "s");

            // Inicia o jogo
            Niveis(primaryStage, this.jogador);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Erro ao carregar o save.");
            alert.showAndWait();
        }
    }

    private void iniciarJogoComPartida(Partida partida, int nivelNumero) {
        JogoScreen janelaJogo = new JogoScreen(partida, nivelNumero);

        Scene scene = new Scene(janelaJogo.getContentorPrincipal());

        scene.setOnKeyPressed(event -> {
            if (!partida.getSapo().isVivo())
                return;

            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                pausarJogo(partida, primaryStage, janelaJogo);
                return;
            }

            switch (event.getCode()) {
                case W, UP -> partida.moverSapo(0, -1);
                case S, DOWN -> partida.moverSapo(0, 1);
                case A, LEFT -> partida.moverSapo(-1, 0);
                case D, RIGHT -> partida.moverSapo(1, 0);
            }
            janelaJogo.atualizarMapa();
        });

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            if (partida.getVidasRestantes() <= 0 || partida.isTerminada())
                return;

            if (partida.isNivelCompleto()) {
                // tua lógica de vitória...
                return;
            }

            janelaJogo.atualizarMapa();
            moverEntidadesDoMapa(partida.getNivelAtual(), partida);
            partida.processarInteracoes();
            janelaJogo.atualizarMapa();
        }));

        gameLoop.setCycleCount(Animation.INDEFINITE);
        this.gameLoopAtual = gameLoop;
        gameLoop.play();

        primaryStage.setTitle("Frogi - " + partida.getNivelAtual().getNome());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}