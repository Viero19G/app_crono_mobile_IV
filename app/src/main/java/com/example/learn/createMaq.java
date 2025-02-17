package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

//import com.example.learn.API.ApiService;
import com.example.learn.API.MaquinaRepository;
import com.example.learn.API.SyncManager;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Classificacao;
import com.example.learn.models.Maquina;
import com.example.learn.models.Operacao;

import java.util.ArrayList;
import java.util.List;

public class createMaq extends AppCompatActivity {
    private ArrayAdapter<Operacao> adptOp;
    private DatabaseHelper dbHelp;
    private List<Integer> idsOp;
    private List<Operacao> listaOperacoesObj;
    private List<Maquina> maqsSpiner;
    private ArrayList<Spinner> spsOps;
    EditText nome_maq, desc_maq;
    Button send_maq, btn_add_op;
    Context context;
    SyncManager sm;
    LinearLayout linear_op_maq;
    private ScrollView scrollView_maq;
    private int id_maq;
    private Spinner spinner;


    private List<Spinner> spinersOp = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maq);
        sm = new SyncManager(createMaq.this);
        listaOperacoesObj = new ArrayList<Operacao>();
        dbHelp = new DatabaseHelper(this);
        idsOp = dbHelp.getOperacoresIds();
        btn_add_op = findViewById(R.id.btn_add_op_maq);
        linear_op_maq = findViewById(R.id.linear_op_maq);
        nome_maq = findViewById(R.id.nome_maq);
        desc_maq = findViewById(R.id.deesc_maq);
        send_maq = findViewById(R.id.send_maq);
        scrollView_maq = findViewById(R.id.scrollView_maq);

        dbHelp = new DatabaseHelper(createMaq.this);

        btn_add_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adptOp = new ArrayAdapter<>(createMaq.this, android.R.layout.simple_spinner_item, dbHelp.getOperacores());
                adptOp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Criando um LinearLayout horizontal para o Spinner
                LinearLayout rowLayout = new LinearLayout(createMaq.this);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                rowLayout.setLayoutParams(rowParams);

                // Criando Spinner dinamicamente
                Spinner spOp = new Spinner(createMaq.this);
                spOp.setId(View.generateViewId());
                LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                        dpToPx(259),
                        dpToPx(39)
                );
                spOp.setAdapter(adptOp);
                spOp.setLayoutParams(spinnerParams);

                // Adiciona o Spinner ao array de Spinners
                spinersOp.add(spOp);

                rowLayout.addView(spOp);
                linear_op_maq.addView(rowLayout);
                linear_op_maq.requestLayout();
                scrollView_maq.post(() -> scrollView_maq.fullScroll(View.FOCUS_DOWN));
            }

            public int dpToPx(int dp) {
                float density = getResources().getDisplayMetrics().density;
                return Math.round(dp * density);
            }
        });


        send_maq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nome_maq.getText().toString().trim();
                String descricao = desc_maq.getText().toString().trim();

                if (nome.isEmpty() || descricao.isEmpty()) {
                    Toast.makeText(createMaq.this, "Preencha todos os campos da máquina!", Toast.LENGTH_SHORT).show();
                    return;
                }

                idsOp.clear(); // Limpa lista antes de preencher para evitar seleções antigas

                for (Spinner spOp : spinersOp) {
                    Operacao operacaoSelecionada = (Operacao) spOp.getSelectedItem();
                    if (operacaoSelecionada != null) {
                        idsOp.add(operacaoSelecionada.getId()); // Adiciona apenas o que está selecionado no momento
                    }
                }

                DatabaseHelper dbHelp = new DatabaseHelper(createMaq.this);
                dbHelp.addMaquina(nome, descricao);
                int maq_id = dbHelp.maqId(nome, descricao);

                MaquinaRepository repo = new MaquinaRepository(createMaq.this);
                Maquina novaMaquina = new Maquina();
                novaMaquina.setNome(nome);
                novaMaquina.setDescricao(descricao);
                novaMaquina.setOperacoes(idsOp);

                repo.criarMaquinaComReenvio(novaMaquina, 1);
            }
        });
    }
}