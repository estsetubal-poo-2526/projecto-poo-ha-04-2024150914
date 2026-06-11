package org.frogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardTest {

    private Leaderboard leaderboard;

    @BeforeEach
    void setUp() {
        leaderboard = new Leaderboard();
    }

    @Test
    void testAdicionarResultado() {
        Jogador jogador = new Jogador("Pedro");

        ResultadoPartida resultado =
                new ResultadoPartida(jogador, 10, 100, 3);

        leaderboard.adicionarResultado(resultado);

        assertEquals(1, leaderboard.getResultados().size());
        assertTrue(leaderboard.getResultados().contains(resultado));
    }

    @Test
    void testOrdenarResultados() {

        Jogador jogador1 = new Jogador("Pedro");
        Jogador jogador2 = new Jogador("Ana");

        ResultadoPartida resultado1 =
                new ResultadoPartida(jogador1, 5, 100, 1);

        ResultadoPartida resultado2 =
                new ResultadoPartida(jogador2, 20, 50, 3);

        leaderboard.adicionarResultado(resultado1);
        leaderboard.adicionarResultado(resultado2);

        assertEquals(resultado2, leaderboard.getResultados().get(0));
        assertEquals(resultado1, leaderboard.getResultados().get(1));
    }

    @Test
    void testGetTop10() {

        for (int i = 1; i <= 15; i++) {

            Jogador jogador = new Jogador("Jogador" + i);

            ResultadoPartida resultado =
                    new ResultadoPartida(jogador, i, 100, 1);

            leaderboard.adicionarResultado(resultado);
        }

        assertEquals(10, leaderboard.getTop10().size());
    }

    @Test
    void testGetResultadosImutavel() {

        Jogador jogador = new Jogador("Pedro");

        ResultadoPartida resultado =
                new ResultadoPartida(jogador, 10, 100, 2);

        leaderboard.adicionarResultado(resultado);

        assertThrows(
                UnsupportedOperationException.class,
                () -> leaderboard.getResultados().add(resultado)
        );
    }
}