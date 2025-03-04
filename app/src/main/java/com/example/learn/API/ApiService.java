package com.example.learn.API;


import com.example.learn.models.Atividade;
import com.example.learn.models.Classificacao;
import com.example.learn.models.Maquina;
import com.example.learn.models.Operacao;
import com.example.learn.models.PostoTrabalho;
import com.example.learn.responses.AtividadesResponse;
import com.example.learn.responses.ClassificacaoResponse;
import com.example.learn.responses.MaquinaBResponse;
import com.example.learn.responses.MaquinaResponse;
import com.example.learn.responses.OperacaoResponse;
import com.example.learn.responses.PostoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

// Define os endpoints da API
public interface ApiService {

    // GET: Busca uma lista de Postos:
    @GET("postos/postolist/")
    Call<PostoResponse> getPostos();

    // POST: Envia uma nova atividade para a API
    @POST("postos/postocreate/")
    Call<PostoResponse> criarPosto(@Body PostoTrabalho postoTrabalho);

    @GET("classificacoes/classificacaolist/")
    Call<ClassificacaoResponse> getClassificacao();

    // POST: Envia uma nova atividade para a API
    @POST("classificacoes/classificacaocreate/")
    Call<ClassificacaoResponse> criarClassificacao(@Body Classificacao classificacao);

    @GET("maquinas/maquinalist/")
    Call<MaquinaBResponse> getMaquinas();

    @POST("maquinas/maquinacreate/")
    Call<MaquinaResponse> criarMaquina(@Body Maquina maquina);

    @GET("operacoes/operacaolist/")
    Call<OperacaoResponse> getOperacao();

    @POST("operacoes/operacaocreate/")
    Call<OperacaoResponse> criarOperacao(@Body Operacao operacao);

    @POST("atividades/")
    Call<AtividadesResponse> criarAtividade(@Body Atividade atividade);


}
