package com.example.learn.models;

public class PostoTrabalho {
    private int id;
    private String nome;
    private String descricao;

    public PostoTrabalho(int id, String nome) {
        this.nome = nome;
        this.id = id;
    }

    public PostoTrabalho(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }


    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    @Override
    public String toString() {
        return nome; }}