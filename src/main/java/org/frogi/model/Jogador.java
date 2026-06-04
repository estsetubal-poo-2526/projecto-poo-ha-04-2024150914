package org.frogi.model;

public class Jogador {

    private String nome;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do jogador não pode estar vazio.");
        }

        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}