package com.example.learn.models;

import java.util.List;

public class MaquinaB {
    private int id;
    private String nome;
    private String descricao;
    private List<Operacao> operacoes;
    private List<Integer> ops;// IDs das operações associadas

    public List<Operacao> getOperacoes() {
        return operacoes;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setOperacoes(List<Integer> operacoes) {
        this.ops = operacoes;
    }
    public MaquinaB() {
    }
    public MaquinaB(String nome, String descricao, List<Operacao> operacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.operacoes = operacoes;
    }
    public MaquinaB(int id, String nome, String descricao, List<Operacao> operacoes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.operacoes = operacoes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    @Override
    public String toString() {
        return nome; // Isso define o que será exibido no Spinner
    }


}