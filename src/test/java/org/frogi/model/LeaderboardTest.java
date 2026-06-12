package org.frogi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardTest {

    private Leaderboard leaderboard;
    private String caminhoTemporario;

    @BeforeEach
    void setUp(@TempDir Path pastaTemporaria) {
        Path ficheiroTeste = pastaTemporaria.resolve("leaderboard_teste.txt");
        caminhoTemporario = ficheiroTeste.toAbsolutePath().toString();

        // Instancia o leaderboard a apontar exclusivamente para o ficheiro temporário
        leaderboard = new Leaderboard(caminhoTemporario);
    }

    @Test
    void testAdicionarResultadoComSucesso() {
        Jogador jogador = new Jogador("Pedro");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 10, 100);

        leaderboard.adicionarResultado(resultado, caminhoTemporario);

        assertEquals(1, leaderboard.getResultados().size());
        assertTrue(leaderboard.getResultados().contains(resultado));

        File f = new File(caminhoTemporario);
        assertTrue(f.exists(), "O ficheiro deve ser fisicamente criado.");
        assertTrue(f.length() > 0, "O ficheiro não deve estar vazio.");
    }

    @Test
    void testAdicionarResultadoValidaParametros() {
        Jogador jogador = new Jogador("Pedro");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 10, 100);

        // Valida se rejeita nulos ou strings em branco
        assertThrows(IllegalArgumentException.class, () ->
                leaderboard.adicionarResultado(null, caminhoTemporario)
        );

        assertThrows(IllegalArgumentException.class, () ->
                leaderboard.adicionarResultado(resultado, "   ")
        );
    }

    @Test
    void testOrdenarResultados() {
        Jogador jogador1 = new Jogador("Pedro");
        Jogador jogador2 = new Jogador("Ana");

        // Configura pontuações diferentes
        // Ana: 20 * 10 - 50 = 150 pts (Melhor)
        // Pedro: 5 * 10 - 100 = 0 pts (Pior)
        ResultadoPartida resultado1 = new ResultadoPartida(jogador1, 5, 100);
        ResultadoPartida resultado2 = new ResultadoPartida(jogador2, 20, 50);

        leaderboard.adicionarResultado(resultado1, caminhoTemporario);
        leaderboard.adicionarResultado(resultado2, caminhoTemporario);

        assertEquals(resultado2, leaderboard.getResultados().get(0), "O melhor resultado deve ficar em primeiro.");
        assertEquals(resultado1, leaderboard.getResultados().get(1));
    }

    @Test
    void testGetTop10ApenasDevolveNoMaximoDezRegistos() {
        for (int i = 1; i <= 15; i++) {
            Jogador jogador = new Jogador("Jogador" + i);
            ResultadoPartida resultado = new ResultadoPartida(jogador, i, 100);
            leaderboard.adicionarResultado(resultado, caminhoTemporario);
        }

        assertEquals(10, leaderboard.getTop10().size(), "O método getTop10() nunca deve devolver mais do que 10.");
    }

    @Test
    void testGetResultadosImutavel() {
        Jogador jogador = new Jogador("Pedro");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 10, 100);
        leaderboard.adicionarResultado(resultado, caminhoTemporario);

        assertThrows(UnsupportedOperationException.class, () ->
                leaderboard.getResultados().add(resultado)
        );
    }

    @Test
    void testCarregamentoNoArranqueReconstroiHistorico() {
        Jogador jogador = new Jogador("Bruna");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 15, 60);

        // Escreve no ficheiro temporário usando a primeira instância
        leaderboard.adicionarResultado(resultado, caminhoTemporario);

        // Cria uma nova instância apontando para o mesmo ficheiro em disco
        Leaderboard leaderboardRecarregado = new Leaderboard(caminhoTemporario);

        // Verifica se conseguiu fazer o parse correto dos dados
        assertEquals(1, leaderboardRecarregado.getResultados().size(), "O arranque deve ler o ficheiro.");
        assertEquals("Bruna", leaderboardRecarregado.getResultados().get(0).getJogador().getNome());
        assertEquals(15, leaderboardRecarregado.getResultados().get(0).getGrilosApanhados());
    }

    @Test
    void testConstrutorRejeitaCaminhoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Leaderboard(null));
        assertThrows(IllegalArgumentException.class, () -> new Leaderboard(""));
    }
}