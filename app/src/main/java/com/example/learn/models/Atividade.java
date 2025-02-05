package com.example.learn.models;

import android.widget.EditText;

import java.util.List;

public class Atividade {
    private String nome;
    private String data_hora_inicio;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_hora_inicio() {
        return data_hora_inicio;
    }

    public void setData_hora_inicio(String data_hora_inicio) {
        this.data_hora_inicio = data_hora_inicio;
    }

    public String getData_hora_fim() {
        return data_hora_fim;
    }

    public void setData_hora_fim(String data_hora_fim) {
        this.data_hora_fim = data_hora_fim;
    }

    public int getPosto_trabalho() {
        return posto_trabalho;
    }

    public void setPosto_trabalho(int posto_trabalho) {
        this.posto_trabalho = posto_trabalho;
    }

    public int getMaquina() {
        return maquina;
    }

    public void setMaquina(int maquina) {
        this.maquina = maquina;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Integer> getOperacoes() {
        return operacoes;
    }

    public void setOperacoes(List<Integer> operacoes) {
        this.operacoes = operacoes;
    }

    public List<Integer> getTempos() {
        return tempos;
    }

    public void setTempos(List<Integer> tempos) {
        this.tempos = tempos;
    }

    public int getTempo_total() {
        return tempo_total;
    }

    public void setTempo_total(int tempo_total) {
        this.tempo_total = tempo_total;
    }

    private String data_hora_fim;
    private int posto_trabalho;
    private int maquina;
    private String observacao;
    private List<Integer> operacoes;
    private List<Integer> tempos;
    private int tempo_total;

    public Atividade(String nome, String data_hora_inicio, String data_hora_fim, int posto_trabalho, int maquina, String observacao, List<Integer> operacoes, List<Integer> tempos, int tempo_total) {
        this.nome = nome;
        this.data_hora_inicio = data_hora_inicio;
        this.data_hora_fim = data_hora_fim;
        this.posto_trabalho = posto_trabalho;
        this.maquina = maquina;
        this.observacao = observacao;
        this.operacoes = operacoes;
        this.tempos = tempos;
        this.tempo_total = tempo_total;
    }
}