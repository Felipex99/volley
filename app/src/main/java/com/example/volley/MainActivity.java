package com.example.volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    private AppCompatEditText text;
    private AppCompatButton enviar;
    private ListView lista;
    private RequestQueue queue;
    private String url;
    private StringRequest stringRequest;
    private ImageView webimage, wifi;
    private ImageRequest imageRequest;
    private AppCompatTextView info;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        criarCampos();
        //INSTANCIAMOS UMA FILA DE REQUISICAO

    }

    public void criarCampos(){
        text = findViewById(R.id.text);
        enviar = findViewById(R.id.enviar);
        info = findViewById(R.id.info);
        webimage = findViewById(R.id.webimage);
        wifi = findViewById(R.id.wifi);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = text.getText().toString();
                RequestQueue queue1 = Volley.newRequestQueue(MainActivity.this);
                int l = 500;
                int a = 500;
                ImageRequest imageRequest1 = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        webimage.setImageBitmap(response);
                    }
                }, l, a, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "DEU BO: "+error.getCause(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue = Volley.newRequestQueue(MainActivity.this);
                //REQUISITA UMA RESPOSTA DE TEXTO DE UMA URL ESPECIFICADA
                if(conectado(MainActivity.this)){
                    Toast.makeText(MainActivity.this, "Conectado", Toast.LENGTH_SHORT).show();
                    wifi.setBackgroundColor(Color.GREEN);
                }else{
                    wifi.setBackgroundColor(Color.RED);
                    Toast.makeText(MainActivity.this, "Ligue sua INTERNET", Toast.LENGTH_SHORT).show();
                }

                queue.add(imageRequest1);

            }
        });
    }
    public boolean conectado(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            if(connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting() == true){
                return true;
            }
        }catch(Exception e){
            return false;
        }

        return false;
    }
    public void cor(){
        if(conectado(MainActivity.this)){
            wifi.setBackgroundColor(Color.GREEN);
        }else{
            wifi.setBackgroundColor(Color.RED);
        }
    }

//                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        info.setText(response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        info.setText("ISSO NÃO FUNCIONOU");
//                    }
//                });
}