package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.FaseSapo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SapoTest {

    private Sapo sapo;

    @BeforeEach
    void setUp() {
        sapo = new Sapo(5, 5);
    }

    @Test
    void testEvolucaoFases() {
        assertEquals(FaseSapo.PEQUENO, sapo.getFaseAtual());

        for(int i = 0; i < 8; i++)
            sapo.consumirGrilo();
        assertEquals(FaseSapo.MEDIO, sapo.getFaseAtual());

        for(int i = 0; i < 10; i++)
            sapo.consumirGrilo();
        assertEquals(FaseSapo.GRANDE, sapo.getFaseAtual());
    }

    @Test
    void testMovimentacao() {
        sapo.mover(3, -2);
        assertEquals(8, sapo.getPosicaoX());
        assertEquals(3, sapo.getPosicaoY());
    }

    @Test
    void testMorteReviver() {
        sapo.morrer();
        assertFalse(sapo.isVivo());
        sapo.reviver();
        assertTrue(sapo.isVivo());
    }
}