package com.example.foliadeiros.Api;

import com.example.foliadeiros.Model.Foliada;
import com.example.foliadeiros.Model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsuarioApiService {

    @GET("{id}/favoritas")
    Call<List<Foliada>> getAll(@Path("id") int usuarioId);

    @POST("create")
    Call<Usuario> createUser(@Body Usuario usuario);


}
