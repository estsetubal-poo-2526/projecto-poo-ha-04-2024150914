package org.frogi.model;

import org.frogi.model.entidades.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PredadorTest {

    private Predador predador;
    private Partida partida;

    @BeforeEach
    void setUp() {
        Jogador jogador = new Jogador("Pedro");
        List<Integer> rios = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufares = {
                {2, 2}, {2, 6}, {4, 3}, {4, 9}, {6, 0}, {6, 2},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        Nivel nivel = new Nivel(1, new Mapa(rios, nenufares));

        partida = new Partida(jogador, nivel);
        predador = new Predador(3, 3);
    }

    @Test
    void testInteragir() {
        int vidasAntes = partida.getVidasRestantes();

        predador.interagir(partida);

        assertEquals(vidasAntes - 1, partida.getVidasRestantes());
    }

    @Test
    void testVelocidade() {
        assertEquals(1, predador.getVelocidade());
    }

    @Test
    void testPosicao() {
        assertEquals(3, predador.getPosicaoX());
        assertEquals(3, predador.getPosicaoY());
    }
}