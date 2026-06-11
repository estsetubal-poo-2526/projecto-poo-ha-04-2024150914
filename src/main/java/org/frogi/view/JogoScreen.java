package org.frogi.view;

import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.frogi.model.Mapa;
import org.frogi.model.Partida;
import org.frogi.model.entidades.*;
import org.frogi.model.powerups.Salto;
import org.frogi.model.powerups.VidaExtra;

import java.util.List;
import java.util.Objects;

public class JogoScreen {

    private static final int TAMANHO_CELULA = 72;
    private final GridPane gridPane;
    private final Partida partida;

    private boolean pausado = false;
    private VBox painelPausa; // O menu que vai aparecer flutuante

    // Contentor principal e componentes da Interface Flutuante
    private final StackPane contentorPrincipal;
    private HBox barraHUD;
    private HBox contentorCoracoes;
    private Label lblTimer;
    private Label lblGrilos;
    private Label lblDicaPausa;


    // Carregar as imagens
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
    private final Image imgCoracaoCheio = carregarImagem("vida_extra.png");

    public JogoScreen(Partida partida) {
        this.partida = partida;
        this.gridPane = new GridPane();
        this.contentorPrincipal = new StackPane();

        // Configuração de fundo do StackPane para envolver e centrar o tabuleiro
        this.contentorPrincipal.setStyle("-fx-background-color: #112211;");
        gridPane.setAlignment(Pos.CENTER);

        // Criar o HUD
        this.barraHUD = criarHUDFlutuante();

        // Camada 1: O tabuleiro de jogo / Camada 2: O HUD por cima
        this.contentorPrincipal.getChildren().addAll(gridPane, barraHUD);

        atualizarMapa();
    }

    private HBox criarHUDFlutuante() {
        HBox hud = new HBox();
        hud.setPadding(new Insets(15, 120, 0, 120));
        hud.setAlignment(Pos.TOP_CENTER);
        hud.setPickOnBounds(false); // Permite que os cliques passem através do HUD para o jogo

        // Região esquerda: Espaço reservado para o sinal de Start
        Region espacoEsquerda = new Region();
        HBox.setHgrow(espacoEsquerda, Priority.ALWAYS);

        // Região central: Vidas, Tempo e Grilos alinhados
        HBox centroInfo = new HBox(35);
        centroInfo.setAlignment(Pos.TOP_CENTER);

        contentorCoracoes = new HBox(4);
        contentorCoracoes.setAlignment(Pos.TOP_CENTER);

        lblTimer = new Label();
        lblGrilos = new Label();

        Font fonteHUD = Font.font("Consolas", FontWeight.BOLD, 22);
        configurarLabelHUD(lblTimer, fonteHUD);
        configurarLabelHUD(lblGrilos, fonteHUD);

        centroInfo.getChildren().addAll(contentorCoracoes, lblTimer, lblGrilos);

        // Região direita: dica do ESC alinhada à direita
        HBox direitaInfo = new HBox();
        direitaInfo.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(direitaInfo, Priority.ALWAYS);

        lblDicaPausa = new Label("[ESC] Pausa");
        Font fonteDica = Font.font("Consolas", FontWeight.BOLD, 16);
        configurarLabelHUD(lblDicaPausa, fonteDica);
        lblDicaPausa.setTextFill(Color.LIGHTGRAY);

        direitaInfo.getChildren().add(lblDicaPausa);

        hud.getChildren().addAll(espacoEsquerda, centroInfo, direitaInfo);
        return hud;
    }

    private void configurarLabelHUD(Label label, Font fonte) {
        label.setFont(fonte);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 4, 0.0, 2, 2);");
    }

    public Pane getContentorPrincipal() {
        return contentorPrincipal;
    }

    private Image carregarImagem(String nomeFicheiro) {
        try {
            String caminho = "/images/" + nomeFicheiro;
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminho)));
        } catch (Exception e) {
            System.out.println("[AVISO] Não foi possível carregar a imagem: " + nomeFicheiro);
            return null;
        }
    }

    public void atualizarMapa() {
        //Atualizar HUD Dinâmico
        long segundosDeJogo = partida.getTempoDecorrido();
        long minutos = segundosDeJogo / 60;
        long segundos = segundosDeJogo % 60;

        lblTimer.setText(String.format("Tempo: %02d:%02d", minutos, segundos));
        lblGrilos.setText("Grilos: " + partida.getGrilosApanhados());

        // Desenhar corações dinamicamente com base nas vidas restantes
        contentorCoracoes.getChildren().clear();
        int vidas = partida.getVidasRestantes();
        for (int i = 0; i < vidas; i++) {
            ImageView coracao = new ImageView(imgCoracaoCheio);
            coracao.setFitWidth(24);
            coracao.setFitHeight(24);
            contentorCoracoes.getChildren().add(coracao);
        }

        // Atualizar Grelha do Tabuleiro
        gridPane.getChildren().clear();

        Mapa mapaAtual = partida.getNivelAtual().getMapa();
        int largura = mapaAtual.getLargura();
        int altura = mapaAtual.getAltura();
        List<Integer> listaRio = mapaAtual.getColunasRio();

        // Relva e Rio
        for (int x = 0; x < largura; x++) {
            for (int y = 0; y < altura; y++) {
                ImageView background = new ImageView();
                background.setFitWidth(TAMANHO_CELULA);
                background.setFitHeight(TAMANHO_CELULA);

                if (listaRio.contains(x)) {
                    background.setImage(imgRio);
                } else {
                    background.setImage(imgRelva);
                }
                gridPane.add(background, x, y);
            }
        }

        // Nenúfares
        int[][] coordsNenufares = mapaAtual.getCoordenadasNenufares();
        for (int[] par : coordsNenufares) {
            ImageView nenufarVisual = new ImageView(imgNenufar);
            nenufarVisual.setFitWidth(TAMANHO_CELULA);
            nenufarVisual.setFitHeight(TAMANHO_CELULA);
            gridPane.add(nenufarVisual, par[0], par[1]);
        }

        // Entidades do Jogo
        for (EntidadeJogo entidade : partida.getNivelAtual().getMapa().getEntidades()) {
            ImageView visualEntidade = criarVisualEntidade(entidade);
            if (visualEntidade != null) {
                gridPane.add(visualEntidade, entidade.getPosicaoX(), entidade.getPosicaoY());
            }
        }

        // Placas
        ImageView sinalInicio = new ImageView(imgSinalInicio);
        ImageView sinalProxNivel = new ImageView(imgSinalProxNivel);
        sinalInicio.setFitWidth(TAMANHO_CELULA);
        sinalInicio.setFitHeight(TAMANHO_CELULA);
        sinalProxNivel.setFitWidth(TAMANHO_CELULA);
        sinalProxNivel.setFitHeight(TAMANHO_CELULA);

        gridPane.add(sinalInicio, 0, 0);
        if(partida.getNivelAtual().getNumero() != 3) {
            gridPane.add(sinalProxNivel, 14, 9);
        }

        // Sapo
        if (partida.getSapo().isVivo()) {
            ImageView visualSapo = new ImageView(imgSapo);
            visualSapo.setFitWidth(TAMANHO_CELULA);
            visualSapo.setFitHeight(TAMANHO_CELULA);
            gridPane.add(visualSapo, partida.getXSapo(), partida.getYSapo());
        }
    }

    private ImageView criarVisualEntidade(EntidadeJogo entidade) {
        ImageView view = new ImageView();

        if (entidade instanceof Grilo) {
            view.setImage(imgGrilo);
            // Define um tamanho mais pequeno para o grilo
            view.setFitWidth(36);
            view.setFitHeight(36);
            GridPane.setHalignment(view, javafx.geometry.HPos.CENTER);
            GridPane.setValignment(view, javafx.geometry.VPos.CENTER);
        } else {
            view.setFitWidth(TAMANHO_CELULA - 6);
            view.setFitHeight(TAMANHO_CELULA - 6);

            if (entidade instanceof Predador) {
                view.setImage(imgPredador);
            } else if (entidade instanceof Princesa) {
                view.setImage(imgPrincesa);
            } else if (entidade instanceof Salto) {
                view.setImage(imgSalto);
            } else if (entidade instanceof VidaExtra) {
                view.setImage(imgVidaExtra);
            }
        }

        return view.getImage() != null ? view : null;
    }


    public void alternarPausa(Timeline gameLoop, Runnable acaoIrParaMenu) {
        if (!pausado) {
            // PAUSAR O JOGO
            pausado = true;
            gameLoop.pause();

            partida.registarInicioPausa();

            // Cria o painel visual de pausa de forma dinâmica
            painelPausa = new VBox(20);
            painelPausa.setAlignment(Pos.CENTER);
            painelPausa.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");

            ImageView imgTituloPausa = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/jogo_pausa.png"))));
            imgTituloPausa.setFitWidth(300);
            imgTituloPausa.setPreserveRatio(true);

            // Botão para Voltar ao Jogo
            Button btnContinuar = new Button();
            btnContinuar.setPrefWidth(181);
            btnContinuar.setPrefHeight(82.42);
            btnContinuar.setStyle(
                    "-fx-background-image: url('/images/botao_continuar.png');" +
                            "-fx-background-size: contain;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-color: transparent;" // Remove o cinzento nativo
            );
            btnContinuar.setOnAction(e -> alternarPausa(gameLoop, acaoIrParaMenu)); // Despausa

            // Botão para Sair para o Menu Principal
            Button btnSair = new Button();
            btnSair.setPrefWidth(181);
            btnSair.setPrefHeight(82.42);
            btnSair.setStyle(
                    "-fx-background-image: url('/images/botao_menu.png');" +
                            "-fx-background-size: contain;" +
                            "-fx-background-repeat: no-repeat;" +
                            "-fx-background-position: center;" +
                            "-fx-cursor: hand;" +
                            "-fx-background-color: transparent;" // Remove o cinzento nativo
            );
            btnSair.setOnAction(e -> {
                gameLoop.stop(); // Garante que a timeline morre de vez
                acaoIrParaMenu.run(); // Executa o método mostrarMenu() do Main
            });

            painelPausa.getChildren().addAll(imgTituloPausa, btnContinuar, btnSair);

            // Adiciona o menu de pausa na camada da frente do StackPane
            contentorPrincipal.getChildren().add(painelPausa);
        } else {
            // RETOMAR O JOGO
            pausado = false;

            // Remove o menu de pausa da tela
            contentorPrincipal.getChildren().remove(painelPausa);

            //Desconta o tempo que esteve em pausa
            partida.ajustarTempoPosPausa();

            gameLoop.play(); // Descongela a Timeline
        }
    }
}