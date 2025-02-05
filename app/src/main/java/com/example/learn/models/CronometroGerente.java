package com.example.learn.models;

public class CronometroGerente {

    private boolean rodando;
    private int segundos = 0;
    private int segundos_total;
    private int id_sp;
    private int id_tx;



    public CronometroGerente(){
    }
    public CronometroGerente(boolean rodando, int segundos, int segundos_total) {
        this.rodando = rodando;
        this.segundos = segundos;
        this.segundos_total = segundos_total;
    }

    public int getId_sp() {
        return id_sp;
    }

    public void setId_sp(int id_sp) {
        this.id_sp = id_sp;
    }

    public int getId_tx() {
        return id_tx;
    }

    public void setId_tx(int id_tx) {
        this.id_tx = id_tx;
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
