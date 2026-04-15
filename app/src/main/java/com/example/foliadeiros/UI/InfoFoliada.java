package com.example.foliadeiros.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.foliadeiros.Adapter.FoliadaAdapter;
import com.example.foliadeiros.Api.FoliadaApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Model.Foliada;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFoliada extends AppCompatActivity {
    int foliadaId;
    private ListView listView;
    private FoliadaAdapter adapter;
    private FoliadaApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_foliada);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        api = RetrofitClient.getClient().create(FoliadaApiService.class);

        foliadaId= getIntent().getIntExtra("foliada_id", -1);
        TextView titulo= (TextView) findViewById(R.id.titulo);
        ImageView img_cartel= (ImageView) findViewById(R.id.cartel);
        TextView txt_fecha= (TextView) findViewById(R.id.fecha);
        TextView txt_hora= (TextView) findViewById(R.id.hora);
        TextView txt_grupo= (TextView) findViewById(R.id.grupos);
        TextView txt_descripcion= (TextView) findViewById(R.id.descripcion);

        api.getById(foliadaId).enqueue(new Callback<Foliada>() {
            @Override
            public void onResponse(Call<Foliada> call, Response<Foliada> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    Foliada foliada= response.body();
                    titulo.setText(foliada.getNombre());
                    if(foliada.getImaxe()!=null && !foliada.getImaxe().isEmpty()){
                        Glide.with(InfoFoliada.this).load(foliada.getImaxe()).into(img_cartel);
                    }else{
                        img_cartel.setImageResource(R.drawable.image_not_found);
                    }
                    if (foliada.getFecha()!=null && foliada.getFecha().isEmpty()){
                        txt_fecha.setText(foliada.getFecha());
                    }else{
                        txt_fecha.setText(" ");
                    }if (foliada.getHora()!=null&& foliada.getHora().isEmpty()){
                        txt_hora.setText(foliada.getHora());
                    }else{
                        txt_hora.setText(" ");
                    }
                    if (foliada.getDescripcion()!=null&&foliada.getDescripcion().isEmpty()){
                        txt_descripcion.setText(foliada.getDescripcion());
                    }else{
                        txt_descripcion.setText(" ");
                    }
                }
            }

            @Override
            public void onFailure(Call<Foliada> call, Throwable t) {
                Log.e("API ERROR", "Mensaje: " + t.getMessage(), t);
                Toast.makeText(InfoFoliada.this, "Error al cargar foliada", Toast.LENGTH_SHORT).show();

            }
        });

    }
}