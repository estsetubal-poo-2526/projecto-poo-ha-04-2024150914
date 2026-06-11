package org.frogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoPartidaTest {

    private ResultadoPartida resultado;

    @BeforeEach
    void setUp() {
        Jogador jogador = new Jogador("Pedro");

        resultado = new ResultadoPartida(
                jogador,
                10,
                120,
                3
        );
    }

    @Test
    void testCalcularPontuacao() {

        int pontuacaoEsperada = 10 * 10 + 3 * 100 + 500;

        assertEquals(pontuacaoEsperada, resultado.calcularPontuacao());
    }

    @Test
    void testGetJogador() {
        assertEquals("Pedro", resultado.getJogador().getNome());
    }

    @Test
    void testGetGrilosApanhados() {
        assertEquals(10, resultado.getGrilosApanhados());
    }

    @Test
    void testGetTempoDecorrido() {
        assertEquals(120, resultado.getTempoDecorrido());
    }

    @Test
    void testGetNivelAlcancado() {
        assertEquals(3, resultado.getNivelAlcancado());
    }

    @Test
    void testToString() {

        String esperado =
                "Pedro | Pontuação: 900 | Nível: 3 | Grilos: 10";

        assertEquals(esperado, resultado.toString());
    }
}