package com.example.learn.models;

public class Operacao {
    private int id;
    private String nome;
    private String descricao;
    private int classificacao;

    public Operacao(){
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(int classificacao) {
        this.classificacao = classificacao;
    }

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

    public Operacao(int id, String nome) {
    }

    public int getId_Class() { return classificacao; }
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return nome; }
}
