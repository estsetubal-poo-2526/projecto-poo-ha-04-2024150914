package org.frogi.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.frogi.model.Leaderboard;
import org.frogi.model.ResultadoPartida;

import java.util.Objects;

public class LeaderboardScreen {
    private final StackPane root;
    private final Runnable onVoltarMenu;
    private final Leaderboard leaderboardModelo; // Referência para aceder aos dados do modelo

    public LeaderboardScreen(Runnable onVoltarMenu, Leaderboard leaderboardModelo) {
        this.root = new StackPane();
        this.onVoltarMenu = onVoltarMenu;
        this.leaderboardModelo = leaderboardModelo;
        configurarLayout();
    }

    private void configurarLayout() {
        // Imagem de Fundo
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/background.png")));
            root.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #3e2723;");
        }

        // Criar a Tabela JavaFX configurada para objetos ResultadoPartida
        TableView<ResultadoPartida> tabela = new TableView<>();
        tabela.setMaxSize(550, 380); // Tamanho ideal para se alinhar com a caixa do mockup

        // Estilo CSS: Fundo semi-transparente bege, texto castanho escuro estilo madeira
        tabela.setStyle("-fx-background-color: rgba(230, 204, 178, 0.6); " +
                "-fx-base: #dee2e6; " +
                "-fx-table-cell-fill: #4a3728; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold;");

        // --- COLUNA 1: NOME ---
        TableColumn<ResultadoPartida, String> colNome = new TableColumn<>("Jogador");
        colNome.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getJogador().getNome())
        );
        colNome.setPrefWidth(180);

        // --- COLUNA 2: TEMPO (Formatado em mm:ss) ---
        TableColumn<ResultadoPartida, String> colTempo = new TableColumn<>("Tempo");
        colTempo.setCellValueFactory(data -> {
            int totalSegundos = data.getValue().getTempoDecorrido();
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            String tempoFormatado = String.format("%02d:%02d", minutos, segundos);
            return new SimpleStringProperty(tempoFormatado);
        });
        colTempo.setPrefWidth(160);

        // --- COLUNA 3: PONTUACAO (Chama o método calcularPontuacao) ---
        TableColumn<ResultadoPartida, String> colScore = new TableColumn<>("Pontuação");
        colScore.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().calcularPontuacao()))
        );
        colScore.setPrefWidth(180);

        // Adicionar as colunas estruturadas à tabela
        tabela.getColumns().addAll(colNome, colTempo, colScore);

        // Injetar na tabela apenas o Top 10 ordenado que a classe Leaderboard calcula
        tabela.getItems().addAll(leaderboardModelo.getTop10());

        // Criar o Botão de Voltar (Seta)
        Button btnVoltar = new Button("↩");
        btnVoltar.setStyle("-fx-background-color: #5d4037; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16; -fx-cursor: hand;");
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        // --- POSICIONAMENTO E MARGENS ---
        StackPane.setAlignment(btnVoltar, Pos.TOP_RIGHT);
        StackPane.setMargin(btnVoltar, new Insets(20));

        // Centraliza a tabela e empurra-a um pouco para baixo para não tapar o título "LeaderBoard" desenhado no PNG
        StackPane.setAlignment(tabela, Pos.CENTER);
        StackPane.setMargin(tabela, new Insets(100, 0, 0, 0));

        // Juntar os componentes visuais ao ecrã
        root.getChildren().addAll(tabela, btnVoltar);
    }

    public Pane getRoot() { return root; }
}