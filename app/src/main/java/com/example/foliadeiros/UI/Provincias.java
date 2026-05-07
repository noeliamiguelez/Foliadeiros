package com.example.foliadeiros.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foliadeiros.Adapter.ProvinciaAdapter;
import com.example.foliadeiros.Api.ProvinciasApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Model.Provincia;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Provincias extends AppCompatActivity {
    private ListView listView;
    private ProvinciaAdapter adapter;
    private List<Provincia> provincias= new ArrayList<>();
    private ProvinciasApiService api;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if (id==R.id.fav){
            Intent intent = new Intent(Provincias.this, Favoritas.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_provincias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        listView= findViewById(R.id.lista_provincias);
        api= RetrofitClient.getClient().create(ProvinciasApiService.class);
        cargarProvincias();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView.setOnItemClickListener((parent, view, position, id)->{
            Provincia p= provincias.get(position);

            Intent intent= new Intent(Provincias.this, listadoFoliadas.class);
            intent.putExtra("provincia_id", p.getId());
            intent.putExtra("provincia_nombre", p.getNombre());
            startActivity(intent);
        });
    }




    private void cargarProvincias(){
        api.getProvincias().enqueue(new Callback<List<Provincia>>() {
            @Override
            public void onResponse(Call<List<Provincia>> call, Response<List<Provincia>> response) {

                if (response.isSuccessful()&&response.body()!=null){
                    provincias= response.body();

                    adapter= new ProvinciaAdapter(Provincias.this, provincias);
                    listView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Provincia>> call, Throwable t) {
                Toast.makeText(Provincias.this, "Error al cargar provincias", Toast.LENGTH_SHORT).show();

            }
        });
    }
}