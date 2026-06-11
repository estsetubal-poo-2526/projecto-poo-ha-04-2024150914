package org.frogi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.Objects;

public class ComoJogarScreen {
    private final StackPane root;
    private final Runnable onVoltarMenu;

    public ComoJogarScreen(Runnable onVoltarMenu) {
        this.root = new StackPane();
        this.onVoltarMenu = onVoltarMenu;
        configurarLayout();
    }

    private void configurarLayout() {
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/como_jogar.png")));
            BackgroundSize bgSize = new BackgroundSize(
                    BackgroundSize.AUTO, BackgroundSize.AUTO,
                    false, false, false, true
            );

            root.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, bgSize)));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1b5e20;");
        }


        // Botão de voltar no canto superior direito
        Button btnVoltar = new Button();
        btnVoltar.setPrefWidth(60);
        btnVoltar.setPrefHeight(40);
        btnVoltar.setStyle(
                "-fx-background-image: url('/images/botao_voltar_atras.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;" // Remove o cinzento nativo
        );
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
        StackPane.setMargin(btnVoltar, new Insets(20));

        root.getChildren().add(btnVoltar);
    }

    public Pane getRoot() { return root; }
}