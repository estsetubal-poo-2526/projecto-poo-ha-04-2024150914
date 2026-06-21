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

        // Botão Voltar
        Button btnVoltar = new Button();
        EstiloBotao.estilizarBotaoComImagem(btnVoltar, "/images/botao_voltar_atras.png", "Voltar", 60, 40);
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
        StackPane.setMargin(btnVoltar, new Insets(20));

        root.getChildren().add(btnVoltar);
    }

    public Pane getRoot() { return root; }
}