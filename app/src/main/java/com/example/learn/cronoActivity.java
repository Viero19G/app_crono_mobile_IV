package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import com.example.learn.adapter.OperacaoAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);

        // Vincular componentes
        btnIniciar = findViewById(R.id.btn_InitC);
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

        // Iniciar Cronômetro
        btnIniciar.setOnClickListener(v -> iniciarCronometro());

        // Próxima Operação
        btnProximo.setOnClickListener(v -> proximaOperacao());

        // Parar e Registrar
        btnParar.setOnClickListener(v -> pararCronometro());
    }

    private void iniciarCronometro() {
        if (!isCronometroRodando) {
            inicioTempo = System.currentTimeMillis();
            isCronometroRodando = true;
        }
    }

    private void proximaOperacao() {
        if (isCronometroRodando) {
            long tempoAtual = System.currentTimeMillis();
            int tempoDecorrido = (int) ((tempoAtual - inicioTempo) / 1000);
            inicioTempo = tempoAtual;

            int operacaoId = 1; // Pegamos do spinner (exemplo)
            listaOperacoes.add(new OperacaoTempo(operacaoId, tempoDecorrido));
            adapter.notifyDataSetChanged();
        }
    }

    private void pararCronometro() {
        if (isCronometroRodando) {
            isCronometroRodando = false;
            long tempoFinal = System.currentTimeMillis();
            int tempoTotal = (int) ((tempoFinal - inicioTempo) / 1000);
            listaOperacoes.add(new OperacaoTempo(0, tempoTotal)); // Tempo final
            adapter.notifyDataSetChanged();
        }
    }
}