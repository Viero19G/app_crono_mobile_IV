package com.example.learn.jsonController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Structs {
    JsonObject jsObj;

    public Structs() {
    }

    public JsonObject nomeDesc(String nome, String desc){
        JsonObject jsObj = new JsonObject();
        jsObj.addProperty("nome", nome);
        jsObj.addProperty("descricao", desc);

        return jsObj;
    }

    public static JsonObject nomeDescClass(String nome, String desc, String classificacao){
        JsonObject jsObj = new JsonObject();
        jsObj.addProperty("nome", nome);
        jsObj.addProperty("descricao", desc);
        jsObj.addProperty("classificacao", classificacao);

        return jsObj;
    }

    public static JsonObject montarJsonAtividade(
            String nome, String dataHoraInicio, String dataHoraFim,
            int postoTrabalho, int maquina, String observacao,
            int[] operacoes, int[] tempos
    ) {
        JsonObject jsObj = new JsonObject();
        jsObj.addProperty("nome", nome);
        jsObj.addProperty("data_hora_inicio", dataHoraInicio);
        jsObj.addProperty("data_hora_fim", dataHoraFim);
        jsObj.addProperty("posto_trabalho", postoTrabalho);
        jsObj.addProperty("maquina", maquina);
        jsObj.addProperty("observacao", observacao);
        // Criando os arrays JSON
        JsonArray jsonOperacoes = new JsonArray();
        for (int op : operacoes) {
            jsonOperacoes.add(op);
        }
        jsObj.add("operacoes", jsonOperacoes);
        JsonArray jsonTempos = new JsonArray();
        for (int tempo : tempos) {
            jsonTempos.add(tempo);
        }
        jsObj.add("tempos", jsonTempos);

        return jsObj;
    }
}
