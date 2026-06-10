package org.frogi.model;

import org.frogi.model.entidades.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GriloTest {

    private Grilo grilo;
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
        grilo = new Grilo(5, 5);
    }

    @Test
    void testInteragir() {
        assertEquals(0, partida.getGrilosApanhados());

        grilo.interagir(partida);

        assertEquals(1, partida.getGrilosApanhados());
    }

    @Test
    void testPosicao() {
        assertEquals(5, grilo.getPosicaoX());
        assertEquals(5, grilo.getPosicaoY());
    }
}