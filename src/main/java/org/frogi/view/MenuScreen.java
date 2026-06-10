package org.frogi.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Objects;

public class MenuScreen {

    private final StackPane root;
    private final Runnable onNewGame;
    private final Runnable onHowToPlay;
    private final Runnable onLeaderboard;

    public MenuScreen(Runnable onNewGame, Runnable onHowToPlay, Runnable onLeaderboard) {
        this.root = new StackPane();
        this.onNewGame = onNewGame;
        this.onHowToPlay = onHowToPlay;
        this.onLeaderboard = onLeaderboard;

        configurarLayout();
    }

    private void configurarLayout() {
        // 1. Imagem de Fundo (Podes usar o teu MenuScreen.jpg se quiseres a floresta inteira)
        try {
            Image imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MenuScreen.jpg")));
            BackgroundImage bg = new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(bg));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #2e7d32;"); // Fundo verde alternativo caso falhe
        }

        // 2. Painel Superior (Botões de atalho ? e LD nos cantos)
        BorderPane topoPane = new BorderPane();
        topoPane.setPickOnBounds(false); // Permite clicar através do painel vazio

        Button btnAjuda = new Button("?");
        btnAjuda.setStyle("-fx-background-color: #8d6e63; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAjuda.setOnAction(e -> onHowToPlay.run());

        Button btnLD = new Button("LD");
        btnLD.setStyle("-fx-background-color: #8d6e63; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLD.setOnAction(e -> onLeaderboard.run());

        topoPane.setLeft(btnAjuda);
        topoPane.setRight(btnLD);
        BorderPane.setMargin(btnAjuda, new javafx.geometry.Insets(20));
        BorderPane.setMargin(btnLD, new javafx.geometry.Insets(20));

        // 3. Painel Central (Título + Botões Principais)
        VBox menuCentral = new VBox(20);
        menuCentral.setAlignment(Pos.CENTER);
        menuCentral.setMaxWidth(300);

        Text titulo = new Text("FROGI");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        titulo.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 2px;");

        // Botões de Ação estilo "Madeira" temporário (podes customizar depois com imagens)
        Button btnNewGame = criarBotaoMenu("New Game");
        btnNewGame.setOnAction(e -> onNewGame.run());

        Button btnLoadGame = criarBotaoMenu("Load Game");
        Button btnOptions = criarBotaoMenu("Options");

        menuCentral.getChildren().addAll(titulo, btnNewGame, btnLoadGame, btnOptions);

        // Juntar tudo no StackPane raiz
        root.getChildren().addAll(topoPane, menuCentral);
    }

    private Button criarBotaoMenu(String texto) {
        Button btn = new Button(texto);
        btn.setPrefWidth(220);
        btn.setPrefHeight(45);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        // Estilo a imitar os troncos castanhos do teu mockup
        btn.setStyle("-fx-background-color: #5d4037; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");

        // Efeito de Hover visual simples
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4e342e; -fx-text-fill: #ffeb3b; -fx-background-radius: 10; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #5d4037; -fx-text-fill: white; -fx-background-radius: 10;"));

        return btn;
    }

    public Pane getRoot() {
        return root;
    }
}