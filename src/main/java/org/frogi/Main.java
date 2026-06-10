package org.frogi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.frogi.model.*;
import org.frogi.model.entidades.*;
import org.frogi.model.powerups.Salto;
import org.frogi.view.JanelaJogo;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Versao Basica

        Jogador jogador = new Jogador("Bruna");

        nivel1(primaryStage, jogador);

    }

    public void nivel1(Stage primaryStage, Jogador jogador){
        // NIVEL 1
        Mapa mapaNivel1 = new Mapa(15, 10);

        mapaNivel1.adicionarEntidade(new Grilo(3, 2));
        mapaNivel1.adicionarEntidade(new Grilo(5, 5));
        mapaNivel1.adicionarEntidade(new Predador(7, 4));
        mapaNivel1.adicionarEntidade(new Salto(2, 6));
        mapaNivel1.adicionarEntidade(new Princesa(9, 9));

        Nivel nivel1 = new Nivel(1, mapaNivel1, "Nível 1 - Aprender a jogar");

        Partida partida = new Partida(jogador, nivel1);
        partida.iniciarPartida();

        // 2. Criar a View
        JanelaJogo janelaJogoNivel1 = new JanelaJogo(partida, 1);

        // 3. Criar a Scene
        Scene scene = new Scene(janelaJogoNivel1.getGridPane());

        // Capturar as teclas do teclado
        scene.setOnKeyPressed(event -> {
            // Se o sapo estiver morto, não faz nada
            if (!partida.getSapo().isVivo()) return;

            switch (event.getCode()) {
                case W, UP ->    partida.moverSapo(0, -1); // Cima (Y diminui)
                case S, DOWN ->  partida.moverSapo(0, 1);  // Baixo (Y aumenta)
                case A, LEFT ->  partida.moverSapo(-1, 0); // Esquerda (X diminui)
                case D, RIGHT -> partida.moverSapo(1, 0);  // Direita (X aumenta)
                default -> { return; } // Ignora outras teclas
            }

            // Depois de mover o modelo, redesenha o mapa na interface gráfica
            janelaJogoNivel1.atualizarMapa();
        });

        primaryStage.setTitle("Frogi - Nivel 1");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}