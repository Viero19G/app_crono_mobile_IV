package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;
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
        // Exibir Toast vermelho por 30 segundos
        mostrarToastVermelho();


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
                        sm.syncDB(() -> {
                            Toast.makeText(MainActivity.this, "Sincronização concluída!", Toast.LENGTH_SHORT).show();
                        }, MainActivity.this);
                        add_maquina.setEnabled(true);
                        add_op.setEnabled(true);
                        add_posto.setEnabled(true);
                        add_btn.setEnabled(true);
                        atv_cass.setEnabled(true);
                    } catch (Exception e) {
                        Log.e("MainActivity", "Erro ao sincronizar", e);
                    }
                }
            });
        }
        add_maquina.setEnabled(false);
        add_op.setEnabled(false);
        add_posto.setEnabled(false);
        add_btn.setEnabled(false);
        atv_cass.setEnabled(false);
    }
    private void mostrarToastVermelho() {
        Handler handler = new Handler(Looper.getMainLooper());
        int duracaoTotal = 10000; // 30 segundos
        int intervaloToast = 5000; // Tempo de Toast.LENGTH_LONG
        int vezesParaRepetir = duracaoTotal / intervaloToast; // Quantidade de repetições

        Runnable toastRunnable = new Runnable() {
            int contador = 0;

            @Override
            public void run() {
                if (contador < vezesParaRepetir) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "⚠️ Lembre-se de Sincronizar a BASE DE DADOS!",
                            Toast.LENGTH_LONG);

                    // Personalizar o Toast para ser vermelho
                    View view = toast.getView();
                    if (view != null) {
                        view.setBackgroundColor(Color.RED);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(Color.WHITE);
                        text.setGravity(Gravity.CENTER);
                    }

                    toast.show();
                    contador++;
                    handler.postDelayed(this, intervaloToast); // Agendar próxima exibição
                }
            }
        };

        // Iniciar a repetição do Toast
        handler.post(toastRunnable);
    }

}
