package com.example.learn.API;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.learn.database.DatabaseHelper;
import com.example.learn.models.Maquina;

import com.example.learn.models.MaquinaB;
import com.example.learn.models.Operacao;
import com.example.learn.responses.MaquinaBResponse;
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
        apiService.getMaquinas().enqueue(new Callback<MaquinaBResponse>() {
            @Override
            public void onResponse(Call<MaquinaBResponse> call, Response<MaquinaBResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MaquinaB> maquinas = response.body().getData();
                    for (MaquinaB maquina : maquinas) {

                        // Adiciona a máquina no banco de dados
                        dbHelper.inserirMaquina(maquina);

                        // Associa operações à máquina
                        for (Operacao operacao : maquina.getOperacoes()) {
                            dbHelper.addMaqOp(operacao.getId(), maquina.getId());
                        }
                    }
                }
                onSyncComplete.run();
            }

            @Override
            public void onFailure(Call<MaquinaBResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar máquinas: " + t.getMessage());
                onSyncComplete.run();
            }
        });
    }
    public void criarMaquinaComReenvio(final Maquina maquina, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Máquina não pôde ser criada após múltiplas tentativas.");
            Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
            return;
        }

        apiService.criarMaquina(maquina).enqueue(new Callback<MaquinaResponse>() {
            @Override
            public void onResponse(Call<MaquinaResponse> call, Response<MaquinaResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MaquinaResponse resposta = response.body();
                    if ("criado com sucesso!".equalsIgnoreCase(resposta.getMessage())) {
                        Log.i("API", "Máquina criada: " + resposta.getData());
                        Toast.makeText(context, "Enviado para API.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();

            }
        });

    }
}