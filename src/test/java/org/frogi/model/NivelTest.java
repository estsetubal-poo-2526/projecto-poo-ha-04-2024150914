package org.frogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NivelTest {

    private Nivel nivel;
    private Mapa mapa;

    @BeforeEach
    void setUp() {
        List<Integer> rios = List.of(2, 4);
        int[][] nenufares = {{2, 2}, {4, 3}};
        mapa = new Mapa(rios, nenufares);
        nivel = new Nivel(1, mapa);
    }

    @Test
    void testGettersEConstrutorPadrao() {
        assertEquals(1, nivel.getNumero());
        assertEquals("Nível 1", nivel.getNome(), "O construtor padrão deve gerar o nome automaticamente.");
        assertEquals(mapa, nivel.getMapa());
    }

    @Test
    void testConstrutorCompletoComNomePersonalizado() {
        Nivel nivelEspecial = new Nivel(2, mapa, "Pântano Assombrado");
        assertEquals(2, nivelEspecial.getNumero());
        assertEquals("Pântano Assombrado", nivelEspecial.getNome());
    }

    @Test
    void testDelegacaoDePosicaoValidaParaOMapa() {
        assertTrue(nivel.isPosicaoValida(0, 0));
        assertTrue(nivel.isPosicaoValida(14, 9));
        assertFalse(nivel.isPosicaoValida(-1, 5));
        assertFalse(nivel.isPosicaoValida(15, 10));
    }

    @Test
    void testProcessarInteracoesValidaParametros() {
        // Garante que o nível não aceita uma partida nula nas interações
        assertThrows(IllegalArgumentException.class, () -> {
            nivel.processarInteracoes(null);
        }, "Deveria lançar IllegalArgumentException para partida nula.");
    }

    @Test
    void testConstrutoresDevemLancarExcecoesParaDadosInvalidos() {
        // Número de nível inválido
        assertThrows(IllegalArgumentException.class, () -> {
            new Nivel(0, mapa);
        }, "Nível zero deve ser proibido.");

        assertThrows(IllegalArgumentException.class, () -> {
            new Nivel(-5, mapa);
        }, "Nível negativo deve ser proibido.");

        // Mapa nulo
        assertThrows(IllegalArgumentException.class, () -> {
            new Nivel(1, null);
        }, "O mapa não pode ser nulo.");

        // Nomes inválidos no construtor completo
        assertThrows(IllegalArgumentException.class, () -> {
            new Nivel(1, mapa, null);
        }, "O nome não pode ser nulo.");

        assertThrows(IllegalArgumentException.class, () -> {
            new Nivel(1, mapa, "   ");
        }, "O nome não pode ser uma String vazia ou cheia de espaços.");
    }
}