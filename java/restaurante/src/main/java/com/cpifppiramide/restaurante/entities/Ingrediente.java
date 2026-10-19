package com.cpifppiramide.restaurante.entities;

public class Ingrediente {
    private String nombre;

    public Ingrediente() {
    }

    public Ingrediente(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
