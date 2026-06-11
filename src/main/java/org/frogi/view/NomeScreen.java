package org.frogi.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.Objects;
import java.util.function.Consumer;

public class NomeScreen {
    private final StackPane root;

    public NomeScreen(Runnable onCancelar, Consumer<String> onConfirmarNome) {
        this.root = new StackPane();

        // Imagem de Fundo
        try {
            Image imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png")));
            root.setBackground(new Background(new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #3e2723;");
        }

        // Caixa central
        VBox painelCentral = new VBox(20);
        painelCentral.setAlignment(Pos.CENTER);
        painelCentral.setMaxSize(460, 280);
        painelCentral.setStyle(
                "-fx-background-color: rgba(92, 64, 51, 0.85); " + // Castanho escuro semi-transparente
                        "-fx-background-radius: 15px; " +
                        "-fx-border-color: #f5ccb0; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-padding: 25px;"
        );

        Label lblPedirNome = new Label("INTRODUZ O TEU NOME:");
        lblPedirNome.setStyle(
                "-fx-text-fill: #f5ccb0; " + // Cor bege condizente com a borda
                        "-fx-font-family: 'Consolas', monospace; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold;"
        );

        TextField txtNome = new TextField();
        txtNome.setPrefWidth(320);
        txtNome.setMaxWidth(320);
        txtNome.setStyle(
                "-fx-background-color: #3e2723; " +
                        "-fx-text-fill: #f5ccb0; " +
                        "-fx-font-family: 'Consolas', monospace; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #5c4033; " +
                        "-fx-border-radius: 5px;"
        );

        // Submeter automaticamente se o jogador carregar na tecla Enter
        txtNome.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            if (!nome.isEmpty()) onConfirmarNome.accept(nome);
        });

        // Contentor para os botões
        HBox linhaBotoes = new HBox(30);
        linhaBotoes.setAlignment(Pos.CENTER);

        // Botão Cancelar com Imagem
        Button btnCancelar = new Button();
        btnCancelar.setPrefWidth(140);
        btnCancelar.setPrefHeight(50);
        btnCancelar.setStyle(
                "-fx-background-image: url('/images/botao_cancelar.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnCancelar.setOnAction(e -> onCancelar.run());

        // Botão Jogar com Imagem
        Button btnJogar = new Button();
        btnJogar.setPrefWidth(140);
        btnJogar.setPrefHeight(50);
        btnJogar.setStyle(
                "-fx-background-image: url('/images/botao_jogar.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnJogar.setOnAction(e -> {
            String nome = txtNome.getText().trim();
            if (!nome.isEmpty()) {
                onConfirmarNome.accept(nome);
            } else {
                // Feedback visual de erro acendendo a borda a vermelho se tentar ir vazio
                txtNome.setStyle(txtNome.getStyle() + "-fx-border-color: #d32f2f; -fx-border-width: 1.5px;");
            }
        });

        linhaBotoes.getChildren().addAll(btnCancelar, btnJogar);
        painelCentral.getChildren().addAll(lblPedirNome, txtNome, linhaBotoes);
        root.getChildren().add(painelCentral);
    }

    public Pane getRoot() { return root; }
}