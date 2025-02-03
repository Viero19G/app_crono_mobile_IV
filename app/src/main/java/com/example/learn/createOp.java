package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.example.learn.API.ApiService;
import com.example.learn.API.OperacaoRepository;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Operacao;

public class createOp extends AppCompatActivity {
    EditText nome_op, desc_op, cass_op;
    Button send_op;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_op);
        nome_op = findViewById(R.id.nome_op);
        desc_op = findViewById(R.id.deesc_op);
        cass_op = findViewById(R.id.cass_op);
        send_op = findViewById(R.id.send_op);

        send_op.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp= new DatabaseHelper(createOp.this);
                //ApiService api = new ApiService();
                dbHelp.addOperacao(nome_op.getText().toString().trim(),
                        desc_op.getText().toString().trim(),
                        cass_op.getText().toString().trim());
                OperacaoRepository repo = new OperacaoRepository(context);
                Operacao novaOp = new Operacao(nome_op.getText().toString(), desc_op.getText().toString(), cass_op.getText().toString());
                repo.criarOperacaoComReenvio(novaOp, 1);
            }
        });
    }
}