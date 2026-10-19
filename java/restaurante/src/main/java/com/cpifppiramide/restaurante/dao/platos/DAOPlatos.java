package com.cpifppiramide.restaurante.dao.platos;

import com.cpifppiramide.restaurante.entities.Plato;
import java.util.List;

public interface DAOPlatos {
    void save(Plato plato);

    List<Plato> getAll();

    List<Plato> getByTipoAndIngrediente(String tipo, String ingrediente);

    Plato getPlatoConIngredientes(String nombre);

    void updatePlato(String nombreOriginal, String nombre, String tipo, java.math.BigDecimal precio,
            List<String> ingredientes);

    boolean delete(String nombre);
}
