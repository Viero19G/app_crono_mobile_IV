package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.Toast;


import com.example.learn.API.AtividadeRepository;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Atividade;
import com.example.learn.models.CronometroGerente;
import com.example.learn.models.Maquina;
import com.example.learn.models.Operacao;
import com.example.learn.models.PostoTrabalho;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class cronoActivity extends AppCompatActivity {

    private Button btnIniciar, btnProximo, btnParar, btnEnviar;
    private Spinner spPost, spMaq, spOp;
    private List<Integer> listaOperacoes, idsOp, idsMaq, idsPosto ,tempoOperacao;
    private List<Spinner> spinersOp = new ArrayList<>();
    private ScrollView scrollView;
    private ArrayAdapter<Operacao> adptOp;
    private ArrayAdapter<Maquina> adptMaq;
    private ArrayAdapter<PostoTrabalho> adptPo;
    private DatabaseHelper dbHelp;
    private LinearLayout linear_op;
    private EditText dataInicio, dataFim, nomeAtv, obs;
    private ArrayList<Operacao> ops;
    private Chronometer cronus;
    private int PauseOffSet = 0;
    private int maq, post, count;
    private final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

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


        // Define o comportamento ao clicar nos campos
        dataInicio.setOnClickListener(v -> showDateTimePicker(dataInicio));
        dataFim.setOnClickListener(v -> showDateTimePicker(dataFim));

        adptMaq  = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getMaquinas() );
        adptPo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getPosto() );

        adptMaq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMaq.setAdapter(adptMaq);

        btnEnviar.setEnabled(false);
        btnProximo.setEnabled(false);
        spMaq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Maquina maquinaSelecionada = (Maquina) parent.getItemAtPosition(position);
                maq = maquinaSelecionada.getId();
                String nome = maquinaSelecionada.getNome();
                ops = dbHelp.getOperacoesMaq(maq);


                if (ops != null && !ops.isEmpty()) {
                    Toast.makeText(cronoActivity.this, "Operações encontradas para : " + nome, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(cronoActivity.this, "Nenhuma operação disponível para esta máquina: " + maq, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPost.setAdapter(adptPo);
        spPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PostoTrabalho postoTrabalho = (PostoTrabalho) parent.getItemAtPosition(position);
                post = postoTrabalho.getId();
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
                spMaq.setEnabled(false);
                spPost.setEnabled(false);
                dataInicio.setEnabled(false);
                btnEnviar.setEnabled(false);
                btnProximo.setEnabled(true);
            }
        });
        btnParar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnviar.setEnabled(true);
                cr.setRodando(false);
                cronus.stop();
                PauseOffSet = (int) (SystemClock.elapsedRealtime()- cronus.getBase() );
                cr.setSegundos_total(PauseOffSet);
            }
        });
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Desativa para impedir alteração da maquina após iniciada cronoAnalise
                spMaq.setEnabled(false);
                spPost.setEnabled(false);
                dataInicio.setEnabled(false);
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
                adptOp = new ArrayAdapter<>(cronoActivity.this, android.R.layout.simple_list_item_1, ops);
                adptOp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spOp.setAdapter(adptOp);

                int selectedPosition = spOp.getSelectedItemPosition();
                spOp.setLayoutParams(spinnerParams);
                spinersOp.add(spOp);
                spOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                dataFim.setEnabled(false);
                obs.setEnabled(false);
                nomeAtv.setEnabled(false);
                btnProximo.setEnabled(false);
                AtividadeRepository repo = new AtividadeRepository(cronoActivity.this);
                List<Integer> operacoes = new ArrayList<>();

                for (Spinner spOp : spinersOp) {
                    Operacao operacaoSelecionada = (Operacao) spOp.getSelectedItem();
                    if (operacaoSelecionada != null) {
                        operacoes.add(operacaoSelecionada.getId());
                    }
                }
                int total_seg = (cr.getSegundos_total()) / 1000;
                String nome = nomeAtv.getText().toString().trim();
                String dataIni = dataInicio.getText().toString().trim();
                String dataF = dataFim.getText().toString().trim();

                if (nome.isEmpty() || dataIni.isEmpty() || dataF.isEmpty() || post <= 0 || maq <= 0) {
                    Toast.makeText(cronoActivity.this, "Preencha todos os campos da atividade!", Toast.LENGTH_SHORT).show();
                    dataFim.setEnabled(true);
                    obs.setEnabled(true);
                    nomeAtv.setEnabled(true);
                    btnProximo.setEnabled(true);
                    return;
                }

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
    private void showDateTimePicker(EditText editText) {
        Calendar newCalendar = Calendar.getInstance();

        // Primeiro, exibe o DatePickerDialog
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            newCalendar.set(Calendar.YEAR, year);
            newCalendar.set(Calendar.MONTH, month);
            newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Após escolher a data, exibe o TimePickerDialog
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                newCalendar.set(Calendar.MINUTE, minute);

                // Formata e define o texto no EditText
                editText.setText(dateTimeFormat.format(newCalendar.getTime()));
            }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true).show();

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

}