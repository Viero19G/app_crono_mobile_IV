package com.example.learn.responses;
import com.example.learn.models.Maquina;

import java.util.List;
public class MaquinaResponse {
    private String message;
    private List<Maquina> data;

    public String getMessage() { return message; }
    public List<Maquina> getData() { return data; }
}
