package org.frogi.model;

import org.frogi.model.entidades.*;
import org.frogi.model.entidades.powerups.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Mapa {

    private final int largura = 15;
    private final int altura = 10;
    private final List<EntidadeJogo> entidades;
    private final List<Integer> colunasRio;
    private int[][] coordenadasNenufares;

    public Mapa(List<Integer> colunasRio, int[][] coordenadasNenufares) {
        if (colunasRio == null) {
            throw new IllegalArgumentException("A lista de colunas do rio não pode ser nula.");
        }
        if (coordenadasNenufares == null) {
            throw new IllegalArgumentException("As coordenadas dos nenúfares não podem ser nulas.");
        }
        this.entidades = new ArrayList<>();
        this.colunasRio = colunasRio;
        this.coordenadasNenufares = coordenadasNenufares;
    }

    public void adicionarEntidade(EntidadeJogo entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("Não é possível adicionar uma entidade nula ao mapa.");
        }
        entidades.add(entidade);
    }

    public void removerEntidade(EntidadeJogo entidade) {
        if (entidade == null) {
            throw new IllegalArgumentException("Não é possível remover uma entidade nula do mapa.");
        }
        entidades.remove(entidade);
    }

    public void processarInteracoes(Partida partida) {
        if (partida == null) {
            throw new IllegalArgumentException("A partida não pode ser nula para processar as interações.");
        }

        Iterator<EntidadeJogo> it = entidades.iterator();

        while (it.hasNext()) {
            EntidadeJogo entidade = it.next();

            // Verifica se o Sapo pisou a entidade
            if (entidade.getPosicaoX() == partida.getXSapo() &&
                    entidade.getPosicaoY() == partida.getYSapo()) {

                // Executa o efeito da colisão
                entidade.interagir(partida);

                // Se for um Grilo ou PowerUp, remove
                if (entidade instanceof Grilo || entidade instanceof PowerUp) {
                    it.remove();
                }
            }
        }

    }

    public boolean isPosicaoValida(int x, int y) {
        return x >= 0 && x < largura && y >= 0 && y < altura;
    }

    public List<EntidadeJogo> getEntidades() {
        return List.copyOf(entidades);
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }
    public List<Integer> getColunasRio() { return colunasRio; }

    public int[][] getCoordenadasNenufares() {
        int[][] copia = new int[this.coordenadasNenufares.length][2];
        for (int i = 0; i < this.coordenadasNenufares.length; i++) {
            copia[i][0] = this.coordenadasNenufares[i][0];
            copia[i][1] = this.coordenadasNenufares[i][1];
        }
        return copia;
    }

    public void moverNenufaresParaBaixo() {
        for (int i = 0; i < coordenadasNenufares.length; i++) {
            coordenadasNenufares[i][1] = (coordenadasNenufares[i][1] + 1) % altura;
        }
    }

}