package org.frogi.model;

import org.frogi.model.exceptions.NomeInvalidoException;

public class Jogador {

    private String nome;

    public Jogador(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new NomeInvalidoException("O nome do jogador não pode estar vazio.");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}