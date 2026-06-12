package org.frogi.model;

import org.frogi.model.exceptions.NomeInvalidoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JogadorTest {

    @Test
    void testCriarJogadorComNomeValido() {
        Jogador jogador = new Jogador("Diogo");
        assertEquals("Diogo", jogador.getNome());
    }

    @Test
    void testCriarJogadorComNomeInvalidoDeveLancarExcecao() {
        assertThrows(NomeInvalidoException.class, () -> new Jogador(""));
        assertThrows(NomeInvalidoException.class, () -> new Jogador("   "));
        assertThrows(NomeInvalidoException.class, () -> new Jogador(null));
    }
}