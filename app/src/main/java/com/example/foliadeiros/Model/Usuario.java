package com.example.foliadeiros.Model;

import java.util.HashSet;
import java.util.Set;

public class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String password;

    private Set<Foliada> favoritas= new HashSet<>();

    public Usuario() {
    }

    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Foliada> getFavoritas() {
        return favoritas;
    }

    public void setFavoritas(Set<Foliada> favoritas) {
        this.favoritas = favoritas;
    }
}
