package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.learn.API.OperacaoRepository;
import com.example.learn.API.SyncManager;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Classificacao;
import com.example.learn.models.Operacao;

import java.util.ArrayList;

public class createOp extends AppCompatActivity {
    EditText nome_op, desc_op;
    Button send_op;
    Context context;
    private Spinner spinner;
    private ArrayList<Classificacao> classificacao;
    private DatabaseHelper dbHelper;
    private int id_class;
    SyncManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_op);

        nome_op = findViewById(R.id.nome_op);
        desc_op = findViewById(R.id.deesc_op);
        send_op = findViewById(R.id.send_op);
        spinner = findViewById(R.id.spinnerOpIns);

        dbHelper = new DatabaseHelper(this);
        sm = new SyncManager(this);

        // Executa sincronização e só carrega o Spinner quando terminar
        sm.syncDB(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        carregarSpinner();
                    }
                });
            }
        });

        send_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp = new DatabaseHelper(createOp.this);

                dbHelp.inserirOperacoesSemId(
                        nome_op.getText().toString().trim(),
                        desc_op.getText().toString().trim(),
                        id_class
                );
                OperacaoRepository repo = new OperacaoRepository(createOp.this);
                Operacao novaOp = new Operacao(
                        nome_op.getText().toString(),
                        desc_op.getText().toString(),
                        id_class
                );
                repo.criarOperacaoComReenvio(novaOp, 1);
            }
        });
    }

    private void carregarSpinner() {
        classificacao = dbHelper.getClassificacao();

        if (!classificacao.isEmpty()) {
            id_class = classificacao.get(0).getId();
        }

        ArrayAdapter<Classificacao> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classificacao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Classificacao classificacaoSelecionada = (Classificacao) parent.getItemAtPosition(position);
                id_class = classificacaoSelecionada.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
