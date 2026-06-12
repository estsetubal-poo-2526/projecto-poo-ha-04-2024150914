package org.frogi.model;

import org.frogi.model.entidades.Sapo;
import org.frogi.model.exceptions.EstadoSapoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SapoTest {

    private Sapo sapo;

    @BeforeEach
    void setUp() {
        // Inicia o sapo em (5, 5), vivo, pequeno e com 0 grilos
        sapo = new Sapo(5, 5);
    }

    @Test
    void testInicializacaoGaranteEstadoPadrão() {
        assertTrue(sapo.isVivo());
        assertEquals(FaseSapo.PEQUENO, sapo.getFaseAtual());
        assertEquals(0, sapo.getGrilosConsumidos());
        assertEquals(5, sapo.getPosicaoX());
        assertEquals(5, sapo.getPosicaoY());
    }

    @Test
    void testEvolucaoERegressaoDeFasesNosLimitesExatos() {
        // 0 a 6 grilos -> PEQUENO
        for (int i = 0; i < 6; i++) {
            sapo.consumirGrilo();
        }
        assertEquals(FaseSapo.PEQUENO, sapo.getFaseAtual());

        // 7 grilos -> Fronteira para MEDIO
        sapo.consumirGrilo();
        assertEquals(FaseSapo.MEDIO, sapo.getFaseAtual());
        assertEquals(7, sapo.getGrilosConsumidos());

        // Avança até 14 grilos -> Continua MEDIO
        for (int i = 0; i < 7; i++) {
            sapo.consumirGrilo();
        }
        assertEquals(FaseSapo.MEDIO, sapo.getFaseAtual());

        // 15 grilos -> Fronteira para GRANDE
        sapo.consumirGrilo();
        assertEquals(FaseSapo.GRANDE, sapo.getFaseAtual());

        // Perder grilos
        sapo.perderGrilos(10);
        assertEquals(5, sapo.getGrilosConsumidos());
        assertEquals(FaseSapo.PEQUENO, sapo.getFaseAtual(), "O sapo deveria ter encolhido para PEQUENO.");
    }

    @Test
    void testMovimentacaoComSucesso() {
        sapo.mover(3, -2);
        assertEquals(8, sapo.getPosicaoX());
        assertEquals(3, sapo.getPosicaoY());
    }

    @Test
    void testCicloDeVidaValido() {
        sapo.morrer();
        assertFalse(sapo.isVivo(), "O sapo deveria estar morto.");

        sapo.reviver(0, 1);
        assertTrue(sapo.isVivo(), "O sapo deveria estar vivo novamente.");
        assertEquals(0, sapo.getPosicaoX());
        assertEquals(1, sapo.getPosicaoY());
    }

    @Test
    void testViolacaoDeEstadoDeveLancarEstadoSapoInvalidoException() {
        // Tenta reviver um sapo que já está vivo
        assertThrows(EstadoSapoInvalidoException.class, () -> {
            sapo.reviver(0, 0);
        }, "Deveria proibir reviver quem já está vivo.");

        // Sapo morto
        sapo.morrer();

        // Tenta matar e já está morto
        assertThrows(EstadoSapoInvalidoException.class, () -> {
            sapo.morrer();
        }, "Deveria proibir matar um sapo já morto.");

        // Tentar mover um sapo morto
        assertThrows(EstadoSapoInvalidoException.class, () -> {
            sapo.mover(1, 0);
        }, "Um sapo morto não se pode mover.");

        // Tentar comer grilos estando morto
        assertThrows(EstadoSapoInvalidoException.class, () -> {
            sapo.consumirGrilo();
        }, "Um sapo morto não pode comer.");
    }

    @Test
    void testPerderGrilosValidaParametros() {
        assertThrows(IllegalArgumentException.class, () -> {
            sapo.perderGrilos(-5);
        }, "Não é possível perder uma quantidade negativa de grilos.");

        // Garante que a perda nunca deixa o contador abaixo de zero (Math.max)
        sapo.consumirGrilo(); // tem 1 grilo
        sapo.perderGrilos(10); // tenta tirar 10
        assertEquals(0, sapo.getGrilosConsumidos(), "O contador de grilos nunca pode ser negativo.");
    }
}