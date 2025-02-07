package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.learn.API.SyncManager;
import com.example.learn.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelp;
    RecyclerView recyclerView;
    FloatingActionButton add_btn;
    Button syncButn,add_posto,add_op, add_maquina, atv_cass;

    SyncManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        // Inicializar DatabaseHelper
        dbHelp = new DatabaseHelper(this);
        // Inicializar SyncManager
        sm = new SyncManager(this);

        // Botão de adicionar
        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, cronoActivity.class);
                startActivity(intent);
            }
        });
        add_op = findViewById(R.id.add_op);
        add_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, createOp.class);
                startActivity(intent);
            }
        });
        atv_cass = findViewById(R.id.atv_cass);
        atv_cass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, createClass.class);
                startActivity(intent);
            }
        });
        add_posto = findViewById(R.id.add_posto);
        add_posto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, createPosto.class);
                startActivity(intent);
            }
        });
        add_maquina = findViewById(R.id.add_maquina);
        add_maquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, createMaq.class);
                startActivity(intent);
            }
        });

        // Botão de sincronização
        syncButn = findViewById(R.id.sync); // Certifique-se de que o ID no XML está correto

        if (syncButn == null) {
            Log.e("MainActivity", "Erro: Botão sync não encontrado no layout!");
        } else {
            syncButn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Log.d("MainActivity", "Iniciando sincronização...");
                        dbHelp.clearTables();
                        sm.syncDB(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });;
                        Log.d("MainActivity", "Sincronização concluída com sucesso.");
                    } catch (Exception e) {
                        Log.e("MainActivity", "Erro ao sincronizar", e);
                    }
                }
            });
        }
    }
}
