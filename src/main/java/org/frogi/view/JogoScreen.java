package org.frogi.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    private static final int TAMANHO_CELULA = 70;
    private final GridPane gridPane;
    private final Partida partida;
    private int nivel;

    // Componentes da Barra do Topo
    private final VBox contentorPrincipal; // Junta o topo + o mapa
    private Label lblVidas;
    private Label lblGrilos;
    private Label lblTimer;
    private long tempoInicial; // Para calcular o tempo decorrido

    // Carregar as imagens (Mantém as tuas exatamente iguais)
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

    public JogoScreen(Partida partida, int nivel) {
        this.partida = partida;
        this.gridPane = new GridPane();
        this.nivel = nivel;
        this.contentorPrincipal = new VBox();
        this.tempoInicial = System.currentTimeMillis(); // Guarda o momento em que o nível começou

        // Inicializar e construir a barra do topo
        HBox barraTopo = criarBarraTopo();

        // Junta a barra do topo e o tabuleiro de jogo no contentor vertical
        this.contentorPrincipal.getChildren().addAll(barraTopo, gridPane);

        atualizarMapa();
    }

    /**
     * Constrói o layout visual da barra de informações superior.
     */
    private HBox criarBarraTopo() {
        HBox barra = new HBox(40); // Espaçamento de 40px entre cada informação
        barra.setPadding(new Insets(10, 20, 10, 20));
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.setStyle("-fx-background-color: #2c3e50;"); // Um fundo escuro elegante

        // Inicializar as Labels com estilo
        lblVidas = new Label();
        lblGrilos = new Label();
        lblTimer = new Label();

        Font fonteStatus = Font.font("Arial", FontWeight.BOLD, 16);

        configurarLabel(lblVidas, fonteStatus);
        configurarLabel(lblGrilos, fonteStatus);
        configurarLabel(lblTimer, fonteStatus);

        barra.getChildren().addAll(lblVidas, lblGrilos, lblTimer);
        return barra;
    }

    private void configurarLabel(Label label, Font fonte) {
        label.setFont(fonte);
        label.setTextFill(Color.WHITE);
    }

    /**
     * Retorna o contentor total (Topo + Mapa) para ser colocado na Scene.
     */
    public VBox getContentorPrincipal() {
        return contentorPrincipal;
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    private Image carregarImagem(String nomeFicheiro) {
        try {
            String caminho = "/" + nomeFicheiro;
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(caminho)));
        } catch (Exception e) {
            System.out.println("[AVISO] Não foi possível carregar a imagem: " + nomeFicheiro);
            return null;
        }
    }

    /**
     * Atualiza o ecrã, incluindo os dados da barra superior.
     */
    public void atualizarMapa() {
        // --- Atualizar Informações de Texto ---
        int vidas = partida.getVidasRestantes();
        int grilosComidos = partida.getGrilosApanhados();

        // Calcula os segundos decorridos desde o início
        long segundosDeJogo = (System.currentTimeMillis() - tempoInicial) / 1000;
        long minutos = segundosDeJogo / 60;
        long segundos = segundosDeJogo % 60;

        lblVidas.setText("❤️Vidas: " + vidas);
        lblGrilos.setText("🦗Grilos: " + grilosComidos);
        lblTimer.setText(String.format("⏱️ Tempo: %02d:%02d", minutos, segundos));

        // --- Atualizar Tabuleiro ---
        gridPane.getChildren().clear();

        Mapa mapaAtual = partida.getNivelAtual().getMapa();
        int largura = mapaAtual.getLargura();
        int altura = mapaAtual.getAltura();

        List<Integer> listaRio = mapaAtual.getColunasRio();

        // 1. Desenhar o Rio e a Relva dinamicamente
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

        // 2. Desenhar os Nenúfares dinamicamente
        int[][] coordsNenufares = mapaAtual.getCoordenadasNenufares();

        for (int[] par : coordsNenufares) {
            ImageView nenufarVisual = new ImageView(imgNenufar);

            nenufarVisual.setFitWidth(TAMANHO_CELULA);
            nenufarVisual.setFitHeight(TAMANHO_CELULA);

            gridPane.add(nenufarVisual, par[0], par[1]);
        }

        // 3. Desenhar as Entidades por cima do chão
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

        // 5. Desenhar o Sapo
        if (partida.getSapo().isVivo()) {
            ImageView visualSapo = new ImageView(imgSapo);
            visualSapo.setFitWidth(TAMANHO_CELULA);
            visualSapo.setFitHeight(TAMANHO_CELULA);
            gridPane.add(visualSapo, partida.getXSapo(), partida.getYSapo());
        }
    }

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