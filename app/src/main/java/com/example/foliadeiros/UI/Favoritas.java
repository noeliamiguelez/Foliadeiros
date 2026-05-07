package com.example.foliadeiros.UI;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foliadeiros.Adapter.FoliadaAdapter;
import com.example.foliadeiros.Api.FoliadaApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Api.UsuarioApiService;
import com.example.foliadeiros.Model.Foliada;
import com.example.foliadeiros.Model.Usuario;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favoritas extends AppCompatActivity {
    int usuarioId;
    private ListView listView;
    private FoliadaAdapter adapter;
    private List<Foliada> foliadas= new ArrayList<>();
    private UsuarioApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton bt_atras= (ImageButton) findViewById(R.id.bt_atras);
        bt_atras.setOnClickListener(view -> {
            finish();
        });

        TextView titulo = (TextView) findViewById(R.id.titulo);

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.lista_foliadas);
        titulo.setText("Favoritas");
        adapter= new FoliadaAdapter(this, foliadas);
        listView.setAdapter(adapter);

        api= RetrofitClient.getClient().create(UsuarioApiService.class);

        usuarioId= getIntent().getIntExtra("usuarioId", -1);

        cargarFavoritas();
    }

    private void cargarFavoritas() {
        api.getAll(usuarioId).enqueue(new Callback<List<Foliada>>() {
            @Override
            public void onResponse(Call<List<Foliada>> call, Response<List<Foliada>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    foliadas.clear();
                    foliadas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(Favoritas.this, "No hay foliadas favoritas", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Foliada>> call, Throwable t) {
                Toast.makeText(Favoritas.this, "Error cargando favoritas", Toast.LENGTH_SHORT).show();
            }

        });
    }

}