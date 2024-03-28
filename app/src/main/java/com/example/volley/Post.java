package com.example.volley;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

public class Post extends AppCompatActivity {
    AppCompatEditText nome, email;
    String nomeS, emailS;
    ListView lista;
    AppCompatButton salvar, apagar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);
        criarCampos();
        listarDados();
    }
    public void criarCampos(){
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        salvar = findViewById(R.id.salvar);
        lista = findViewById(R.id.lista);
        apagar = findViewById(R.id.apagar);
        apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Post.this, "BANCO LOCAL APAGADO", Toast.LENGTH_SHORT).show();
                SQLiteDatabase banco = openOrCreateDatabase("banco", MODE_PRIVATE, null);
                banco.execSQL("DROP TABLE IF EXISTS usuario");
                criarTabela();
                banco.close();
                listarDados();
            }
        });
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeS = nome.getText().toString();
                emailS = email.getText().toString()+"@gmail.com";
                salvarDados(nomeS, emailS);
                enviarAoBanco(nomeS, emailS);
            }
        });
    }
    public void salvarDados(String nome, String email){
        try{
            SQLiteDatabase banco = openOrCreateDatabase("banco", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS usuario(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " nome VARCHAR," +
                    "email VARCHAR)");
            String query = "INSERT INTO usuario(" +
                    "nome, " +
                    "email) VALUES(?, ?)";
            SQLiteStatement stmt = banco.compileStatement(query);
            stmt.bindString(1, nome);
            stmt.bindString(2, email);
            stmt.executeInsert();
            banco.close();
            listarDados();
        }catch(Exception e){
            Toast.makeText(Post.this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }
    public void listarDados(){
        try{
            SQLiteDatabase banco = openOrCreateDatabase("banco", MODE_PRIVATE, null);
            Cursor cursor = banco.rawQuery("SELECT email FROM usuario",null);
            cursor.moveToFirst();
            ArrayList<String> array = new ArrayList<>();
            int i = 0;
            while(cursor.getCount()!=0 && !(cursor.isAfterLast())){
                i++;
                array.add(cursor.getString(0));
                cursor.moveToNext();
            }
            lista.setAdapter(new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    array
            ));
            cursor.close();
            banco.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void criarTabela(){
        try {
            SQLiteDatabase banco = openOrCreateDatabase("banco", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS usuario(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " nome VARCHAR," +
                    "email VARCHAR)");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void enviarAoBanco(String nome, String email){
        String url = "seu host";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Post.this, "SUCCESS: "+nome+email+" RESPONSE: "+response, Toast.LENGTH_SHORT).show();
                        Log.e("SECESSO", "RESPONSE: "+response.toString());
                    }
                },
        new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CONNECTION ERROR: ", "erro: "+error.getMessage());
                Toast.makeText(Post.this, "ERROR: "+ error, Toast.LENGTH_SHORT).show();
            }
        }){
            protected HashMap<String, String> getParamas() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("nome", nome);
                map.put("email",email);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
