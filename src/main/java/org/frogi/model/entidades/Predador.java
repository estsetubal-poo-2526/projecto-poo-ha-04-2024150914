package org.frogi.model.entidades;

import org.frogi.model.Partida;
import org.frogi.model.Nivel;
import org.frogi.model.Mapa;

import java.util.Random;

public class Predador extends EntidadeJogo {

    public Predador(int posicaoX, int posicaoY) {
        super(posicaoX, posicaoY);
    }

    @Override
    public void interagir(Partida partida) {
        partida.perderVida();
    }


    public void moverAutomatico(Nivel nivelAtual, Partida partida) {
        int dx = 0;
        int dy = 0;

        if (nivelAtual.getNumero() == 1) {
            // NÍVEL 1: Movimento Aleatório
            Random rand = new Random();
            boolean moverNoEixoX = rand.nextBoolean();
            int sentido = rand.nextBoolean() ? 1 : -1;

            if (moverNoEixoX) dx = sentido;
            else dy = sentido;

            int novoX = getPosicaoX() + dx;
            int novoY = getPosicaoY() + dy;

            if (isMovimentoValido(novoX, novoY, nivelAtual)) {
                setPosicao(novoX, novoY);
            }

        } else if (nivelAtual.getNumero() == 2) {
            // NÍVEL 2: Movimento em direçao ao sapo
            calcularDirecaoPerseguicao(partida, nivelAtual);

        } else if (nivelAtual.getNumero() >= 3) {
            // NÍVEL 3: Dois passos em diraçao ao sapo com verificação imediata entre eles

            // Dá o primeiro passo
            calcularDirecaoPerseguicao(partida, nivelAtual);
            partida.processarInteracoes(); // SE O APANHOU AQUI, O SAPO MORRE JÁ e faz respawn

            // Se o sapo morreu ou a partida reiniciou o nível, o predador não deve continuar
            if (!partida.getSapo().isVivo() || (partida.getXSapo() == 1 && partida.getYSapo() == 1)) {
                return;
            }

            // Dá o segundo passo a partir da nova posição
            calcularDirecaoPerseguicao(partida, nivelAtual);
            partida.processarInteracoes(); // Verifica se o apanhou no segundo quadrado
        }
    }

    private void calcularDirecaoPerseguicao(Partida partida, Nivel nivelAtual) {
        int sapoX = partida.getXSapo();
        int sapoY = partida.getYSapo();

        int predadorX = getPosicaoX();
        int predadorY = getPosicaoY();

        // Calcula a distância absoluta em cada eixo
        int distXX = Math.abs(sapoX - predadorX);
        int distY = Math.abs(sapoY - predadorY);

        int dx = 0;
        int dy = 0;

        // O predador foca-se no eixo onde a distância é maior para tentar alinhar-se com o sapo
        if (distXX >= distY && predadorX != sapoX) {
            // Se o sapo está à direita, avança. Se está à esquerda, recua
            dx = (sapoX > predadorX) ? 1 : -1;
        } else if (predadorY != sapoY) {
            // Se o sapo está abaixo, avança. Se está acima, recua
            dy = (sapoY > predadorY) ? 1 : -1;
        }

        int novoX = predadorX + dx;
        int novoY = predadorY + dy;

        // Se o caminho para perseguir for válido (não for água livre ou fora do mapa), ele avança
        if (isMovimentoValido(novoX, novoY, nivelAtual)) {
            setPosicao(novoX, novoY);
        } else {
            // Se o caminho direto estiver bloqueado por água, tenta o outro eixo de forma aleatória
            Random rand = new Random();
            dx = 0; dy = 0;
            if (rand.nextBoolean()) dx = (sapoX > predadorX) ? 1 : -1;
            else dy = (sapoY > predadorY) ? 1 : -1;

            if (isMovimentoValido(predadorX + dx, predadorY + dy, nivelAtual)) {
                setPosicao(predadorX + dx, predadorY + dy);
            }
        }
    }


    private boolean isMovimentoValido(int x, int y, Nivel nivelAtual) {
        // Validar limites do mapa
        if (!nivelAtual.isPosicaoValida(x, y)) {
            return false;
        }

        Mapa mapa = nivelAtual.getMapa();

        // Validar regras do Rio e dos Nenúfares
        if (mapa.getColunasRio().contains(x)) {
            boolean estaEmCimaDeNenufar = false;

            for (int[] par : mapa.getCoordenadasNenufares()) {
                if (par[0] == x && par[1] == y) {
                    estaEmCimaDeNenufar = true;
                    break;
                }
            }

            // Se for coluna de rio e não houver nenúfar, o movimento é inválido para o predador
            if (!estaEmCimaDeNenufar) {
                return false;
            }
        }

        return true; // Se passou em tudo, o movimento é válido
    }

}