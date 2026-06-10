package org.frogi.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {

    private static final String FICHEIRO_LEADERBOARD = "leaderboard.txt";
    private List<ResultadoPartida> resultados;

    public Leaderboard() {
        this.resultados = new ArrayList<>();
        carregarDoFicheiro();
    }

    public void adicionarResultado(ResultadoPartida resultado) {
        resultados.add(resultado);
        ordenarResultados();
        guardarNoFicheiro(resultado);
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
        return List.copyOf(resultados);
    }

    //Grava apenas o novo resultado no fim do ficheiro
    private void guardarNoFicheiro(ResultadoPartida r) {
        try (FileWriter fw = new FileWriter(FICHEIRO_LEADERBOARD, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Guardar: Nome;GrilosComidos;TempoEmSegundos;Venceu;Nivel
            out.println(r.getJogador().getNome() + ";" +
                    r.getGrilosApanhados() + ";" +
                    r.getTempoDecorrido() + ";" +
                    r.isVenceu() + ";" +
                    r.getNivelAlcancado());

        } catch (IOException e) {
            System.out.println("Erro ao gravar recorde: " + e.getMessage());
        }
    }

    //Lê o ficheiro linha a linha e reconstrói os objetos ResultadoPartida
    private void carregarDoFicheiro() {
        File f = new File(FICHEIRO_LEADERBOARD);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 5) {
                    Jogador jog = new Jogador(dados[0]);
                    int grilos = Integer.parseInt(dados[1]);
                    int tempo = Integer.parseInt(dados[2]);
                    boolean venceu = Boolean.parseBoolean(dados[3]);
                    int nivel = Integer.parseInt(dados[4]);

                    // Criar o objeto ResultadoPartida correspondente a essa linha
                    ResultadoPartida res = new ResultadoPartida(jog, grilos, tempo, venceu, nivel);

                    // Adicionar diretamente à lista interna sem gravar de novo
                    resultados.add(res);
                }
            }
            // Garante que tudo fica ordenado por pontuação após carregar o ficheiro
            ordenarResultados();

        } catch (Exception e) {
            System.out.println("Erro ao carregar o histórico do leaderboard: " + e.getMessage());
        }
    }
}