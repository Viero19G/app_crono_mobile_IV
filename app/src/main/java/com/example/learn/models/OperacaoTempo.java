package com.example.learn.models;

public class OperacaoTempo {
    private int operacaoId;
    private int tempoSegundos;

    public OperacaoTempo(int operacaoId, int tempoSegundos) {
        this.operacaoId = operacaoId;
        this.tempoSegundos = tempoSegundos;
    }

    public int getOperacaoId() { return operacaoId; }
    public int getTempoSegundos() { return tempoSegundos; }
}
