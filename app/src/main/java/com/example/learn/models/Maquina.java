package com.example.learn.models;

public class Maquina {
    private int id;
    private String nome;
    private String descricao;

    public Maquina(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    public Maquina(int id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }


}