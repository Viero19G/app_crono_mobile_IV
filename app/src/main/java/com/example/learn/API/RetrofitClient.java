package com.example.learn.API;

import com.example.learn.responses.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Criando um interceptor para capturar logs detalhados
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); // Log completo

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging) // Adiciona o interceptor de logs
                    .build();

            // Configurando Gson para tratar diferentes formatos de resposta
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(OperacaoResponse.class, new OperacaoResponse.OperacaoResponseDeserializer())
                    .registerTypeAdapter(AtividadesResponse.class, new AtividadesResponse.AtividadesResponseDeserializer())
                    .registerTypeAdapter(ClassificacaoResponse.class, new ClassificacaoResponse.ClassificacaoResponseDeserializer())
                    .registerTypeAdapter(MaquinaResponse.class, new MaquinaResponse.MaquinaResponseDeserializer())
                    .registerTypeAdapter(PostoResponse.class, new PostoResponse.PostoResponseDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://app-crono.robustec.com.br/api/") // API com HTTPS
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Usa Gson configurado
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
