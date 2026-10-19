package com.cpifppiramide.restaurante.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Plato {
    private String nombre;
    private BigDecimal precio;
    private String tipo;
    private List<Ingrediente> ingredientes;

    public Plato() {
        this.ingredientes = new ArrayList<>();
    }

    public Plato(String nombre, BigDecimal precio, String tipo) {
        this(nombre, precio, tipo, new ArrayList<>());
    }

    public Plato(String nombre, BigDecimal precio, String tipo, List<Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes == null ? new ArrayList<>() : new ArrayList<>(ingredientes);
    }
}
