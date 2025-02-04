package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;

import com.example.learn.adapter.OperacaoAdapter;
import com.example.learn.models.Cronometro;
import com.example.learn.models.OperacaoTempo;

import java.util.ArrayList;
import java.util.List;

public class cronoActivity extends AppCompatActivity {

    private Button btnIniciar, btnProximo, btnParar, btnEnviar;
    private Spinner spPost, spMaq;
    private RecyclerView recyclerOperacoes;
    private List<OperacaoTempo> listaOperacoes;
    private OperacaoAdapter adapter;
    private long inicioTempo;
    private boolean isCronometroRodando = false;
    private Chronometer cronus;
    private int PauseOffSet = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);
        Cronometro cr;
        cr = new Cronometro();

        // Vincular componentes
        btnIniciar = findViewById(R.id.btn_InitC);
        cronus = findViewById(R.id.chronometerI)
        btnProximo = findViewById(R.id.btnProximo);
        btnParar = findViewById(R.id.btnParar);
        btnEnviar = findViewById(R.id.btnEnviar);
        spPost = findViewById(R.id.spPost);
        spMaq = findViewById(R.id.spMaq);
        recyclerOperacoes = findViewById(R.id.recyclerOperacoes);

        // Configurar RecyclerView
        listaOperacoes = new ArrayList<>();
        adapter = new OperacaoAdapter(this, listaOperacoes);
        recyclerOperacoes.setLayoutManager(new LinearLayoutManager(this));
        recyclerOperacoes.setAdapter(adapter);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.setRodando(true);
                cronus.setBase(SystemClock.elapsedRealtime()- PauseOffSet);
                cronus.start();

            }
        });
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.setRodando(false);
                cronus.stop();
                PauseOffSet = (int) (SystemClock.elapsedRealtime()- cronus.getBase() / 1000);
                cr.setSegundos_total(PauseOffSet);
            }
        });
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseOffSet = (int) (SystemClock.elapsedRealtime()- cronus.getBase() / 1000);
                int seg_old = cr.getSegundos();
                cr.setSegundos(PauseOffSet);
                int total = seg_old + PauseOffSet;
                cr.setSegundos_total(total);
            }
        });
    }


}