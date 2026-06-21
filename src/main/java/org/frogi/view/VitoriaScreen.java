package org.frogi.view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.util.Objects;

public class VitoriaScreen {
    private final StackPane root;
    private final Runnable onFinalizarVitoria;

    public VitoriaScreen(Runnable onFinalizarVitoria) {
        this.root = new StackPane();
        this.onFinalizarVitoria = onFinalizarVitoria;

        // Preenche o ecrã de preto caso as imagens falhem
        root.setStyle("-fx-background-color: #000000;");

        try {
            // Carrega as duas imagens
            Image imgFase1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/vitoriapt1.png")));
            Image imgFase2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/vitoriapt2.png")));

            // Cria as Views e configura-as para serem responsivas
            ImageView viewFase1 = new ImageView(imgFase1);
            ImageView viewFase2 = new ImageView(imgFase2);

            configurarResponsividade(viewFase1);
            configurarResponsividade(viewFase2);

            // Adiciona ao StackPane
            // A Fase 2 (Príncipe) fica por trás, e a Fase 1 (Sapo) fica à frente tapando-a
            root.getChildren().addAll(viewFase2, viewFase1);
            iniciarSequenciaAnimacao(viewFase1);

        } catch (Exception e) {
            System.out.println("[Erro] Não foi possível carregar as imagens de vitória: " + e.getMessage());
            // Se falhar espera 3 segundos e avança para não encravar o jogo
            PauseTransition falhaPause = new PauseTransition(Duration.seconds(3));
            falhaPause.setOnFinished(event -> onFinalizarVitoria.run());
            falhaPause.play();
        }
    }

    private void configurarResponsividade(ImageView imageView) {
        imageView.setPreserveRatio(false);
        imageView.fitWidthProperty().bind(root.widthProperty());
        imageView.fitHeightProperty().bind(root.heightProperty());
    }

    private void iniciarSequenciaAnimacao(ImageView imagemSapo) {
        // Cria o Fade Out para a imagem do topo (Sapo desaparece e revela o Príncipe por trás)
        FadeTransition fadeOutSapo = new FadeTransition(Duration.seconds(2.5), imagemSapo);
        fadeOutSapo.setFromValue(1.0); // Totalmente visível
        fadeOutSapo.setToValue(0.0);   // Totalmente invisível (o sapo desaparece)

        fadeOutSapo.setDelay(Duration.seconds(0.5));

        // Espera 3 segundos com o príncipe visível
        fadeOutSapo.setOnFinished(event -> {
            PauseTransition pausaProPrincipe = new PauseTransition(Duration.seconds(3.0));
            pausaProPrincipe.setOnFinished(endEvent -> onFinalizarVitoria.run());
            pausaProPrincipe.play();
        });

        fadeOutSapo.play();
    }

    public Pane getRoot() {
        return root;
    }
}