package org.frogi.model;

import org.frogi.model.entidades.Grilo;
import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapaTest {

    private Mapa mapa;
    private Sapo sapo;

    @BeforeEach
    void setUp() {
        List<Integer> rios = List.of(2, 4, 6, 8, 10, 12);
        int[][] nenufares = {
                {2, 2}, {2, 6}, {4, 3}, {4, 9}, {6, 0}, {6, 2},
                {8, 5}, {8, 9}, {10, 0}, {10, 2}, {10, 7}, {12, 1}, {12, 3}
        };
        mapa = new Mapa(rios, nenufares);
        sapo = new Sapo(5, 5);
        mapa.adicionarEntidade(sapo);
    }

    @Test
    void testPosicaoValida() {
        assertTrue(mapa.isPosicaoValida(10, 9));
        assertFalse(mapa.isPosicaoValida(-1, 5));
        assertFalse(mapa.isPosicaoValida(20, 5));
    }

    @Test
    void testAdicionarERemoverEntidade() {
        Grilo grilo = new Grilo(7, 7);
        mapa.adicionarEntidade(grilo);
        assertTrue(mapa.getEntidades().contains(grilo));

        mapa.removerEntidade(grilo);
        assertFalse(mapa.getEntidades().contains(grilo));
    }
}