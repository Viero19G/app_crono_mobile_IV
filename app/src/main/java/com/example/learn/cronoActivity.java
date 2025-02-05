package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.learn.API.AtividadeRepository;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Atividade;
import com.example.learn.models.CronometroGerente;

import java.util.ArrayList;
import java.util.List;

public class cronoActivity extends AppCompatActivity {

    private Button btnIniciar, btnProximo, btnParar, btnEnviar;
    private Spinner spPost, spMaq, spOp;
    private List<Integer> listaOperacoes, idsOp, idsMaq, idsPosto ,tempoOperacao;
    private List<Spinner> spinersOp = new ArrayList<>();
    private ScrollView scrollView;
    private ArrayAdapter<String> adptOp, adptMaq, adptPo;
    private DatabaseHelper dbHelp;
    private LinearLayout linear_op;
    private EditText dataInicio, dataFim, nomeAtv, obs;

    private Chronometer cronus;
    private int PauseOffSet = 0;
    private int maq, post, count;
    Context context;

    // Listas para armazenar os IDs dos elementos adicionados
    private List<Integer> listaSpinners = new ArrayList<>();
    private List<Integer> listaTextViews = new ArrayList<>();
    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crono);
        CronometroGerente cr;
        cr = new CronometroGerente();
        cronus = findViewById(R.id.chronometer);
        // Vincular componentes
        btnIniciar = findViewById(R.id.btn_InitC);
        dbHelp = new DatabaseHelper(this);
        dataInicio = findViewById(R.id.dataInicio);
        dataFim = findViewById(R.id.dataFim);
        nomeAtv = findViewById(R.id.nomeAtv);
        obs = findViewById(R.id.obs);
        btnProximo = findViewById(R.id.btnProximo);
        btnParar = findViewById(R.id.btnParar);
        btnEnviar = findViewById(R.id.btnEnviar);
        spPost = findViewById(R.id.spPost);
        spMaq = findViewById(R.id.spMaq);
        linear_op = findViewById(R.id.linear_op);
        scrollView = findViewById(R.id.scrowl); // Pegando o ScrollView

        idsMaq = dbHelp.getMaquinasIds();
        idsOp = dbHelp.getOperacoresIds();
        idsPosto = dbHelp.getPostoIds();

        // Configurar RecyclerView
        listaOperacoes = new ArrayList<Integer>();
        tempoOperacao = new ArrayList<Integer>();



        adptOp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getOperacores() );
        adptMaq  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getMaquinas() );
        adptPo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getPosto() );

        spMaq.setAdapter(adptMaq);
        int selectedPosition = spMaq.getSelectedItemPosition();
        maq = selectedPosition;
        spMaq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int indiceSelecionado = position;
                maq = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPost.setAdapter(adptPo);
        int selectedPosition2 = spPost.getSelectedItemPosition();
        post = selectedPosition2;
        spPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int indiceSelecionado2 = position;
                post = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                PauseOffSet = (int) (SystemClock.elapsedRealtime()- cronus.getBase() );
                cr.setSegundos_total(PauseOffSet);
            }
        });
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cronus.stop();
                int tempoAtual = (int) (SystemClock.elapsedRealtime() - cronus.getBase()) / 1000;
                int tempoUltimaOperacao = tempoAtual - cr.getSegundos_total(); // Diferença de tempo da última operação

                // Garante que o tempo não seja negativo
                if (tempoUltimaOperacao < 0) {
                    tempoUltimaOperacao = 0;
                }

                cr.setSegundos_total(tempoAtual);
                cr.setSegundos(tempoUltimaOperacao);
                tempoOperacao.add(tempoUltimaOperacao);
                cronus.start();

                // Criando um LinearLayout horizontal para o Spinner e TextView
                LinearLayout rowLayout = new LinearLayout(cronoActivity.this);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowLayout.setLayoutParams(rowParams);
                // Criando Spinner dinamicamente
                Spinner spOp = new Spinner(cronoActivity.this);
                spOp.setId(View.generateViewId());
                LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                        dpToPx(259),
                        dpToPx(39)
                );
                spOp.setAdapter(adptOp);
                int selectedPosition = spOp.getSelectedItemPosition();
                spOp.setLayoutParams(spinnerParams);
                spinersOp.add(spOp);
                spOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int indiceSelecionado = position;
                        listaOperacoes.add(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // Criando TextView dinamicamente
                TextView tv_seg = new TextView(cronoActivity.this);
                tv_seg.setId(View.generateViewId());
                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        dpToPx(75),
                        dpToPx(39)
                );
                tv_seg.setLayoutParams(textViewParams);
                tv_seg.setText("00:00");
                // Adicionando elementos ao layout horizontal
                rowLayout.addView(spOp);
                rowLayout.addView(tv_seg);

                // Adiciona o Spinner e o TextView ao layout (supondo que o layout principal seja um LinearLayout)
                LinearLayout layout = findViewById(R.id.linear_op); // Substitua pelo ID do seu layout
                layout.addView(rowLayout);

                // Salva os IDs no objeto cr
                cr.setId_tx(tv_seg.getId());
                cr.setId_sp(spOp.getId());

                // Armazenando os IDs dos elementos criados
                listaSpinners.add(spOp.getId());
                listaTextViews.add(tv_seg.getId());

                Integer old = listaTextViews.get(count);
                count = count +1;
                // Atualiza o TextView com o valor de PauseOffSet
                TextView textView = findViewById(old);
                if (textView != null) {
                    textView.setText(String.valueOf(cr.getSegundos()));
                    tempoOperacao.add(cr.getSegundos());
                }
                linear_op.requestLayout();
                scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
            }

            // Método para converter dp para pixels
            public int dpToPx(int dp) {
                float density = getResources().getDisplayMetrics().density;
                return Math.round(dp * density);
            }

    });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtividadeRepository repo = new AtividadeRepository(context);
                List<Integer> operacoes = new ArrayList<>();

                // Verifica se listaOperacoes e idsOp não estão vazios
                if (idsOp != null && !idsOp.isEmpty()) {
                    for (int indice : listaOperacoes) {
                        if (indice >= 0 && indice < idsOp.size()) {
                            operacoes.add(idsOp.get(indice));
                        } else {
                            operacoes.add(1); // Valor padrão para erros (pode ser tratado depois)
                        }
                    }
                }
                int total_seg = (cr.getSegundos_total()) / 1000;
                Atividade novaAtividade = new Atividade(
                        nomeAtv.getText().toString().trim(),  // Nome da atividade
                        dataInicio.getText().toString().trim(),
                        dataFim.getText().toString().trim(),
                        post,
                        maq,
                        obs.getText().toString().trim(),
                        operacoes,
                        tempoOperacao,
                        total_seg
                );


                repo.criarAtividadeComReenvio(novaAtividade, 1);
            }
        });

    }


}