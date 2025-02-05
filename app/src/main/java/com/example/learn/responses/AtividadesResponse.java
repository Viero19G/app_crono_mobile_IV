package com.example.learn.responses;

import com.example.learn.models.Atividade;


import java.util.List;

public class AtividadesResponse {
        private String message;
        private List<Atividade> data;

        public String getMessage() { return message; }
        public List<Atividade> getData() { return data; }
    }

