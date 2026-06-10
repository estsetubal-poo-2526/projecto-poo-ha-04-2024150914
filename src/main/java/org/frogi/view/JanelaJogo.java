package org.frogi.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.frogi.model.Partida;
import org.frogi.model.entidades.*;
import org.frogi.model.powerups.PowerUp;
import org.frogi.model.powerups.Salto;
import org.frogi.model.powerups.VidaExtra;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JanelaJogo {

    private static final int TAMANHO_CELULA = 70; // Mantém o tamanho dos quadrados
    private final GridPane gridPane;
    private final Partida partida;
    private int nivel;

    // Carregar as imagens em memória a partir da pasta resources
    private final Image imgRelva = carregarImagem("relva.png");
    private final Image imgRio = carregarImagem("rio.png");
    private final Image imgNenufar = carregarImagem("nenufar.png");
    private final Image imgSinalInicio = carregarImagem("sinal_inicio.png");
    private final Image imgSinalProxNivel = carregarImagem("sinal_prox_nivel.png");
    private final Image imgVidaExtra = carregarImagem("vida_extra.png");
    private final Image imgSalto = carregarImagem("salto.png");
    private final Image imgSapo = carregarImagem("sapo.png");
    private final Image imgGrilo = carregarImagem("grilo.png");
    private final Image imgPredador = carregarImagem("predador.png");
    private final Image imgPrincesa = carregarImagem("princesa.png");

    public JanelaJogo(Partida partida, int nivel) {
        this.partida = partida;
        this.gridPane = new GridPane();
        this.nivel = nivel;
        atualizarMapa();
    }

    /**
     * Método auxiliar para carregar imagens de forma segura da pasta resources.
     */
    private Image carregarImagem(String nomeFicheiro) {
        try {
            String caminho = "/" + nomeFicheiro;
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminho)));
        } catch (Exception e) {
            System.out.println("[AVISO] Não foi possível carregar a imagem: " + nomeFicheiro);
            return null;
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Limpa o ecrã e redesenha o cenário e os bonecos usando as imagens carregadas.
     */
    public void atualizarMapa() {
        gridPane.getChildren().clear();

        int largura = partida.getNivelAtual().getMapa().getLargura();
        int altura = partida.getNivelAtual().getMapa().getAltura();

        if(this.nivel == 1) {
            // 1. Desenhar o Rio e a Relva
            for (int x = 0; x < largura; x++) {
                for (int y = 0; y < altura; y++) {
                    ImageView background = new ImageView();
                    background.setFitWidth(TAMANHO_CELULA);
                    background.setFitHeight(TAMANHO_CELULA);
                    List<Integer> listaRelva = List.of(0, 1, 3, 5, 7, 9, 11, 13, 14);
                    List<Integer> listaRio = List.of(2, 4, 6, 8, 10, 12);

                    if (listaRelva.contains(x)) {
                        background.setImage(imgRelva);
                    } else if (listaRio.contains(x)) {
                        background.setImage(imgRio);
                    }
                    gridPane.add(background, x, y);
                }
            }

            // 2. Desenhar os Nenufares
            for (int x = 0; x < largura; x++) {
                for (int y = 0; y < altura; y++) {
                    ImageView nenufares = new ImageView();
                    nenufares.setFitWidth(TAMANHO_CELULA);
                    nenufares.setFitHeight(TAMANHO_CELULA);
                    nenufares.setImage(imgNenufar);
                    List<Integer> listaRio = List.of(2, 4, 6, 8, 10, 12);
                    List<Integer> listaYNenufares = List.of(2,6);

                    if (listaRio.contains(x) && listaYNenufares.contains(y)) {
                        gridPane.add(nenufares, x, y);
                    }
                }
            }

            // 3. Desenhar as Entidades (Grilos, Predadores, etc.) por cima do chão
            for (EntidadeJogo entidade : partida.getNivelAtual().getMapa().getEntidades()) {
                ImageView visualEntidade = criarVisualEntidade(entidade);
                if (visualEntidade != null) {
                    gridPane.add(visualEntidade, entidade.getPosicaoX(), entidade.getPosicaoY());
                }
            }

            // 4. Desenhar os Sinais
            ImageView sinalInicio = new ImageView(imgSinalInicio);
            ImageView sinalProxNivel = new ImageView(imgSinalProxNivel);

            sinalInicio.setFitWidth(TAMANHO_CELULA);
            sinalInicio.setFitHeight(TAMANHO_CELULA);
            sinalProxNivel.setFitWidth(TAMANHO_CELULA);
            sinalProxNivel.setFitHeight(TAMANHO_CELULA);

            gridPane.add(sinalInicio, 0, 0);
            gridPane.add(sinalProxNivel, 14, 0);

            // 5. Desenhar o Sapo por cima de tudo
            if (partida.getSapo().isVivo()) {
                ImageView visualSapo = new ImageView(imgSapo);
                visualSapo.setFitWidth(TAMANHO_CELULA);
                visualSapo.setFitHeight(TAMANHO_CELULA);
                gridPane.add(visualSapo, partida.getXSapo(), partida.getYSapo());
            }

        }
    }


    /**
     * Associa cada classe do modelo à respetiva imagem .png
     */
    private ImageView criarVisualEntidade(EntidadeJogo entidade) {
        ImageView view = new ImageView();
        view.setFitWidth(TAMANHO_CELULA - 4);
        view.setFitHeight(TAMANHO_CELULA - 4);

        if (entidade instanceof Grilo) {
            view.setImage(imgGrilo);
        } else if (entidade instanceof Predador) {
            view.setImage(imgPredador);
        } else if (entidade instanceof Princesa) {
            view.setImage(imgPrincesa);
        } else if (entidade instanceof Salto) {
            view.setImage(imgSalto);
        } else if (entidade instanceof VidaExtra) {
            view.setImage(imgVidaExtra);
        }

        return view.getImage() != null ? view : null;
    }
}