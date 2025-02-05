package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.example.learn.API.ApiService;

import com.example.learn.API.PostoRepository;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.PostoTrabalho;

public class createPosto extends AppCompatActivity {
    EditText nome_posto, desc_posto;
    Button send_posto;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_posto);
        nome_posto = findViewById(R.id.nome_posto);
        desc_posto = findViewById(R.id.deesc_posto);
        send_posto = findViewById(R.id.send_posto);
        send_posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp= new DatabaseHelper(createPosto.this);
               // ApiService api = new ApiService();
                dbHelp.addMaquina(nome_posto.getText().toString().trim(),
                        desc_posto.getText().toString().trim());
                PostoRepository repo = new PostoRepository(context);
                PostoTrabalho novaPosto = new PostoTrabalho(nome_posto.getText().toString(), desc_posto.getText().toString());
                repo.criarPostoComReenvio(novaPosto, 1);
            }
        });
    }
}