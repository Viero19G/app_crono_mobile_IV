package com.example.learn.models;

import java.util.List;

public class Maquina {
    private int id;
    private String nome;
    private String descricao;
    private List<Operacao> ops;
    private List<Integer> operacoes;// IDs das operações associadas

    public Maquina(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }


    public List<Operacao> getOperacoes() {
        return ops;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setOperacoes(List<Integer> operacoes) {
        this.operacoes = operacoes;
    }
    public Maquina() {
    }
    public Maquina(String nome, String descricao, List<Operacao> operacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.ops = operacoes;
    }
    public Maquina(int id, String nome, String descricao, List<Operacao> operacoes) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ops = operacoes;
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