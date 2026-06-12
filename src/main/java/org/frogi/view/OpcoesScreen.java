package org.frogi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.frogi.controller.SomController;

import java.util.Objects;

public class OpcoesScreen {

    private final StackPane root;
    private final Runnable onVoltarMenu;
    private final Stage primaryStage;

    public OpcoesScreen(Runnable onVoltarMenu, Stage primaryStage) {
        this.root = new StackPane();
        this.onVoltarMenu = onVoltarMenu;
        this.primaryStage = primaryStage;
        configurarLayout();
    }

    private void configurarLayout() {
        // Imagem de Fundo
        try {
            Image imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/opcoes.png")));
            BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, true, false);
            root.setBackground(new Background(new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, bgSize)));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #2e7d32;");
        }

        // Botão Voltar
        AnchorPane camadaTop = new AnchorPane();
        camadaTop.setPickOnBounds(false);

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

        AnchorPane.setTopAnchor(btnVoltar, 32.0);
        AnchorPane.setLeftAnchor(btnVoltar, 25.0);
        camadaTop.getChildren().add(btnVoltar);

        // Painel Central de Opções
        VBox painelCentral = new VBox(30);
        painelCentral.setAlignment(Pos.CENTER);
        painelCentral.setMaxWidth(500);
        VBox.setMargin(painelCentral, new Insets(100, 0, 0, 0));

        // Volume
        HBox linhaVolume = new HBox(20);
        linhaVolume.setAlignment(Pos.CENTER);

        // Imagem da Musica
        Region musica = new Region();
        musica.setPrefSize(181, 82.42);
        musica.setStyle("-fx-background-image: url('/images/botao_musica.png'); -fx-background-size: contain; -fx-background-repeat: no-repeat;");


        // Controlador de Volume
        Slider sliderMusica = new Slider(0, 100, 50); // Mínimo 0, Máximo 100, Começa em 50
        sliderMusica.setPrefWidth(250);
        sliderMusica.setStyle(
                "-fx-control-inner-background: #f5ccb0; " +
                        "-fx-track-color: #f5ccb0; " +
                        "-fx-background-color: transparent;"
        );

        // Listener que deteta quando o jogador mexe no Slider
        sliderMusica.setValue(SomController.getInstance().getVolumeMusica() * 100); // Define a posição inicial com base no volume atual
        sliderMusica.valueProperty().addListener((observable, oldValue, newValue) -> {
            SomController.getInstance().setVolumeMusica(newValue.doubleValue() / 100.0);
        });

        linhaVolume.getChildren().addAll(musica, sliderMusica);

        // BOTÕES DE ECRÃ (Janela / FullScreen)
        HBox linhaEcra = new HBox(30);
        linhaEcra.setAlignment(Pos.CENTER);

        Button btnJanela = new Button();
        btnJanela.setPrefWidth(181);
        btnJanela.setPrefHeight(82.42);
        btnJanela.setStyle(
                "-fx-background-image: url('/images/botao_janela.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnJanela.setOnAction(e -> {
            primaryStage.setFullScreen(false);
        });

        Button btnFullScreen = new Button();
        btnFullScreen.setPrefWidth(181);
        btnFullScreen.setPrefHeight(82.42);
        btnFullScreen.setStyle(
                "-fx-background-image: url('/images/botao_fullscreen.png');" +
                        "-fx-background-size: contain;" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-cursor: hand;" +
                        "-fx-background-color: transparent;"
        );
        btnFullScreen.setOnAction(e -> {
            primaryStage.setFullScreen(true);
        });

        linhaEcra.getChildren().addAll(btnJanela, btnFullScreen);

        // CRÉDITOS
        Label lblCreditos = new Label("Desenvolvido por: Bruna Magarreiro e Gonçalo Soares\nFrogi © 2026");
        lblCreditos.setStyle("-fx-text-fill: #ffffff; -fx-font-family: 'Courier New'; -fx-font-weight: bold; -fx-alignment: center; -fx-text-alignment: center;");
        VBox.setMargin(lblCreditos, new Insets(40, 0, 0, 0));

        painelCentral.getChildren().addAll(linhaVolume, linhaEcra, lblCreditos);
        root.getChildren().addAll(camadaTop, painelCentral);
    }

    public Pane getRoot() { return root; }
}