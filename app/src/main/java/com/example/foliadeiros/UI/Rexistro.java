package com.example.foliadeiros.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foliadeiros.Adapter.ProvinciaAdapter;
import com.example.foliadeiros.Api.ProvinciasApiService;
import com.example.foliadeiros.Api.RetrofitClient;
import com.example.foliadeiros.Api.UsuarioApiService;
import com.example.foliadeiros.Model.Provincia;
import com.example.foliadeiros.Model.Usuario;
import com.example.foliadeiros.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rexistro extends AppCompatActivity {
    private Usuario user;
    private UsuarioApiService api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rexistro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button bt_siConta= (Button) findViewById(R.id.bt_siConta);
        Button bt_registro= (Button) findViewById(R.id.bt_crearConta);

        bt_siConta.setOnClickListener(view -> {
            Intent i_siConta = new Intent(Rexistro.this, LogIn.class);
            startActivity(i_siConta);
            finish();
        });

        bt_registro.setOnClickListener(view -> {
            EditText usuario= (EditText) findViewById(R.id.nome_usuario);
            EditText mail= (EditText) findViewById(R.id.mail);
            EditText contra= (EditText) findViewById(R.id.password);
            EditText contra2= (EditText) findViewById(R.id.password2);

            String nombre= usuario.getText().toString().trim();
            String email= mail.getText().toString().trim();
            String pas= contra.getText().toString().trim();
            String pas2= contra2.getText().toString().trim();


            if (!pas.equals(pas2)){
                Toast.makeText(this, "As contrasinais non coindicen", Toast.LENGTH_SHORT).show();
                return;
            }else{

                api= RetrofitClient.getClient().create(UsuarioApiService.class);
                api.createUser(new Usuario(nombre, email, pas)).enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(Rexistro.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(Rexistro.this, Provincias.class);
                            startActivity(intent);
                            finish();
                        }else if(response.code()==409){
                            mail.setError("El mail ya existe");
                            mail.requestFocus();
                        }else{
                            Toast.makeText(Rexistro.this, "Error ao rexistrar", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Toast.makeText(Rexistro.this, "Error de conexión", Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
    }
}