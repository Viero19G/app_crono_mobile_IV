package com.example.learn.API;
import android.content.Context;
import android.util.Log;

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

    public PostoRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        dbHelper = new DatabaseHelper(context);
    }

    public void sincronizarPostos() {
        apiService.getPostos().enqueue(new Callback<PostoResponse>() {
            @Override
            public void onResponse(Call<PostoResponse> call, Response<PostoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PostoTrabalho> postos = response.body().getData();
                    for (PostoTrabalho posto : postos) {
                        dbHelper.inserirPosto(posto);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostoResponse> call, Throwable t) {
                Log.e("API", "Erro ao buscar postos: " + t.getMessage());
            }
        });
    }


    public void criarPostoComReenvio(final PostoTrabalho postoTrabalho, final int tentativas) {
        if (tentativas <= 0) {
            Log.e("API", "Posto não pôde ser criada após múltiplas tentativas.");
            return;
        }

        apiService.criarPosto(postoTrabalho).enqueue(new Callback<PostoResponse>() {
            @Override
            public void onResponse(Call<PostoResponse> call, Response<PostoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PostoResponse resposta = response.body();
                    if ("Posto criado com sucesso!".equals(resposta.getMessage())) {
                        Log.i("API", "Posto criada: " + resposta.getData().toString());
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
                Log.e("API", "Falha na requisição: " + t.getMessage() + ". Tentando novamente... (" + tentativas + " restantes)");
                criarPostoComReenvio(postoTrabalho, tentativas - 1);
            }
        });
        sm.syncDB();
    }
}
