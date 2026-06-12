package org.frogi.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private final Leaderboard leaderboardModelo;

    public LeaderboardScreen(Runnable onVoltarMenu, Leaderboard leaderboardModelo) {
        this.root = new StackPane();
        this.onVoltarMenu = onVoltarMenu;
        this.leaderboardModelo = leaderboardModelo;
        configurarLayout();
    }

    private void configurarLayout() {
        // Imagem de Fundo
        try {
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png")));
            root.setBackground(new Background(new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true))));
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #3e2723;");
        }

        // Tabela para objetos ResultadoPartida
        TableView<ResultadoPartida> tabela = new TableView<>();
        tabela.setMaxSize(580, 380);

        // --- ESTILO CSS ---
        // Quando a tabela está vazia
        Label mensagemVazia = new Label("Nenhum principe foi salvo ainda...");
        mensagemVazia.setStyle(
                "-fx-text-fill: #5c4033; " +
                        "-fx-font-family: 'Consolas', 'Courier New', monospace; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-style: italic;"
        );
        tabela.setPlaceholder(mensagemVazia);
        // Configuração principal da tabela (fundo bege semi-transparente, cantos arredondados e fonte retro)
        tabela.setStyle(
                "-fx-background-color: rgba(245, 222, 179, 0.5); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #5c4033; " +
                        "-fx-border-width: 3px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-padding: 5px; " +
                        "-fx-font-family: 'Consolas', 'Courier New', monospace; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold;"
        );

        // Limpar e pintar os sub-componentes da TableView
        String cssInterno =
                ".table-view .column-header-background { -fx-background-color: transparent; }" +
                        ".table-view .column-header { -fx-background-color: rgba(92, 64, 51, 0.15); -fx-size: 35px; -fx-border-color: transparent transparent #5c4033 transparent; }" +
                        ".table-view .column-header .label { -fx-text-fill: #4a3525; -fx-alignment: center; }" +
                        ".table-view .table-row-cell { -fx-background-color: transparent; -fx-text-background-color: #3d2314; }" +
                        ".table-view .table-row-cell:filled:hover { -fx-background-color: rgba(92, 64, 51, 0.12); }" +
                        ".table-cell { -fx-border-color: transparent; -fx-alignment: center; }" +
                        ".table-view .scroll-bar:vertical { -fx-background-color: transparent; }" +
                        ".table-view .scroll-bar:vertical .thumb { -fx-background-color: #a88464; -fx-background-radius: 5px; }" +
                        ".table-view .scroll-bar:horizontal { -fx-opacity: 0; -fx-max-height: 0; }";

        tabela.getStylesheets().add("data:text/css," + cssInterno.replaceAll(" ", "%20"));

        // Torna o redimensionamento das colunas automático e proporcional (Impede que quebre ou desalinhe em Full Screen)
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // COLUNA 1: JOGADOR
        TableColumn<ResultadoPartida, String> colNome = new TableColumn<>("Jogador/a");
        colNome.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getJogador().getNome())
        );

        // COLUNA 2: TEMPO em mm:ss
        TableColumn<ResultadoPartida, String> colTempo = new TableColumn<>("Tempo");
        colTempo.setCellValueFactory(data -> {
            int totalSegundos = data.getValue().getTempoDecorrido();
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            return new SimpleStringProperty(String.format("%02d:%02d", minutos, segundos));
        });

        // COLUNA 3: GRILOS
        TableColumn<ResultadoPartida, String> colGrilos = new TableColumn<>("Grilos");
        colGrilos.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getGrilosApanhados()))
        );

        // COLUNA 4: PONTUACAO
        TableColumn<ResultadoPartida, String> colScore = new TableColumn<>("Pontuação");
        colScore.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().calcularPontuacao()))
        );

        tabela.getColumns().addAll(colNome, colTempo, colGrilos, colScore);

        tabela.getItems().addAll(leaderboardModelo.getTop10());

        // Botão de voltar no canto superior esquerdo
        Button btnVoltar = new Button();
        EstiloBotao.estilizarBotaoComImagem(btnVoltar, "/images/botao_voltar_atras.png", "Voltar", 60, 40);
        btnVoltar.setOnAction(e -> onVoltarMenu.run());

        StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
        StackPane.setMargin(btnVoltar, new Insets(20));

        StackPane.setAlignment(tabela, Pos.CENTER);
        StackPane.setMargin(tabela, new Insets(90, 40, 40, 40));

        root.getChildren().addAll(tabela, btnVoltar);
    }

    public Pane getRoot() { return root; }
}