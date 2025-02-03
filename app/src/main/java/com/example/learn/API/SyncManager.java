package com.example.learn.API;

import android.content.Context;
import android.widget.Toast;
import com.example.learn.database.DatabaseHelper;


import okhttp3.OkHttpClient;


public class SyncManager {
    private final MaquinaRepository maquinaRepository;
    private final OperacaoRepository operacaoRepository;
    private final PostoRepository postoRepository;

    private final OkHttpClient client;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public SyncManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.client = new OkHttpClient();

        this.maquinaRepository = new MaquinaRepository(context);
        this.operacaoRepository = new OperacaoRepository(context);
        this.postoRepository = new PostoRepository(context);
    }


    public void syncDB(){
        dbHelper.clearTables();
        maquinaRepository.sincronizarMaquinas();
        operacaoRepository.sincronizarOperacoes();
        postoRepository.sincronizarPostos();
    }

    // Repeat similar logic for syncOperacoes() and syncMaquinas()

    private void showToast(String message) {
        new android.os.Handler(context.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}
