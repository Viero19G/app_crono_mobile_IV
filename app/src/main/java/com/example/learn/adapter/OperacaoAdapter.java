package com.example.learn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;
import com.example.learn.models.OperacaoTempo;

import java.util.List;

public class OperacaoAdapter extends RecyclerView.Adapter<OperacaoAdapter.ViewHolder> {
    private List<OperacaoTempo> operacoes;
    private Context context;

    public OperacaoAdapter(Context context, List<OperacaoTempo> operacoes) {
        this.context = context;
        this.operacoes = operacoes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_operacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OperacaoTempo operacao = operacoes.get(position);
        holder.txtOperacao.setText("Operação ID: " + operacao.getOperacaoId());
        holder.txtTempo.setText("Tempo: " + operacao.getTempoSegundos() + "s");
    }

    @Override
    public int getItemCount() {
        return operacoes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtOperacao, txtTempo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOperacao = itemView.findViewById(R.id.spinnerOperacao);
            txtTempo = itemView.findViewById(R.id.txtTempo);
        }
    }
}