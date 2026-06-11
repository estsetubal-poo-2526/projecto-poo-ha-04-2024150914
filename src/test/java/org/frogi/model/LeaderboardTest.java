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

    // A anotação @TempDir cria uma pasta virtual isolada e apaga-a no fim do teste
    @BeforeEach
    void setUp(@TempDir Path pastaTemporaria) {
        // Define um ficheiro de texto exclusivo para este teste dentro da pasta temporária
        Path ficheiroTeste = pastaTemporaria.resolve("leaderboard_teste.txt");
        caminhoTemporario = ficheiroTeste.toAbsolutePath().toString();

        // Instancia o leaderboard apontando para o ambiente isolado
        leaderboard = new Leaderboard(caminhoTemporario);
    }

    @Test
    void testAdicionarResultado() {
        Jogador jogador = new Jogador("Pedro");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 10, 100);

        leaderboard.adicionarResultado(resultado, caminhoTemporario);

        // Verifica a memória interna
        assertEquals(1, leaderboard.getResultados().size());
        assertTrue(leaderboard.getResultados().contains(resultado));

        // BÓNUS: Testa se gravou mesmo fisicamente no ficheiro temporário
        File f = new File(caminhoTemporario);
        assertTrue(f.exists(), "O ficheiro de pontuações deve ser fisicamente criado.");
        assertTrue(f.length() > 0, "O ficheiro não deve estar vazio após gravar.");
    }

    @Test
    void testOrdenarResultados() {
        Jogador jogador1 = new Jogador("Pedro");
        Jogador jogador2 = new Jogador("Ana");

        // Configura pontuações diferentes através dos grilos e tempos
        ResultadoPartida resultado1 = new ResultadoPartida(jogador1, 5, 100);
        ResultadoPartida resultado2 = new ResultadoPartida(jogador2, 20, 50);

        leaderboard.adicionarResultado(resultado1, caminhoTemporario);
        leaderboard.adicionarResultado(resultado2, caminhoTemporario);

        // Garante que a ordenação coloca o melhor resultado (resultado2) no topo (índice 0)
        assertEquals(resultado2, leaderboard.getResultados().get(0), "O melhor resultado deve ficar em primeiro.");
        assertEquals(resultado1, leaderboard.getResultados().get(1));
    }

    @Test
    void testGetTop10() {
        // Insere 15 registos para garantir que o corte acontece nos 10
        for (int i = 1; i <= 15; i++) {
            Jogador jogador = new Jogador("Jogador" + i);
            ResultadoPartida resultado = new ResultadoPartida(jogador, i, 100);
            leaderboard.adicionarResultado(resultado);
        }

        assertEquals(10, leaderboard.getTop10().size(), "O método getTop10() nunca deve devolver mais do que 10 registos.");
    }

    @Test
    void testGetResultadosImutavel() {
        Jogador jogador = new Jogador("Pedro");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 10, 100);
        leaderboard.adicionarResultado(resultado);

        // Garante proteção de encapsulamento: ninguém fora da classe pode adulterar a lista diretamente
        assertThrows(
                UnsupportedOperationException.class,
                () -> leaderboard.getResultados().add(resultado),
                "A lista exposta por getResultados() tem de ser estritamente imutável."
        );
    }

    @Test
    void testCarregamentoNoArranque() {
        // 1. Cria um cenário onde um resultado já foi gravado anteriormente
        Jogador jogador = new Jogador("Bruna");
        ResultadoPartida resultado = new ResultadoPartida(jogador, 15, 60);
        leaderboard.adicionarResultado(resultado);

        // 2. Instancia um NOVO Leaderboard a apontar para o mesmo ficheiro temporário
        Leaderboard leaderboardRecarregado = new Leaderboard(caminhoTemporario);

        // 3. Valida se o construtor conseguiu ir buscar com sucesso o histórico guardado em disco
        assertEquals(1, leaderboardRecarregado.getResultados().size(), "O arranque deve reconstruir o histórico a partir do ficheiro.");
        assertEquals("Bruna", leaderboardRecarregado.getResultados().get(0).getJogador().getNome());
    }
}