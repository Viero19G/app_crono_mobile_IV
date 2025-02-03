package com.example.learn.models;

public class Operacao {
    private int id;
    private String nome;
    private String descricao;
    private String classificacao;

    public Operacao(String nome, String descricao, String classificacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.classificacao = classificacao;
    }


    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getClassificacao() { return classificacao; }
}
