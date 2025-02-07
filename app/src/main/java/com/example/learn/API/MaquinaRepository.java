package com.example.learn.API;
import android.content.Context;
import android.util.Log;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Maquina;

import com.example.learn.responses.MaquinaResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaquinaRepository {
    private final ApiService apiService;
    private final DatabaseHelper dbHelper;
    SyncManager sm;
    Context context;

    public MaquinaRepository(Context context) {
        this.context = context;  // ADICIONE ESTA LINHA
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void sincronizarMaquinas(Runnable onSyncComplete) {
        apiService.getMaquinas().enqueue(new Callback<MaquinaResponse>() {
            @Override
            public void onResponse(Call<MaquinaResponse> call, Response<MaquinaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Maquina> maquinas = response.body().getData();
                    for (Maquina maquina : maquinas) {
                        dbHelper.inserirMaquina(maquina);
                    }
                }
                onSyncComplete.run();
            }

            @Override
            public void onFailure(Call<MaquinaResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar máquinas: " + t.getMessage());
                onSyncComplete.run();
            }
        });
    }
    public void criarMaquinaComReenvio(final Maquina maquina, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Máquina não pôde ser criada após múltiplas tentativas.");
            return;
        }

        apiService.criarMaquina(maquina).enqueue(new Callback<MaquinaResponse>() {
            @Override
            public void onResponse(Call<MaquinaResponse> call, Response<MaquinaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MaquinaResponse resposta = response.body();
                    if ("Maquina criada com sucesso!".equalsIgnoreCase(resposta.getMessage())) {
                        Log.i("API", "Máquina criada: " + resposta.getData());
                    } else {
                        Log.w("API", "Resposta inesperada, reenviando... (" + tentativas + " restantes)");
                        criarMaquinaComReenvio(maquina, tentativas - 1);
                    }
                } else {
                    Log.e("API", "Erro na resposta da API, reenviando... (" + tentativas + " restantes)");
                    criarMaquinaComReenvio(maquina, tentativas - 1);
                }
            }

            @Override
            public void onFailure(Call<MaquinaResponse> call, Throwable t) {
                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                criarMaquinaComReenvio(maquina, tentativas - 1);
            }
        });

    }
}