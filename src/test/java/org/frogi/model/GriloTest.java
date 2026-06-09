package org.frogi.model;

import org.frogi.model.entidades.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GriloTest {

    private Grilo grilo;
    private Partida partida;

    @BeforeEach
    void setUp() {
        Jogador jogador = new Jogador("Pedro");
        Nivel nivel = new Nivel(1, new Mapa(15, 15));

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