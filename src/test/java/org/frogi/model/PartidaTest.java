package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private Partida partida;
    private Jogador jogador;
    private Nivel nivel;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("TestPlayer");
        Mapa mapa = new Mapa(20, 20);
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