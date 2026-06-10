package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private Partida partida;
    private Jogador jogador;
    private Nivel nivel;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("TestPlayer");
        List<Integer> rios = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufares = {
                {2, 2}, {2, 6}, {4, 3}, {4, 9}, {6, 0}, {6, 2},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        Mapa mapa = new Mapa(rios, nenufares);
        nivel = new Nivel(1, mapa);
        partida = new Partida(jogador, nivel);
    }

    @Test
    void testInicializacao() {
        assertEquals(3, partida.getVidasRestantes());
        assertFalse(partida.isTerminada());
        assertNotNull(partida.getSapo());
    }

    @Test
    void testPerderVida() {
        int vidasIniciais = partida.getVidasRestantes();
        partida.perderVida();
        assertEquals(vidasIniciais - 1, partida.getVidasRestantes());
    }

    @Test
    void testAdicionarGrilo() {
        partida.adicionarGrilo();
        assertEquals(1, partida.getGrilosApanhados());
    }
}