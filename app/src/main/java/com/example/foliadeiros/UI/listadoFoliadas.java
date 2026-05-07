package com.example.foliadeiros.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foliadeiros.Adapter.FoliadaAdapter;
import com.example.foliadeiros.Adapter.ProvinciaAdapter;
import com.example.foliadeiros.Api.FoliadaApiService;
import com.example.foliadeiros.Api.ProvinciasApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Model.Foliada;
import com.example.foliadeiros.Model.Provincia;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class listadoFoliadas extends AppCompatActivity {
    int provinciaId;
    String nombreProvincia;
    private ListView listView;
    private FoliadaAdapter adapter;
    private List<Foliada> foliadas= new ArrayList<>();
    private FoliadaApiService api;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if (id==R.id.fav){
            Intent intent = new Intent(listadoFoliadas.this, Favoritas.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listado_foliadas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView titulo= (TextView) findViewById(R.id.titulo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView=findViewById(R.id.lista_foliadas);

        provinciaId= getIntent().getIntExtra("provincia_id", -1);
        nombreProvincia= getIntent().getStringExtra("provincia_nombre");
        titulo.setText(nombreProvincia);

        api= RetrofitClient.getClient().create(FoliadaApiService.class);
        cargarFoliadas();

        ImageButton bt_atras= (ImageButton) findViewById(R.id.bt_atras);
        bt_atras.setOnClickListener(view -> {
            finish();
        });

        listView.setOnItemClickListener((parent, view, position, id)->{
            Foliada f= foliadas.get(position);
            Intent intent= new Intent(listadoFoliadas.this, InfoFoliada.class);
            intent.putExtra("foliada_id", f.getId());
            startActivity(intent);
        });
    }

    private void cargarFoliadas(){
        api.getByProvincia(provinciaId).enqueue(new Callback<List<Foliada>>() {
            @Override
            public void onResponse(Call<List<Foliada>> call, Response<List<Foliada>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    foliadas= response.body();

                    adapter= new FoliadaAdapter(listadoFoliadas.this, foliadas);
                    listView= findViewById(R.id.lista_foliadas);
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Foliada>> call, Throwable t) {
                Toast.makeText(listadoFoliadas.this, "Error cargando foliadas", Toast.LENGTH_SHORT).show();

            }
        });
    }

}