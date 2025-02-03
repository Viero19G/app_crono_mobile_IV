package com.example.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.example.learn.API.ApiService;
import com.example.learn.API.MaquinaRepository;
import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Maquina;

public class createMaq extends AppCompatActivity {
    EditText nome_maq, desc_maq;
    Button send_maq;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maq);

        nome_maq = findViewById(R.id.nome_maq);
        desc_maq = findViewById(R.id.deesc_maq);
        send_maq = findViewById(R.id.send_maq);

        send_maq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelp= new DatabaseHelper(createMaq.this);
                //ApiService api = new ApiService();
                dbHelp.addMaquina(nome_maq.getText().toString().trim(),
                        desc_maq.getText().toString().trim());
                MaquinaRepository repo = new MaquinaRepository(context);
                Maquina novaMaquina = new Maquina(nome_maq.getText().toString(), desc_maq.getText().toString());
                repo.criarMaquinaComReenvio(novaMaquina, 1);
            }
        });
    }
}