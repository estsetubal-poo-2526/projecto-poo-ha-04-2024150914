package org.frogi.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Objects;

public class GameOverScreen {
    private final StackPane root;

    public GameOverScreen(Runnable acaoTentarOutraVez, Runnable acaoBotaoMenu) {
        this.root = new StackPane();
        try {
            Image imgFundo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png")));
            BackgroundImage bg = new BackgroundImage(imgFundo,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(bg));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #000000;");
        }

        // Cria um painel para escurecer o background
        Region peliculaEscura = new Region();
        // O valor '0.6' define a opacidade (60% de preto)
        peliculaEscura.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        VBox conteudo = new VBox(40);
        conteudo.setAlignment(Pos.CENTER);

        // Texto de Game Over
        Text textoGameOver = new Text("GAME OVER");
        textoGameOver.setFont(Font.font("'Consolas', monospace", FontWeight.BOLD, 70));
        textoGameOver.setFill(Color.WHITE);
        textoGameOver.setStroke(Color.BLACK);
        textoGameOver.setStrokeWidth(2.0);

        // Botão Tentar Outra Vez
        Button botaoReiniciar = new Button();
        EstiloBotao.estilizarBotaoComImagem(botaoReiniciar, "/images/botao_tentar_outra_vez.png", "Tentar outra vez", 181, 82.42);
        botaoReiniciar.setOnAction(event -> acaoTentarOutraVez.run());

        // Botão Voltar ao Menu
        Button botaoMenu = new Button();
        EstiloBotao.estilizarBotaoComImagem(botaoMenu, "/images/botao_menu.png", "Menu", 181, 82.42);
        botaoMenu.setOnAction(event -> acaoBotaoMenu.run());

        conteudo.getChildren().addAll(textoGameOver, botaoReiniciar, botaoMenu);

        this.root.getChildren().addAll(peliculaEscura, conteudo);
    }

    public StackPane getRoot() {
        return root;
    }
}