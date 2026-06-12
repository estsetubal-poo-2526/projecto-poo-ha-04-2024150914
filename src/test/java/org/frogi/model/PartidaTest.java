package org.frogi.model;

import org.frogi.model.exceptions.PosicaoInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private Partida partida;
    private Jogador jogador;
    private Nivel nivel;
    private Mapa mapa;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("TestPlayer");

        // Rio na coluna 2. Nenúfar apenas em (2, 1)
        List<Integer> rios = List.of(2);
        int[][] nenufares = {{2, 1}};

        mapa = new Mapa(rios, nenufares);
        nivel = new Nivel(1, mapa);
        partida = new Partida(jogador, nivel);
        partida.iniciarPartida();
    }

    @Test
    void testInicializacaoGaranteEstadoCorreto() {
        assertEquals(3, partida.getVidasRestantes(), "A partida deve começar com exatamente 3 vidas.");
        assertFalse(partida.isTerminada(), "A partida não deve começar terminada.");
        assertFalse(partida.isVenceu(), "A partida não deve começar com vitória.");
        assertEquals(0, partida.getGrilosApanhados(), "O contador de grilos deve começar a zero.");
        assertEquals(0, partida.getXSapo());
        assertEquals(1, partida.getYSapo());
    }

    @Test
    void testMoverSapoParaTerrenoSeguro() {
        // Sapo move-se de (0,1) para (1,1) - coluna 1 não é rio
        partida.moverSapo(1, 0);

        assertEquals(1, partida.getXSapo());
        assertEquals(1, partida.getYSapo());
        assertEquals(3, partida.getVidasRestantes(), "O sapo não deve perder vida em terreno seguro.");
    }

    @Test
    void testMoverSapoParaCimaDeNenufarNaoTiraVida() {
        // Sapo move-se de (0,1) para (2,1) - coluna 2 é rio, mas (2,1) tem um nenúfar
        partida.moverSapo(2, 0);

        assertEquals(2, partida.getXSapo());
        assertEquals(1, partida.getYSapo());
        assertEquals(3, partida.getVidasRestantes(), "O sapo deve sobreviver se aterrar em cima de um nenúfar.");
    }

    @Test
    void testMoverSapoParaRioSemNenufarProvocaMorte() {
        // Move o sapo para (1,1) e depois para (2,2) - rio sem nenúfar, o nenúfar está em (2,1)
        partida.moverSapo(1, 0);
        partida.moverSapo(1, 1);

        // O sapo cai na água, morre e faz respawn na casa inicial (0, 1)
        assertEquals(2, partida.getVidasRestantes(), "O sapo deveria ter perdido uma vida ao cair ao rio.");
        assertEquals(0, partida.getXSapo(), "Após morrer, o sapo deve reaparecer no SPAWN_X.");
        assertEquals(1, partida.getYSapo(), "Após morrer, o sapo deve reaparecer no SPAWN_Y.");
    }

    @Test
    void testMoverSapoParaForaDoMapaLancaExcecao() {
        assertThrows(PosicaoInvalidaException.class, () -> {
            partida.moverSapo(-1, 0);
        }, "Deveria lançar PosicaoInvalidaException ao tentar sair do mapa pela esquerda.");
    }

    @Test
    void testAdicionarERemoverGrilosAoSapoNaPartida() {
        partida.adicionarGrilo();
        partida.adicionarGrilo();
        assertEquals(2, partida.getGrilosApanhados());

        partida.removerGrilos(1);
        assertEquals(1, partida.getGrilosApanhados());
    }

    @Test
    void testSistemaDePausaNaoGeraTempoDeJogo() throws InterruptedException {
        // Regista o início da pausa
        partida.registarInicioPausa();

        // Simula uma paragem de 1 segundo e 100 milissegundos
        Thread.sleep(1100);

        // Ajusta o tempo após a pausa terminar
        partida.ajustarTempoPosPausa();

        // Como o jogo esteve pausado durante esse segundo, o tempo decorrido deve continuar a 0!
        assertEquals(0, partida.getTempoDecorrido(), "O tempo passado em pausa deve ser completamente ignorado.");
    }

    @Test
    void testReiniciarPartidaRestauraValoresIniciais() {
        partida.adicionarGrilo();
        partida.perderVida();

        assertEquals(2, partida.getVidasRestantes());
        assertEquals(1, partida.getGrilosApanhados());

        partida.reiniciarPartida();

        assertEquals(3, partida.getVidasRestantes());
        assertEquals(0, partida.getGrilosApanhados());
        assertFalse(partida.isTerminada());
    }

    @Test
    void testMetodosDefensivosRejeitamNulos() {
        assertThrows(IllegalArgumentException.class, () -> new Partida(null, nivel));
        assertThrows(IllegalArgumentException.class, () -> new Partida(jogador, null));
        assertThrows(IllegalArgumentException.class, () -> partida.setNivel(null));
        assertThrows(IllegalArgumentException.class, () -> partida.avancarParaProximoNivel(null));
    }
}