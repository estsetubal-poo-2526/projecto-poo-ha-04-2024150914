package org.frogi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.frogi.controller.SomController;
import org.frogi.controller.JogoController;
import org.frogi.model.Jogador;
import org.frogi.model.Leaderboard;
import org.frogi.view.*;

import java.util.Objects;

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

        SomController.getInstance().tocarMusicaFundo();

        // Cena com um painel vazio inicial e o tamanho padrão
        this.cenaPrincipal = new Scene(new javafx.scene.layout.StackPane(), 1280, 720);

        primaryStage.setTitle("Frogi");
        primaryStage.setScene(cenaPrincipal);
        try {
            // Carrega o sprite do sapo que já usas no jogo para ser o ícone da janela
            Image iconSapo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/sapo.png")));
            primaryStage.getIcons().add(iconSapo);
        } catch (Exception e) {
            System.out.println("[AVISO] Não foi possível carregar o ícone da janela.");
        }
        primaryStage.show();

        // Arranca mostrando o Menu Principal
        mostrarMenu();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Garante que o controlador de som desliga todas as threads de áudio ativas
        SomController.getInstance().pararMusica();
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
        // Pede o nome ao jogador
        NomeScreen ecraNome = new NomeScreen(
                this::mostrarMenu, // Se clicar em Cancelar
                (String nomeIntroduzido) -> {
                    // Se um nome válido for submetido, guarda o jogador
                    this.jogador = new Jogador(nomeIntroduzido);

                    JogoController jogoController = new JogoController(
                            primaryStage,
                            cenaPrincipal,
                            this.jogador,
                            this.leaderboard,
                            this::mostrarLeaderboard,
                            this::mostrarMenu
                    );

                    // Inicializa e arranca o jogo
                    jogoController.iniciar();
                }
        );

        cenaPrincipal.setRoot(ecraNome.getRoot());
        primaryStage.setTitle("Frogi - Jogador");
    }

    public static void main(String[] args) {
        launch(args);
    }
}