package com.example.learn.API;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.learn.R;
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
    private int completedSyncs = 0;
    public SyncManager(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.client = new OkHttpClient();

        this.maquinaRepository = new MaquinaRepository(context);
        this.operacaoRepository = new OperacaoRepository(context);
        this.postoRepository = new PostoRepository(context);
        this.classificacaoRepository = new ClassificacaoRepository(context);
    }


    public void syncDB(Runnable onComplete, Context context) {
        this.onComplete = onComplete;
        dbHelper.clearTables();

        syncCount = 4;
        completedSyncs = 0;

        // Criar o diálogo personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_progress, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();

        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        TextView txtProgress = dialogView.findViewById(R.id.txt_progress);

        progressBar.setProgress(0);

        // Chamar sincronizações
        postoRepository.sincronizarPostos(() -> onSyncComplete(dialog, progressBar, txtProgress));
        classificacaoRepository.sincronizarClassificacao(() -> onSyncComplete(dialog, progressBar, txtProgress));
        operacaoRepository.sincronizarOperacoes(() -> onSyncComplete(dialog, progressBar, txtProgress));
        maquinaRepository.sincronizarMaquinas(() -> onSyncComplete(dialog, progressBar, txtProgress));
    }

    private synchronized void onSyncComplete(AlertDialog dialog, ProgressBar progressBar, TextView txtProgress) {
        completedSyncs++;
        int progress = (completedSyncs * 100) / syncCount;

        progressBar.setProgress(progress);
        txtProgress.setText("Sincronizando... " + progress + "%");

        if (completedSyncs >= syncCount) {
            dialog.dismiss();
            if (onComplete != null) {
                onComplete.run();
            }
        }
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
