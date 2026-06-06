package org.frogi.model;

import org.frogi.model.entidades.Grilo;
import org.frogi.model.entidades.Sapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapaTest {

    private Mapa mapa;
    private Sapo sapo;

    @BeforeEach
    void setUp() {
        mapa = new Mapa(15, 15);
        sapo = new Sapo(5, 5);
        mapa.adicionarEntidade(sapo);
    }

    @Test
    void testPosicaoValida() {
        assertTrue(mapa.isPosicaoValida(10, 10));
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