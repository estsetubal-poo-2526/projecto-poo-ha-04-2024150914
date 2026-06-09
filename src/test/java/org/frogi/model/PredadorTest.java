package org.frogi.model;

import org.frogi.model.entidades.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PredadorTest {

    private Predador predador;
    private Partida partida;

    @BeforeEach
    void setUp() {
        Jogador jogador = new Jogador("Pedro");
        Nivel nivel = new Nivel(1, new Mapa(15, 15));

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