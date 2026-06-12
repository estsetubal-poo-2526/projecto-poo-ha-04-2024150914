package org.frogi.model;

import org.frogi.model.exceptions.NomeInvalidoException;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {

    private static final String FICHEIRO_LEADERBOARD = "leaderboard.txt";
    private List<ResultadoPartida> resultados;

    public Leaderboard() {
        this.resultados = new ArrayList<>();
        carregarDoFicheiro(FICHEIRO_LEADERBOARD);
    }

    public Leaderboard(String caminhoFicheiro) {
        if (caminhoFicheiro == null || caminhoFicheiro.isBlank()) {
            throw new IllegalArgumentException("O caminho do ficheiro do leaderboard não pode estar vazio.");
        }
        this.resultados = new ArrayList<>();
        carregarDoFicheiro(caminhoFicheiro);
    }

    public void adicionarResultado(ResultadoPartida resultado, String ficheiro) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado da partida não pode ser nulo.");
        }
        if (ficheiro == null || ficheiro.isBlank()) {
            throw new IllegalArgumentException("O nome do ficheiro de destino não pode estar vazio.");
        }
        resultados.add(resultado);
        ordenarResultados();
        guardarNoFicheiro(resultado, ficheiro);
    }

    public void adicionarResultado(ResultadoPartida resultado) {
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado da partida não pode ser nulo.");
        }
        resultados.add(resultado);
        ordenarResultados();
        guardarNoFicheiro(resultado, FICHEIRO_LEADERBOARD);
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
    private void guardarNoFicheiro(ResultadoPartida r, String ficheiro) {
        try (FileWriter fw = new FileWriter(ficheiro, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            // Guardar: Nome;GrilosComidos;TempoEmSegundos;
            out.println(r.getJogador().getNome() + ";" +
                    r.getGrilosApanhados() + ";" +
                    r.getTempoDecorrido() + ";");

        } catch (IOException e) {
            System.err.println("Erro ao gravar recorde: " + e.getMessage());
        }
    }

    //Lê o ficheiro linha a linha e reconstrói os objetos ResultadoPartida
    private void carregarDoFicheiro(String ficheiro) {
        File f = new File(ficheiro);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 3) {
                    try{
                        Jogador jog = new Jogador(dados[0]);
                        int grilos = Integer.parseInt(dados[1]);
                        int tempo = Integer.parseInt(dados[2]);

                        // Cria o objeto ResultadoPartida correspondente a essa linha
                        ResultadoPartida res = new ResultadoPartida(jog, grilos, tempo);

                        // Adiciona diretamente à lista interna sem gravar de novo
                        resultados.add(res);
                    }catch(NumberFormatException | NomeInvalidoException e){
                        System.err.println("Linha corrompida no ficheiro '" + ficheiro + "'. Ignorada.");
                    }
                }
            }
            // Garante que tudo fica ordenado por pontuação após carregar o ficheiro
            ordenarResultados();

        } catch (FileNotFoundException e) {
            System.err.println("Ficheiro de leaderboard não encontrado: " + e.getMessage());
        } catch (IOException e){
            System.err.println("Falha ao ler o histórico do leaderboard: " + e.getMessage());
        }
    }
}