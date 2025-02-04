package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.learn.models.CronometroGerente;
import com.example.learn.models.OperacaoTempo;

import java.util.ArrayList;
import java.util.List;

public class cronoActivity extends AppCompatActivity {

    private Button btnIniciar, btnProximo, btnParar, btnEnviar;
    private Spinner spPost, spMaq, spOp;
    private List<OperacaoTempo> listaOperacoes;

    private LinearLayout linear_op;
    private TextView tv;
    private Chronometer cronus;
    private int PauseOffSet = 0;
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);
        CronometroGerente cr;
        cr = new CronometroGerente();
        cronus = findViewById(R.id.chronometer);
        // Vincular componentes
        btnIniciar = findViewById(R.id.btn_InitC);

        btnProximo = findViewById(R.id.btnProximo);
        btnParar = findViewById(R.id.btnParar);
        btnEnviar = findViewById(R.id.btnEnviar);
        spPost = findViewById(R.id.spPost);
        spMaq = findViewById(R.id.spMaq);
        linear_op = findViewById(R.id.linear_op);


        // Configurar RecyclerView
        listaOperacoes = new ArrayList<>();


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
                // Cálculos iniciais
                PauseOffSet = (int) (SystemClock.elapsedRealtime() - cronus.getBase() / 1000);
                int seg_old = cr.getSegundos();
                cr.setSegundos(PauseOffSet);
                int total = seg_old + PauseOffSet;
                cr.setSegundos_total(total);

                // Cria o Spinner dinamicamente
                Spinner spOp = new Spinner(cronoActivity.this, null, android.R.attr.spinnerStyle);
                spOp.setId(View.generateViewId()); // Gera um ID único para o Spinner



                // Define o layout_width e layout_height do Spinner
                LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                        dpToPx(259), // largura em pixels (convertida de 259dp)
                        dpToPx(39)   // altura em pixels (convertida de 39dp)
                );
                spOp.setLayoutParams(spinnerParams);

                // Cria o TextView dinamicamente
                TextView tv_seg = new TextView(cronoActivity.this);
                tv_seg.setId(View.generateViewId()); // Gera um ID único para o TextView

                // Define o layout_width e layout_height do TextView
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        dpToPx(75), // largura em pixels (convertida de 75dp)
                        dpToPx(39)  // altura em pixels (convertida de 39dp)
                );
                tv_seg.setLayoutParams(textViewParams);

                // Define o texto inicial do TextView
                tv_seg.setText("00");

                // Adiciona o Spinner e o TextView ao layout (supondo que o layout principal seja um LinearLayout)
                LinearLayout layout = findViewById(R.id.linear_op); // Substitua pelo ID do seu layout
                layout.addView(spOp);
                layout.addView(tv_seg);

                // Salva os IDs no objeto cr
                cr.setId_tx(tv_seg.getId());
                cr.setId_sp(spOp.getId());

                // Atualiza o TextView com o valor de PauseOffSet
                TextView textView = findViewById(cr.getId_tx());
                if (textView != null) {
                    textView.setText(String.valueOf(PauseOffSet));
                }
            }

            // Método para converter dp para pixels
            public int dpToPx(int dp) {
                float density = getResources().getDisplayMetrics().density;
                return Math.round(dp * density);
            }
    });}


}