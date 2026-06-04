package org.frogi.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {

    private List<ResultadoPartida> resultados;

    public Leaderboard() {
        resultados = new ArrayList<>();
    }

    public void adicionarResultado(ResultadoPartida resultado) {
        resultados.add(resultado);
        ordenarResultados();
    }

    public void ordenarResultados() {

        resultados.sort(
                Comparator.comparingInt(
                        ResultadoPartida::calcularPontuacao
                ).reversed()
        );
    }

    public List<ResultadoPartida> getTop10() {

        int limite = Math.min(10, resultados.size());

        return resultados.subList(0, limite);
    }

    public List<ResultadoPartida> getResultados() {
        return resultados;
    }
}