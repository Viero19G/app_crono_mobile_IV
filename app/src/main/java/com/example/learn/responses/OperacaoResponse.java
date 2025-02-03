package com.example.learn.responses;

import com.example.learn.models.Operacao;

import java.util.List;

public class OperacaoResponse {
    private String message;
    private List<Operacao> data;

    public String getMessage() { return message; }
    public List<Operacao> getData() { return data; }
}
