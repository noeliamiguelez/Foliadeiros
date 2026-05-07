package com.example.foliadeiros.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.example.foliadeiros.Adapter.FoliadaAdapter;
import com.example.foliadeiros.Api.FoliadaApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Model.Foliada;
import com.example.foliadeiros.Model.Grupo;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFoliada extends AppCompatActivity {
    private Foliada foliada;
    int foliadaId;
    private ListView listView;
    private FoliadaAdapter adapter;
    private FoliadaApiService api;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if (id==R.id.fav){
            Intent intent = new Intent(InfoFoliada.this, Favoritas.class);
            startActivity(intent);
            return true;
        }else if (id==R.id.comp){
            compartirFoliada();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
        ImageButton bt_atras= (ImageButton) findViewById(R.id.bt_atras);
        bt_atras.setOnClickListener(view -> {
            finish();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = RetrofitClient.getClient().create(FoliadaApiService.class);

        foliadaId= getIntent().getIntExtra("foliada_id", -1);
        TextView titulo= (TextView) findViewById(R.id.titulo);
        ImageView img_cartel= (ImageView) findViewById(R.id.cartel);
        TextView txt_fecha= (TextView) findViewById(R.id.fecha);
        TextView txt_hora= (TextView) findViewById(R.id.hora);
        TextView txt_grupo= (TextView) findViewById(R.id.grupos);
        TextView txt_descripcion= (TextView) findViewById(R.id.descripcion);
        TextView txt_ubi= (TextView) findViewById(R.id.ubicacion);

        api.getById(foliadaId).enqueue(new Callback<Foliada>() {
            @Override
            public void onResponse(Call<Foliada> call, Response<Foliada> response) {
                if (response.isSuccessful()&&response.body()!=null){
                    foliada= response.body();
                    titulo.setText(foliada.getNombre());
                    if(foliada.getImaxe()!=null && !foliada.getImaxe().isEmpty()){
                        String nombre = foliada.getImaxe().replace("images", "");
                        String url = "http://10.0.2.2:8081/images/"+nombre;
                        Glide.with(InfoFoliada.this).load(url).into(img_cartel);
                    }else{
                        img_cartel.setImageResource(R.drawable.image_not_found);
                    }
                    if (foliada.getFecha()!=null && !foliada.getFecha().isEmpty()){
                        txt_fecha.setText(foliada.getFecha());
                    }else{
                        txt_fecha.setText(" ");
                    }
                    if (foliada.getGrupos()!=null && !foliada.getGrupos().isEmpty()){
                        StringBuilder nombres= new StringBuilder();
                        for (Grupo g: foliada.getGrupos()){
                            nombres.append(g.getNombre()).append("\n");
                        }
                        txt_grupo.setText(nombres.toString());
                    }else{
                        txt_grupo.setText(" ");
                    }
                    if (foliada.getHora()!=null&& !foliada.getHora().isEmpty()){
                        txt_hora.setText(foliada.getHora());
                    }else{
                        txt_hora.setText(" ");
                    }
                    if (foliada.getDescripcion()!=null&&!foliada.getDescripcion().isEmpty()){
                        txt_descripcion.setText(foliada.getDescripcion());
                    }else{
                        txt_descripcion.setText(" ");
                    }
                    if(foliada.getLugar()!= null && !foliada.getLugar().isEmpty()){
                        txt_ubi.setText(foliada.getLugar());
                    }else{
                        txt_ubi.setText(" ");
                    }
                }
            }

            @Override
            public void onFailure(Call<Foliada> call, Throwable t) {
                Toast.makeText(InfoFoliada.this, "Error al cargar foliada", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void compartirFoliada(){

        String texto= foliada.getNombre()+"\n\n"+
                foliada.getLugar()+"\n"+
                foliada.getFecha()+"\n";
                if(foliada.getHora()!=null){
                    texto+= foliada.getHora()+"\n\n";
                }

                texto+="Descubre mais en Foliadeir@S";

        Intent intent= new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);
        startActivity(Intent.createChooser(intent, "Compartir foliada"));

    }

}