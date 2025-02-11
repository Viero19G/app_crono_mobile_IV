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
    private ArrayAdapter<String> adptOp;
    private DatabaseHelper dbHelp;
    private List<Integer> listaOperacoes, idsOp;
    private List<Operacao> listaOperacoesObj;
    private List<Maquina> maqsSpiner;
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
        adptOp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,dbHelp.getOperacores() );
        dbHelp = new DatabaseHelper(createMaq.this);

        btn_add_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaOperacoes = new ArrayList<>();
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
                rowLayout.addView(spOp);
                LinearLayout layout = findViewById(R.id.linear_op_maq);
                layout.addView(rowLayout);
                layout.requestLayout();
                layout.post(() -> scrollView_maq.fullScroll(View.FOCUS_DOWN));
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
                DatabaseHelper dbHelp= new DatabaseHelper(createMaq.this);
                //ApiService api = new ApiService();
                dbHelp.addMaquina(nome_maq.getText().toString().trim(),
                        desc_maq.getText().toString().trim());
                int maq_id = dbHelp.maqId(nome_maq.getText().toString().trim(),
                         desc_maq.getText().toString().trim());

                List<Integer> operacoes = new ArrayList<>();
                if (idsOp != null && !idsOp.isEmpty()) {
                    for (int indice : listaOperacoes) {
                        if (indice >= 0 && indice < idsOp.size()) {
                            operacoes.add(idsOp.get(indice));
                        } else {
                            operacoes.add(1); // Valor padrão para erros (pode ser tratado depois)
                        }
                    }
                }
                for (int op : operacoes){
                    dbHelp.addMaqOp(op, maq_id);
                    Operacao opLoc = dbHelp.opsPorId(op);
                    listaOperacoesObj.add(opLoc);
                }
                MaquinaRepository repo = new MaquinaRepository(context);
                Maquina novaMaquina = new Maquina();
                novaMaquina.setNome(nome_maq.getText().toString().trim());
                novaMaquina.setDescricao(desc_maq.getText().toString().trim());
                novaMaquina.setOperacoes(operacoes);

                repo.criarMaquinaComReenvio(novaMaquina, 1);
            }


        });



    }
}