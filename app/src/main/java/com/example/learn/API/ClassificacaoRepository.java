package com.example.learn.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Classificacao;

import com.example.learn.responses.ClassificacaoResponse;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassificacaoRepository {
    private final ApiService apiService;
    private final DatabaseHelper dbHelper;
    SyncManager sm;
    Context context;



    public ClassificacaoRepository(Context context) {
        this.context = context;  // ADICIONE ESTA LINHA
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void sincronizarClassificacao(Runnable onSyncComplete) {
        apiService.getClassificacao().enqueue(new Callback<ClassificacaoResponse>() {
            @Override
            public void onResponse(Call<ClassificacaoResponse> call, Response<ClassificacaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Classificacao> classificacaos = response.body().getData();
                    for (Classificacao classificacao : classificacaos) {
                        dbHelper.inserirClassificacao(classificacao);
                    }
                }
                if (onSyncComplete != null) onSyncComplete.run(); // Indica que a sync finalizou
            }

            @Override
            public void onFailure(Call<ClassificacaoResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar classificacoes: " + t.getMessage());
                if (onSyncComplete != null) onSyncComplete.run();
            }
        });
    }

    public void criarClassificacaoComReenvio(final Classificacao classificacao, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Classificação não pôde ser criada após múltiplas tentativas.");
            Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
            return;
        }

        apiService.criarClassificacao(classificacao).enqueue(new Callback<ClassificacaoResponse>() {
            @Override
            public void onResponse(Call<ClassificacaoResponse> call, Response<ClassificacaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ClassificacaoResponse resposta = response.body();
                    if ("criado com sucesso!".equalsIgnoreCase(resposta.getMessage())) {
                        Log.i("API", "Classificação criada: " + resposta.getData());
                        Toast.makeText(context, "Enviado para API.", Toast.LENGTH_LONG).show();

                    } else {
                        Log.w("API", "Resposta inesperada, reenviando... (" + tentativas + " restantes)");
                        criarClassificacaoComReenvio(classificacao, tentativas - 1);
                    }
                } else {
                    Log.e("API", "Erro na resposta da API, reenviando... (" + tentativas + " restantes)");
                    criarClassificacaoComReenvio(classificacao, tentativas - 1);
                }
            }

            @Override
            public void onFailure(Call<ClassificacaoResponse> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
                criarClassificacaoComReenvio(classificacao, tentativas - 1);

            }
        });

    }

}