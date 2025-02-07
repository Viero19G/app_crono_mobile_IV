package com.example.learn.API;
import android.content.Context;
import android.util.Log;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Maquina;
import com.example.learn.models.Operacao;

import com.example.learn.responses.MaquinaResponse;
import com.example.learn.responses.OperacaoResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperacaoRepository {

    private final ApiService apiService;
    private final DatabaseHelper dbHelper;
    SyncManager sm;
    Context context;

    public OperacaoRepository(Context context) {
        this.context = context;  // ADICIONE ESTA LINHA
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void sincronizarOperacoes(Runnable onSyncComplete) {
        apiService.getOperacao().enqueue(new Callback<OperacaoResponse>() {
            @Override
            public void onResponse(Call<OperacaoResponse> call, Response<OperacaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Operacao> operacoes = response.body().getData();
                    for (Operacao operacao : operacoes) {
                        dbHelper.inserirOperacoes(operacao);
                    }
                }
                onSyncComplete.run();
            }

            @Override
            public void onFailure(Call<OperacaoResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar operações: " + t.getMessage());
                onSyncComplete.run();
            }
        });
    }


    public void criarOperacaoComReenvio(final Operacao operacao, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Operação não pôde ser criada após múltiplas tentativas.");
            return;
        }

        apiService.criarOperacao(operacao).enqueue(new Callback<OperacaoResponse>() {
            @Override
            public void onResponse(Call<OperacaoResponse> call, Response<OperacaoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    OperacaoResponse resposta = response.body();
                    if ("Operacao criada com sucesso!".equalsIgnoreCase(resposta.getMessage())) {
                        Log.i("API", "Operação criada: " + resposta.getData());
                    } else {
                        Log.w("API", "Resposta inesperada, reenviando... (" + tentativas + " restantes)");
                        criarOperacaoComReenvio(operacao, tentativas - 1);
                    }
                } else {
                    Log.e("API", "Erro na resposta da API, reenviando... (" + tentativas + " restantes)");
                    criarOperacaoComReenvio(operacao, tentativas - 1);
                }
            }

            @Override
            public void onFailure(Call<OperacaoResponse> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                criarOperacaoComReenvio(operacao, tentativas - 1);
            }
        });

    }

}
