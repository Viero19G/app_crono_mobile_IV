package com.example.learn.API;

import android.content.Context;
import android.widget.Toast;
import com.example.learn.database.DatabaseHelper;


import okhttp3.OkHttpClient;


public class SyncManager {
    private final OkHttpClient client;
    private final MaquinaRepository maquinaRepository;
    private final OperacaoRepository operacaoRepository;
    private final PostoRepository postoRepository;
    private final ClassificacaoRepository classificacaoRepository;
    private final DatabaseHelper dbHelper;
    private final Context context;
    private int syncCount = 0; // Contador de sincronizações ativas
    private Runnable onComplete; // Callback quando todas as syncs terminarem

    public SyncManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.client = new OkHttpClient();

        this.maquinaRepository = new MaquinaRepository(context);
        this.operacaoRepository = new OperacaoRepository(context);
        this.postoRepository = new PostoRepository(context);
        this.classificacaoRepository = new ClassificacaoRepository(context);
    }


    public void syncDB(Runnable onComplete) {
        this.onComplete = onComplete;
        dbHelper.clearTables();

        syncCount = 4; // Número total de sincronizações que precisam finalizar


        postoRepository.sincronizarPostos(this::onSyncComplete);
        classificacaoRepository.sincronizarClassificacao(this::onSyncComplete);
        operacaoRepository.sincronizarOperacoes(this::onSyncComplete);
        maquinaRepository.sincronizarMaquinas(this::onSyncComplete);
    }

    private synchronized void onSyncComplete() {
        syncCount--;
        if (syncCount == 0 && onComplete != null) {
            onComplete.run();
        }
    }

    private void showToast(String message) {
        new android.os.Handler(context.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
    }
}
