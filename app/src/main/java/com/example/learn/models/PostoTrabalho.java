package com.example.learn.models;

public class PostoTrabalho {
    private int id;
    private String nome;
    private String descricao;

    public PostoTrabalho(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}