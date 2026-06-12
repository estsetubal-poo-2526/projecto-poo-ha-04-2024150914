package org.frogi.model;

import org.frogi.model.entidades.Grilo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapaTest {

    private Mapa mapa;
    private List<Integer> rios;
    private int[][] nenufaresIniciais;

    @BeforeEach
    void setUp() {
        rios = List.of(2, 4);
        // Coloca um nenúfar na linha 9 para testar o reset
        nenufaresIniciais = new int[][]{
                {2, 5},
                {4, 9} // Vai ter de saltar para a linha 0
        };
        mapa = new Mapa(rios, nenufaresIniciais);
    }

    @Test
    void testPosicaoValidaDentroEForaDosLimites() {
        // Cantos e posições válidas
        assertTrue(mapa.isPosicaoValida(0, 0));
        assertTrue(mapa.isPosicaoValida(14, 9)); // Limite máximo
        assertTrue(mapa.isPosicaoValida(7, 5));

        // Posições inválidas
        assertFalse(mapa.isPosicaoValida(-1, 5), "Coordenada X negativa deve ser inválida.");
        assertFalse(mapa.isPosicaoValida(5, -1), "Coordenada Y negativa deve ser inválida.");
        assertFalse(mapa.isPosicaoValida(15, 5), "X igual à largura está fora do limite (0 a 14).");
        assertFalse(mapa.isPosicaoValida(5, 10), "Y igual à altura está fora do limite (0 a 9).");
    }

    @Test
    void testAdicionarERemoverEntidadesDoMapa() {
        Grilo grilo = new Grilo(5, 5);

        mapa.adicionarEntidade(grilo);
        assertEquals(1, mapa.getEntidades().size());
        assertTrue(mapa.getEntidades().contains(grilo));

        mapa.removerEntidade(grilo);
        assertEquals(0, mapa.getEntidades().size());
        assertFalse(mapa.getEntidades().contains(grilo));
    }

    @Test
    void testMoverNenufaresParaBaixoAplicaEfeitoCiclico() {
        // Executa o movimento do rio
        mapa.moverNenufaresParaBaixo();
        int[][] coordenadasAtuais = mapa.getCoordenadasNenufares();

        // O nenúfar que estava em (2, 5) deve descer para (2, 6)
        assertEquals(6, coordenadasAtuais[0][1]);

        // O nenúfar que estava em (4, 9) deve dar a volta para (4, 0)
        assertEquals(0, coordenadasAtuais[1][1]);
    }

    @Test
    void testProcessarInteracoesConsomeGriloERemoveDoMapa() {
        Jogador jogador = new Jogador("Nuno");
        Nivel nivel = new Nivel(1, mapa);
        Partida partida = new Partida(jogador, nivel);
        partida.iniciarPartida(); // Coloca o sapo na posição (0, 1)

        // Criamos um grilo exatamente na mesma posição de spawn do Sapo (0, 1)
        Grilo griloCobaia = new Grilo(0, 1);
        mapa.adicionarEntidade(griloCobaia);

        // O grilo está no mapa e a pontuação é 0
        assertTrue(mapa.getEntidades().contains(griloCobaia));
        assertEquals(0, partida.getGrilosApanhados());

        mapa.processarInteracoes(partida);

        // O sapo come o grilo e o Iterator remove-o do mapa
        assertEquals(1, partida.getGrilosApanhados(), "O sapo deveria ter incrementado o contador de grilos.");
        assertFalse(mapa.getEntidades().contains(griloCobaia), "O grilo consumido deve ser removido da lista de entidades.");
    }

    @Test
    void testConstrutorDeveLancarExcecaoSeNenufarForCriadoForaDoRio() {
        // Rio configurado apenas nas colunas 2 e 4
        List<Integer> colunasRio = List.of(2, 4);

        // Nenúfar na coluna X=5 (que não é rio)
        int[][] nenufaresInvalidos = {
                {2, 3},
                {5, 1}
        };

        assertThrows(
                IllegalArgumentException.class,
                () -> new Mapa(colunasRio, nenufaresInvalidos),
                "O construtor deveria ter rejeitado a criação de um nenúfar fora das colunas de rio."
        );
    }


    @Test
    void testMetodosRejeitamValoresNulos() {
        assertThrows(IllegalArgumentException.class, () -> mapa.adicionarEntidade(null));
        assertThrows(IllegalArgumentException.class, () -> mapa.removerEntidade(null));
        assertThrows(IllegalArgumentException.class, () -> mapa.processarInteracoes(null));
        assertThrows(IllegalArgumentException.class, () -> new Mapa(null, nenufaresIniciais));
        assertThrows(IllegalArgumentException.class, () -> new Mapa(rios, null));
    }
}