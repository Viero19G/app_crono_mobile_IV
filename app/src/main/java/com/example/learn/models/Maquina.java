package com.example.learn.models;

public class Maquina {
    private String nome;
    private String descricao;

    public Maquina(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}