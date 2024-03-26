package com.example.volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private AppCompatEditText text;
    private AppCompatButton enviar;
    private ListView lista;
    private AppCompatTextView info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarCampos();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.google.com";
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Dados enviados.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void criarCampos(){
        text = findViewById(R.id.text);
        enviar = findViewById(R.id.enviar);
        lista = findViewById(R.id.lista);
        info = findViewById(R.id.info);
    }

}