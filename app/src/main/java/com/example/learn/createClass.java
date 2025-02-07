package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.learn.API.ClassificacaoRepository;
import com.example.learn.API.SyncManager;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Classificacao;


public class createClass extends AppCompatActivity {
    EditText nome_class;
    Button send_class;
    Context context;
    SyncManager sm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        sm = new SyncManager(createClass.this);
        sm.syncDB(new Runnable() {
            @Override
            public void run() {
            }
        });

        nome_class = findViewById(R.id.nome_class);
        send_class = findViewById(R.id.send_class);
        send_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp= new DatabaseHelper(createClass.this);
                // ApiService api = new ApiService();
                dbHelp.addClassificacao(nome_class.getText().toString().trim());
                ClassificacaoRepository repo = new ClassificacaoRepository(context);
                Classificacao novaClas = new Classificacao(nome_class.getText().toString());
                repo.criarClassificacaoComReenvio(novaClas, 1);
            }
        });
    }
}