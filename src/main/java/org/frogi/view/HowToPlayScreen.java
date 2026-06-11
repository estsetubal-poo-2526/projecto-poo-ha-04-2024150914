package org.frogi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
        // Fundo
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/HowToPlayScreen.jpg")));
            root.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1b5e20;");
        }

        VBox conteudo = new VBox(15);
        conteudo.setAlignment(Pos.CENTER);
        conteudo.setPadding(new Insets(40));
        conteudo.setMaxWidth(700);

        Text titulo = new Text("COMO JOGAR - FROGI");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titulo.setFill(Color.WHITE);

        TextFlow regras = new TextFlow();
        regras.setMaxWidth(650);

        String textoRegras = """
            🎮 OBJETIVO:
            Conduz o sapo através do pântano, comendo grilos para crescer e chegar à princesa.

            🕹️ MOVIMENTOS:
            • Setas ou WASD → Mover o sapo

            🌊 REGRAS IMPORTANTES:
            • Só podes atravessar o rio pelos nenúfares
            • Cair na água = morte
            • Predadores apanham-te = morte

            ❤️ Sistema de Vidas:
            • Começas com 3 vidas
            • Ao morrer perdes 1 vida e 1/3 dos grilos
            • 0 vidas = reinício do jogo

            🏆 VITÓRIA:
            Chega à princesa no nível final para te transformares em príncipe!
            """;

        Label lblRegras = new Label(textoRegras);
        lblRegras.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        lblRegras.setTextFill(Color.WHITE);
        lblRegras.setWrapText(true);
        lblRegras.setStyle("-fx-line-height: 1.6;");

        Button btnVoltar = new Button("↩ VOLTAR AO MENU");
        btnVoltar.setStyle("-fx-background-color: #5d4037; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18; -fx-padding: 12 30;");
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        conteudo.getChildren().addAll(titulo, lblRegras, btnVoltar);

        root.getChildren().add(conteudo);
    }

    public Pane getRoot() {
        return root;
    }
}