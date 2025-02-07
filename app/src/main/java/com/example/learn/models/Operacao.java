package com.example.learn.models;

public class Operacao {
    private int id;
    private String nome;
    private String descricao;
    private int classificacao;

    public Operacao(String nome, String descricao, int id_class) {
        this.nome = nome;
        this.descricao = descricao;
        this.classificacao = id_class;
    }

    public Operacao(int id, String nome, String descricao, int id_class) {
        this.id =id;
        this.nome = nome;
        this.descricao = descricao;
        this.classificacao = id_class;
    }

    public int getId_Class() { return classificacao; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return nome; // Isso define o que será exibido no Spinner
    }
}
