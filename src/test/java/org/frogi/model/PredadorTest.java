package org.frogi.model;

import org.frogi.model.entidades.Predador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PredadorTest {

    private Predador predador;
    private Partida partida;
    private Mapa mapa;
    private Jogador jogador;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("Pedro");

        // Rio na coluna 2 com nenúfar em (2,3)
        List<Integer> rios = List.of(2);
        int[][] nenufares = {{2, 3}};
        mapa = new Mapa(rios, nenufares);

        Nivel nivel1 = new Nivel(1, mapa);
        partida = new Partida(jogador, nivel1);
        // Sapo em 0,1
        partida.iniciarPartida();

        // Predador na relva
        predador = new Predador(0, 3);
    }

    @Test
    void testInteragirRetiraVidaAoJogador() {
        int vidasAntes = partida.getVidasRestantes();
        predador.interagir(partida);
        assertEquals(vidasAntes - 1, partida.getVidasRestantes(), "O predador deve tirar uma vida ao interagir.");
    }

    @Test
    void testPosicaoInicialEGetters() {
        assertEquals(0, predador.getPosicaoX());
        assertEquals(3, predador.getPosicaoY());
    }

    @Test
    void testMoverAutomaticoNivel2PersegueSapoNoEixoMaior() {
        Nivel nivel2 = new Nivel(2, mapa);
        partida.setNivel(nivel2);

        // Sapo está em (0,1) e o Predador está em (0,3)
        // Distância X = 0, Distância Y = 2. O predador foca-se no eixo Y.
        // Como o Sapo está acima (1 < 3), o predador deve subir para (0,2).
        predador.moverAutomatico(nivel2, partida);

        assertEquals(0, predador.getPosicaoX());
        assertEquals(2, predador.getPosicaoY(), "O predador no nível 2 deve mover-se no eixo Y para comer o sapo.");
    }

    @Test
    void testPredadorRecusaCairNoRioSemNenufar() {
        Nivel nivel2 = new Nivel(2, mapa);
        partida.setNivel(nivel2);

        // Pomos o predador em (1, 5) e o sapo em (3, 5)
        // A perseguição direta faria o predador andar para a direita: X=2 (coluna de rio)
        // Como em (2,5) não há nenúfar, o predador deve recusar o movimento e manter-se seguro
        predador.setPosicao(1, 5);
        partida.getSapo().setPosicao(3, 5);

        predador.moverAutomatico(nivel2, partida);

        // O movimento falha ou desvia, mas nao pode ir para (2,5)
        assertNotEquals(2, predador.getPosicaoX(), "O predador não pode mover-se para uma zona de rio vazia.");
    }

    @Test
    void testMoverAutomaticoNivel3InterrompeSeSapoMorrer() {
        Nivel nivel3 = new Nivel(3, mapa);
        partida.setNivel(nivel3);

        // Colocamos o predador (0,2) a apenas 1 passo de distância do sapo
        // O sapo está em (0, 1)
        // No 1º passo o predador vai aterrar em (0,1), colidir com o sapo e matá-lo.
        predador.setPosicao(0, 2);
        mapa.adicionarEntidade(predador);

        int vidasAntes = partida.getVidasRestantes();

        predador.moverAutomatico(nivel3, partida);

        assertEquals(vidasAntes - 1, partida.getVidasRestantes(), "O sapo deveria ter perdido uma vida no 1º passo.");
        // O sapo morreu, por isso o 2º passo nao deve ocorrer
        assertTrue(partida.getXSapo() == 0 && partida.getYSapo() == 1 || !partida.getSapo().isVivo(),
                "A execução deve parar assim que o sapo morre para evitar loops infinitos ou desvios inválidos.");
    }

    @Test
    void testMetodoMoverAutomaticoValidaParametrosNulos() {
        Nivel nivel = partida.getNivelAtual();

        assertThrows(IllegalArgumentException.class, () -> {
            predador.moverAutomatico(null, partida);
        }, "Deveria lançar IllegalArgumentException se o nível for nulo.");

        assertThrows(IllegalArgumentException.class, () -> {
            predador.moverAutomatico(nivel, null);
        }, "Deveria lançar IllegalArgumentException se a partida for nula.");
    }
}