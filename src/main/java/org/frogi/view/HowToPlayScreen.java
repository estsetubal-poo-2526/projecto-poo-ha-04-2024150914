package org.frogi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.Objects;

public class HowToPlayScreen {
    private final StackPane root;
    private final Runnable onVoltarMenu;

    public HowToPlayScreen(Runnable onVoltarMenu) {
        this.root = new StackPane();
        this.onVoltarMenu = onVoltarMenu;
        configurarLayout();
    }

    private void configurarLayout() {
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/HowToPlayScreen.jpg")));
            root.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1b5e20;");
        }

        // Botão de voltar no canto superior direito (imitando o teu botão de sair em seta/madeira)
        Button btnVoltar = new Button("↩");
        btnVoltar.setStyle("-fx-background-color: #5d4037; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        StackPane.setAlignment(btnVoltar, Pos.TOP_RIGHT);
        StackPane.setMargin(btnVoltar, new Insets(20));

        root.getChildren().add(btnVoltar);
    }

    public Pane getRoot() { return root; }
}