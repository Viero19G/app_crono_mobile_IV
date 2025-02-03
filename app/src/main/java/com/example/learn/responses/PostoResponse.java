package com.example.learn.responses;

import com.example.learn.models.PostoTrabalho;

import java.util.List;

public class PostoResponse{
    private String message;
    private List<PostoTrabalho> data;

    public String getMessage() { return message;}
    public List<PostoTrabalho> getData() { return data;}
}