package org.frogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoPartidaTest {

    private ResultadoPartida resultadoNormal;
    private Jogador jogador;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("Pedro");

        // 15 grilos e 40 segundos de jogo
        // Pontuação esperada é (15 * 10) - 40 = 150 - 40 = 110
        resultadoNormal = new ResultadoPartida(jogador, 15, 40);
    }

    @Test
    void testGettersGarantemDadosCorretos() {
        assertEquals(jogador, resultadoNormal.getJogador());
        assertEquals("Pedro", resultadoNormal.getJogador().getNome());
        assertEquals(15, resultadoNormal.getGrilosApanhados());
        assertEquals(40, resultadoNormal.getTempoDecorrido());
    }

    @Test
    void testCalcularPontuacaoCenarioNormal() {
        // (15 * 10) - 40 = 110
        int pontuacaoEsperada = 110;
        assertEquals(pontuacaoEsperada, resultadoNormal.calcularPontuacao(), "A pontuação calculada está incorreta.");
    }

    @Test
    void testCalcularPontuacaoNuncaDeveSerNegativa() {
        // 2 grilos e 500 segundos de jogo
        // Pontuação é (2 * 10) - 500 = 20 - 500 = -480 mas nao deve haver pont negativas deve ficar 0
        ResultadoPartida resultadoMau = new ResultadoPartida(jogador, 2, 500);

        assertEquals(0, resultadoMau.calcularPontuacao(), "A pontuação devia ter sido travada em 0 devido ao Math.max.");
    }

    @Test
    void testToStringFormatoCorreto() {
        String esperado = "Pedro | Pontuação: 110 | Grilos: 15";
        assertEquals(esperado, resultadoNormal.toString());
    }

    @Test
    void testConstrutorDeveLancarExcecoesParaDadosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ResultadoPartida(null, 10, 60);
        }, "Deveria proibir jogador nulo.");

        assertThrows(IllegalArgumentException.class, () -> {
            new ResultadoPartida(jogador, -1, 60);
        }, "Deveria proibir grilos negativos.");

        assertThrows(IllegalArgumentException.class, () -> {
            new ResultadoPartida(jogador, 10, -5);
        }, "Deveria proibir tempo decorrido negativo.");
    }
}