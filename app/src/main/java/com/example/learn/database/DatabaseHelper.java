package com.example.learn.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.learn.models.Classificacao;
import com.example.learn.models.Maquina;
import com.example.learn.models.MaquinaB;
import com.example.learn.models.Operacao;
import com.example.learn.models.PostoTrabalho;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "myDatabase.db";
    private static final int DATABASE_VERSION = 2; // Atualize para recriar o banco

    // Tabelas e colunas corrigidas
    private static final String TABELA_MAQUINA = " maquina ";
    private static final String ID_MAQUINA = " _id ";
    private static final String NOME_MAQUINA = " nome ";
    private static final String DESC_MAQUINA = " descricao ";

    private static final String TABELA_POSTO = " posto ";
    private static final String ID_POSTO = " _id ";
    private static final String NOME_POSTO = " nome ";
    private static final String DESC_POSTO = " descricao ";

    private static final String TABELA_OPERACAO = " operacao ";
    private static final String ID_OPERACAO = " _id ";
    private static final String NOME_OPERACAO = " nome ";
    private static final String DESC_OPERACAO = " descricao ";
    private static final String FK_CLASSIFICACAO = " classificacao_id "; // Corrigido

    private static final String TABELA_ATIVIDADE = " atividade ";
    private static final String ID_ATIVIDADE = " _id ";
    private static final String NOME_ATIVIDADE = " nome ";
    private static final String OBS_ATIVIDADE = " observacao ";
    private static final String DATAI_ATIVIDADE = " data_inicio ";
    private static final String DATAF_ATIVIDADE = " data_fim ";
    private static final String FK_POSTO = " posto_id ";
    private static final String FK_MAQUINA = " maquina_id ";

    private static final String TABELA_TOP = " tempo_operacao ";
    private static final String ID_TOP = " _id ";
    private static final String TEMPO_TOP = " tempo ";
    private static final String FK_OP = " op_id ";
    private static final String FK_ATIVIDADE = " atividade_id "; // Corrigido

    private static final String TABELA_CLASSIFICACAO = " classificacao ";
    private static final String ID_CLASSIFICACAO = " _id ";
    private static final String NOME_CLASSIFICACAO = " nome ";

    private static final String TABELA_MAQOP = " maquina_operacoes ";
    private static final String FK_MAQ = " id_maq ";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criando tabelas corrigidas
        String query1 = "CREATE TABLE " + TABELA_MAQUINA + " (" +
                ID_MAQUINA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME_MAQUINA + " TEXT, " +
                DESC_MAQUINA + " TEXT);";
        db.execSQL(query1);

        String query2 = "CREATE TABLE " + TABELA_POSTO + " (" +
                ID_POSTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME_POSTO + " TEXT, " +
                DESC_POSTO + " TEXT);";
        db.execSQL(query2);

        String query3 = "CREATE TABLE " + TABELA_CLASSIFICACAO + " (" +
                ID_CLASSIFICACAO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME_CLASSIFICACAO + " TEXT);";
        db.execSQL(query3);

        String query4 = "CREATE TABLE " + TABELA_OPERACAO + " (" +
                ID_OPERACAO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME_OPERACAO + " TEXT, " +
                DESC_OPERACAO + " TEXT, " +
                FK_CLASSIFICACAO + " INTEGER, " +
                "FOREIGN KEY (" + FK_CLASSIFICACAO + ") REFERENCES " + TABELA_CLASSIFICACAO + "(" + ID_CLASSIFICACAO + "));";
        db.execSQL(query4);

        String query5 = "CREATE TABLE " + TABELA_ATIVIDADE + " (" +
                ID_ATIVIDADE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOME_ATIVIDADE + " TEXT, " +
                OBS_ATIVIDADE + " TEXT, " +
                DATAI_ATIVIDADE + " TEXT, " + // Armazenando datas como string (ISO 8601)
                DATAF_ATIVIDADE + " TEXT, " +
                FK_POSTO + " INTEGER, " +
                FK_MAQUINA + " INTEGER, " +
                "FOREIGN KEY (" + FK_POSTO + ") REFERENCES " + TABELA_POSTO + "(" + ID_POSTO + "), " +
                "FOREIGN KEY (" + FK_MAQUINA + ") REFERENCES " + TABELA_MAQUINA + "(" + ID_MAQUINA + "));";
        db.execSQL(query5);

        String query6 = "CREATE TABLE " + TABELA_TOP + " (" +
                ID_TOP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FK_ATIVIDADE + " INTEGER, " +
                FK_OP + " INTEGER, " +
                TEMPO_TOP + " INTEGER, " +
                "FOREIGN KEY (" + FK_ATIVIDADE + ") REFERENCES " + TABELA_ATIVIDADE + "(" + ID_ATIVIDADE + "), " +
                "FOREIGN KEY (" + FK_OP + ") REFERENCES " + TABELA_OPERACAO + "(" + ID_OPERACAO + "));";
        db.execSQL(query6);

        String query7 = "CREATE TABLE " + TABELA_MAQOP + " (" +
                FK_OP + " INTEGER, " +
                FK_MAQ + " INTEGER, " +
                "FOREIGN KEY (" + FK_OP + ") REFERENCES " + TABELA_OPERACAO + "(" + ID_OPERACAO + "), " +
                "FOREIGN KEY (" + FK_MAQ + ") REFERENCES " + TABELA_MAQUINA + "(" + ID_MAQUINA + "));";
        db.execSQL(query7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MAQUINA);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_POSTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_CLASSIFICACAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_OPERACAO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_ATIVIDADE);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_TOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_MAQOP);
    }

    public void clearTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  " + TABELA_MAQUINA);
        db.execSQL("DELETE FROM  " + TABELA_POSTO);
        db.execSQL("DELETE FROM  " + TABELA_CLASSIFICACAO);
        db.execSQL("DELETE FROM  " + TABELA_OPERACAO);
        db.execSQL("DELETE FROM  " + TABELA_ATIVIDADE);
        db.execSQL("DELETE FROM  " + TABELA_TOP);
        db.execSQL("DELETE FROM  " + TABELA_MAQOP);

    }

    public void addMaqOp(int id_op, int id_maq) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FK_OP, id_op);
        cv.put(FK_MAQ, id_maq);
        long result = db.insert(TABELA_MAQOP, null, cv);
        if (result == 1) {
        } else {
        }
    }

    public void addMaquina(String nome, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOME_MAQUINA, nome);
        cv.put(DESC_MAQUINA, desc);
        long result = db.insert(TABELA_MAQUINA, null, cv);
        if (result == 1) {
        } else {
        }
    }




    public void addClassificacao(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOME_CLASSIFICACAO, nome);
        long result = db.insert(TABELA_CLASSIFICACAO, null, cv);
        if (result == 1) {
        } else {
        }
    }

    public void addPosto(String nome, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOME_POSTO, nome);
        cv.put(DESC_POSTO, desc);
        long result = db.insert(TABELA_POSTO, null, cv);
        if (result == 1) {
        } else {
        }
    }
    public void inserirPosto(PostoTrabalho posto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_POSTO, posto.getId());
        values.put(NOME_POSTO, posto.getNome());
        values.put(DESC_POSTO, posto.getDescricao());

        db.insertWithOnConflict(TABELA_POSTO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public void inserirClassificacao(Classificacao classificacao) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_CLASSIFICACAO, classificacao.getId());
        values.put(NOME_CLASSIFICACAO, classificacao.getNome());
        ;
        db.insertWithOnConflict(TABELA_CLASSIFICACAO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }


    public void addAtividade(String nome, String observacao, String dataInicio, String dataFim, int postoId, int maquinaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOME_ATIVIDADE, nome);
        cv.put(OBS_ATIVIDADE, observacao);
        cv.put(DATAI_ATIVIDADE, dataInicio);
        cv.put(DATAF_ATIVIDADE, dataFim);
        cv.put(FK_POSTO, postoId);
        cv.put(FK_MAQUINA, maquinaId);
        long result = db.insert(TABELA_ATIVIDADE, null, cv);
        if (result == -1) {
        } else {
        }
    }

    public void addTempoOperacao(int atividadeId, int operacaoId, int tempo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FK_ATIVIDADE, atividadeId);
        cv.put(FK_OP, operacaoId);
        cv.put(TEMPO_TOP, tempo);
        long result = db.insert(TABELA_TOP, null, cv);
        if (result == -1) {
        } else {
        }
    }

    public void inserirMaquina(MaquinaB maquina) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_MAQUINA, maquina.getId());
        values.put(NOME_MAQUINA, maquina.getNome());
        values.put(DESC_MAQUINA, maquina.getDescricao());

        db.insertWithOnConflict(TABELA_MAQUINA, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void inserirOperacoes(Operacao op) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_OPERACAO, op.getId());
        values.put(NOME_OPERACAO, op.getNome());
        values.put(ID_CLASSIFICACAO, op.getId_Class());

        db.insertWithOnConflict(TABELA_OPERACAO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public void inserirOperacoesSemId(String nome,String desc,int id_class ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESC_OPERACAO, desc);
        values.put(NOME_OPERACAO, nome);
        values.put(ID_CLASSIFICACAO, id_class);

        db.insertWithOnConflict(TABELA_OPERACAO, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    @SuppressLint("Range")
    public int maqId(String nome, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT _id FROM " + TABELA_MAQUINA + " WHERE nome = ? AND descricao = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nome, desc});

        int id = -1;  // Valor default caso não encontre nenhum registro
        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }

        if (cursor != null) {
            cursor.close();
        }

        return id;
    }

    @SuppressLint("Range")
    public Operacao opsPorId(int id_op) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT _id, nome, descricao, classificacao_id FROM " + TABELA_OPERACAO + " WHERE _id = ?";

        // Passar id_op como String no array
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_op)});

        Operacao operacao = null; // Valor default caso não encontre nenhum registro
        if (cursor != null && cursor.moveToFirst()) {
            // Aqui, você recupera as colunas corretas e cria o objeto Operacao
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
            int classificacaoId = cursor.getInt(cursor.getColumnIndex("classificacao_id"));

            // Cria o objeto Operacao com os dados obtidos
            operacao = new Operacao(id, nome, descricao, classificacaoId);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close(); // Sempre feche o banco de dados depois de usar

        return operacao; // Retorna o objeto Operacao
    }
    @SuppressLint("Range")
    public Operacao opsPorIds(int id_op) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT _id, nome  FROM " + TABELA_OPERACAO + " WHERE _id = ?";

        // Passar id_op como String no array
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id_op)});

        Operacao operacao = null; // Valor default caso não encontre nenhum registro
        if (cursor != null && cursor.moveToFirst()) {
            // Aqui, você recupera as colunas corretas e cria o objeto Operacao
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));

            // Cria o objeto Operacao com os dados obtidos
            operacao = new Operacao(id, nome);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close(); // Sempre feche o banco de dados depois de usar

        return operacao; // Retorna o objeto Operacao
    }

    public void addOp(String nome,String desc,int id_class ) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues cv = new ContentValues();
        cv.put(NOME_OPERACAO, nome);
        cv.put(DESC_OPERACAO, desc);
        cv.put(ID_CLASSIFICACAO, id_class);
        long result = db.insert(TABELA_OPERACAO, null, cv);
        if (result == 1) {
        } else {
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getOperacores() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OPERACAO + " ORDER BY _id;";
        ArrayList<String> dados = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            dados.add(cursor.getString(cursor.getColumnIndex("nome")));
        }
        cursor.close();
        db.close();
        return dados;
    }
    @SuppressLint("Range")
    public ArrayList<Operacao> getOperacoesMaq(int id_maq) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Operacao> operacoes = new ArrayList<>();

        String query = "SELECT  op_id  FROM " + TABELA_MAQOP + " WHERE " + FK_MAQ + " = ?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{String.valueOf(id_maq)});

            if (cursor != null && cursor.moveToFirst()) {  // Verifica se o cursor tem dados
                do {
                    int id_op = cursor.getInt(cursor.getColumnIndex("op_id"));
                    Operacao operacao = opsPorIds(id_op);
                    if (operacao != null) {
                        operacoes.add(operacao);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace(); // Registra qualquer erro no log
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return operacoes;
    }

    @SuppressLint("Range")
    public ArrayList<Integer> getOperacoresIds() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_OPERACAO + " ORDER BY _id;";
        ArrayList<Integer> dados = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            dados.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
        db.close();
        return dados;
    }

    @SuppressLint("Range")
    public ArrayList<Maquina> getMaquinas() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_MAQUINA + " ORDER BY _id;";
        ArrayList<Maquina> maquinas = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            maquinas.add(new Maquina(id, nome));
        }
        cursor.close();
        db.close();
        return maquinas;
    }

    @SuppressLint("Range")
    public ArrayList<Integer> getMaquinasIds() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_MAQUINA + " ORDER BY _id;";
        ArrayList<Integer> dados = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            dados.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
        db.close();
        return dados;
    }

    @SuppressLint("Range")
    public ArrayList<PostoTrabalho> getPosto() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_POSTO + " ORDER BY _id;";
        ArrayList<PostoTrabalho> postos = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            postos.add(new PostoTrabalho(id, nome));
        }
        cursor.close();
        db.close();
        return postos;
    }

    @SuppressLint("Range")
    public ArrayList<Integer> getPostoIds() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_POSTO + " ORDER BY _id;";
        ArrayList<Integer> dados = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            dados.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
        db.close();
        return dados;
    }

    @SuppressLint("Range")
    public ArrayList<Classificacao> getClassificacao() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_CLASSIFICACAO + " ORDER BY _id;";
        ArrayList<Classificacao> classificacoes = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            classificacoes.add(new Classificacao(id, nome));
        }
        cursor.close();
        db.close();
        return classificacoes;
    }
    @SuppressLint("Range")
    public ArrayList<Maquina> getMaqId() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABELA_MAQUINA + " ORDER BY _id;";
        ArrayList<Maquina>  maq = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String nome = cursor.getString(cursor.getColumnIndex("nome"));
            maq.add(new Maquina(id, nome));
        }
        cursor.close();
        db.close();
        return maq;
    }
}