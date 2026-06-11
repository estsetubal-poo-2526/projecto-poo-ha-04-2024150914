package org.frogi.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Objects;

public class MenuScreen {

    private final StackPane root;
    private final Runnable onNewGame;
    private final Runnable onHowToPlay;
    private final Runnable onLeaderboard;
    private final Runnable onOptions;

    public MenuScreen(Runnable onNewGame, Runnable onHowToPlay, Runnable onLeaderboard, Runnable onOptions) {
        this.root = new StackPane();
        this.onNewGame = onNewGame;
        this.onHowToPlay = onHowToPlay;
        this.onLeaderboard = onLeaderboard;
        this.onOptions = onOptions;

        configurarLayout();
    }

    private void configurarLayout() {
        // Imagem de Fundo
        try {
            Image imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu.png")));
            BackgroundImage bg = new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(bg));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #2e7d32;"); // Fundo verde alternativo caso falhe
        }

        // Painel Superior (Botão de atalho ? no canto esquerdo)
        BorderPane topoPane = new BorderPane();
        topoPane.setPickOnBounds(false); // Permite clicar através do painel vazio

        Button btnAjuda = new Button();
        btnAjuda.setPrefWidth(60);
        btnAjuda.setPrefHeight(40);
        btnAjuda.setStyle(
                "-fx-background-image: url('/images/botao_ajuda.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnAjuda.setOnAction(e -> onHowToPlay.run());


        topoPane.setLeft(btnAjuda);
        BorderPane.setMargin(btnAjuda, new javafx.geometry.Insets(20));

        // Painel Central (Botões Principais)
        VBox menuCentral = new VBox(20);
        menuCentral.setAlignment(Pos.CENTER);
        menuCentral.setMaxWidth(300);


        // Botões de Ação
        Button btnNovoJogo = new Button();
        btnNovoJogo.setPrefWidth(181);
        btnNovoJogo.setPrefHeight(82.42);
        btnNovoJogo.setStyle(
                "-fx-background-image: url('/images/botao_novo_jogo.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnNovoJogo.setOnAction(e -> onNewGame.run());

        Button btnOpcoes = new Button();
        btnOpcoes.setPrefWidth(181);
        btnOpcoes.setPrefHeight(82.42);
        btnOpcoes.setStyle(
                "-fx-background-image: url('/images/botao_opcoes.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnOpcoes.setOnAction(e -> onOptions.run());

        Button btnPontuacoes = new Button();
        btnPontuacoes.setPrefWidth(181);
        btnPontuacoes.setPrefHeight(82.42);
        btnPontuacoes.setStyle(
                "-fx-background-image: url('/images/botao_pontuacoes.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnPontuacoes.setOnAction(e -> onLeaderboard.run());

        menuCentral.getChildren().addAll(btnNovoJogo, btnOpcoes, btnPontuacoes);

        root.getChildren().addAll(topoPane, menuCentral);
    }

    public Pane getRoot() {
        return root;
    }
}