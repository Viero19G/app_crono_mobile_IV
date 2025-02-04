package com.example.learn.models;

public class Cronometro {

    private boolean rodando;
    private int segundos;
    private int segundos_total;

    public Cronometro(){
    }
    public Cronometro(boolean rodando, int segundos, int segundos_total) {
        this.rodando = rodando;
        this.segundos = segundos;
        this.segundos_total = segundos_total;
    }

    public boolean isRodando() {
        return rodando;
    }

    public void setRodando(boolean rodando) {
        this.rodando = rodando;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getSegundos_total() {
        return segundos_total;
    }

    public void setSegundos_total(int segundos_total) {
        this.segundos_total = segundos_total;
    }
}
