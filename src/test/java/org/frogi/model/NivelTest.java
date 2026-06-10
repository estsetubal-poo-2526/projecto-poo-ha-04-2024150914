package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NivelTest {

    private Nivel nivel;
    private Mapa mapa;

    @BeforeEach
    void setUp() {
        List<Integer> rios = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufares = {
                {2, 2}, {2, 6}, {4, 3}, {4, 9}, {6, 0}, {6, 2},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        mapa =  new Mapa(rios, nenufares);
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
        assertTrue(nivel.isPosicaoValida(10, 9));
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