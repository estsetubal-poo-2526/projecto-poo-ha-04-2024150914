package org.frogi.model;

import org.frogi.model.entidades.*;
import org.frogi.model.powerups.PowerUp;

import java.util.ArrayList;
import java.util.List;

public class Mapa {

    private final int largura = 15;
    private final int altura = 10;
    private final List<EntidadeJogo> entidades;
    private final List<Integer> colunasRio;
    private final int[][] coordenadasNenufares;

    public Mapa(List<Integer> colunasRio, int[][] coordenadasNenufares) {
        this.entidades = new ArrayList<>();
        this.colunasRio = colunasRio;
        this.coordenadasNenufares = coordenadasNenufares;
    }

    public void adicionarEntidade(EntidadeJogo entidade) {
        entidades.add(entidade);
    }

    public void removerEntidade(EntidadeJogo entidade) {
        entidades.remove(entidade);
    }

    /**
     * Processa interações do Sapo com todas as entidades no mapa
     */
    public void processarInteracoes(Partida partida) {
        List<EntidadeJogo> paraRemover = new ArrayList<>();

        for (EntidadeJogo entidade : new ArrayList<>(entidades)) {
            if (entidade.getPosicaoX() == partida.getXSapo() &&
                entidade.getPosicaoY() == partida.getYSapo()) {
                
                entidade.interagir(partida);
                
                // Grilos e PowerUps são consumidos/removidos
                if (entidade instanceof Grilo || entidade instanceof PowerUp) {
                    paraRemover.add(entidade);
                }
            }
        }

        for (EntidadeJogo e : paraRemover) {
            removerEntidade(e);
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
    public int[][] getCoordenadasNenufares() { return coordenadasNenufares; }
}