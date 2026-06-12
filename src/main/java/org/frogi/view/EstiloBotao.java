package org.frogi.view;

import javafx.scene.control.Button;
import java.util.Objects;

public class EstiloBotao {

    public static void estilizarBotaoComImagem(Button botao, String caminhoImagem, String textoFallback,double  width, double height) {
        try {
            String urlImg = Objects.requireNonNull(EstiloBotao.class.getResource(caminhoImagem)).toExternalForm();
            botao.setStyle(
                    "-fx-background-image: url('" + urlImg + "');" +
                            "-fx-background-size: contain;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-color: transparent;"
            );
        } catch (Exception e) {
            // Fallback caso a imagem falhe
            botao.setText(textoFallback);
            botao.setStyle("-fx-cursor: hand; -fx-font-weight: bold;");
        }

        botao.setPrefWidth(width);
        botao.setPrefHeight(height);
        botao.setOnMouseEntered(e -> {
            botao.setScaleX(1.08);
            botao.setScaleY(1.08);
        });

        botao.setOnMouseExited(e -> {
            botao.setScaleX(1.0);
            botao.setScaleY(1.0);
        });

        botao.setOnMousePressed(e -> {
            botao.setScaleX(0.92);
            botao.setScaleY(0.92);
        });

        botao.setOnMouseReleased(e -> {
            if (botao.isHover()) {
                botao.setScaleX(1.08);
                botao.setScaleY(1.08);
            } else {
                botao.setScaleX(1.0);
                botao.setScaleY(1.0);
            }
        });
    }
}
