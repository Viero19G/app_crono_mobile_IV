package com.example.learn.API;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.learn.database.DatabaseHelper;

import com.example.learn.models.PostoTrabalho;

import com.example.learn.responses.PostoResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostoRepository {
    private final ApiService apiService;
    private final DatabaseHelper dbHelper;
    SyncManager sm;
    Context context;

    public PostoRepository(Context context) {
        this.context = context;  // ADICIONE ESTA LINHA
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void sincronizarPostos(Runnable onSyncComplete) {
        apiService.getPostos().enqueue(new Callback<PostoResponse>() {
            @Override
            public void onResponse(Call<PostoResponse> call, Response<PostoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostoTrabalho> postos = response.body().getData();
                    for (PostoTrabalho posto : postos) {
                        dbHelper.inserirPosto(posto);
                    }
                }
                onSyncComplete.run();
            }

            @Override
            public void onFailure(Call<PostoResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar postos: " + t.getMessage());
                onSyncComplete.run();
            }
        });
    }



    public void criarPostoComReenvio(final PostoTrabalho postoTrabalho, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Posto não pôde ser criada após múltiplas tentativas.");
            Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
            return;
        }

        apiService.criarPosto(postoTrabalho).enqueue(new Callback<PostoResponse>() {
            @Override
            public void onResponse(Call<PostoResponse> call, Response<PostoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostoResponse resposta = response.body();
                    if ("criado com sucesso!".equals(resposta.getMessage())) {
                        Log.i("API", "Posto criado: " + resposta.getData().toString());
                        Toast.makeText(context, "Enviado para API.", Toast.LENGTH_LONG).show();

                    } else {
                        Log.w("API", "Resposta inesperada, reenviando... (" + tentativas + " restantes)");
                        criarPostoComReenvio(postoTrabalho, tentativas - 1);
                    }
                } else {
                    Log.e("API", "Erro na resposta da API, reenviando... (" + tentativas + " restantes)");
                    criarPostoComReenvio(postoTrabalho, tentativas - 1);
                }
            }

            @Override
            public void onFailure(Call<PostoResponse> call, Throwable t) {
                Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();

                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                criarPostoComReenvio(postoTrabalho, tentativas - 1);
                Toast.makeText(context, "Erro ao enviar para API.", Toast.LENGTH_LONG).show();
            }
        });

    }
}
