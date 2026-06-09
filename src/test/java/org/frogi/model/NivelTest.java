package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NivelTest {

    private Nivel nivel;
    private Mapa mapa;

    @BeforeEach
    void setUp() {
        mapa = new Mapa(15, 15);
        nivel = new Nivel(1, mapa);
    }

    @Test
    void testGetNumero() {
        assertEquals(1, nivel.getNumero());
    }

    @Test
    void testGetNome() {
        assertEquals("Nível 1", nivel.getNome());
    }

    @Test
    void testGetMapa() {
        assertEquals(mapa, nivel.getMapa());
    }

    @Test
    void testPosicaoValida() {
        assertTrue(nivel.isPosicaoValida(10, 10));
        assertFalse(nivel.isPosicaoValida(-1, 5));
        assertFalse(nivel.isPosicaoValida(20, 20));
    }

    @Test
    void testVerificarVitoria() {
//        Sapo sapo = new Sapo(1, 1);
//
//        assertFalse(nivel.verificarVitoria(sapo));
    }
}