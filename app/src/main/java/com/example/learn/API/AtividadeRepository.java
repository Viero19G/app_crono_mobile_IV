package com.example.learn.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Atividade;

import com.example.learn.responses.AtividadesResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtividadeRepository {
    private final ApiService apiService;
    private final DatabaseHelper dbHelper;
    SyncManager sm;
    Context context;

    public AtividadeRepository(Context context) {
        this.context = context;  // ADICIONE ESTA LINHA
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void criarAtividadeComReenvio(final Atividade atividade, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Atividade não pôde ser criada após múltiplas tentativas.");
            Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
            return;
        }

        apiService.criarAtividade(atividade).enqueue(new Callback<AtividadesResponse>() {
            @Override
            public void onResponse(Call<AtividadesResponse> call, Response<AtividadesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AtividadesResponse resposta = response.body();
                    if ("criado com sucesso!".equalsIgnoreCase(resposta.getMessage())) {
                        Log.i("API", "Atividade criada: " + resposta.getData());
                        Toast.makeText(context, "Enviado para API.", Toast.LENGTH_LONG).show();

                    } else {
                        Log.w("API", "Resposta inesperada, reenviando... (" + tentativas + " restantes)");
                        criarAtividadeComReenvio(atividade, tentativas - 1);
                    }
                } else {
                    Log.e("API", "Erro na resposta da API, reenviando... (" + tentativas + " restantes)");
                    criarAtividadeComReenvio(atividade, tentativas - 1);
                }
            }

            @Override
            public void onFailure(Call<AtividadesResponse> call, Throwable t) {
                Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                criarAtividadeComReenvio(atividade, tentativas - 1);
            }
        });
    }
}
