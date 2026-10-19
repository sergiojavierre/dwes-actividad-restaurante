package com.cpifppiramide.restaurante.dao.ingredientes;

import com.cpifppiramide.restaurante.dao.DBConnector;
import com.cpifppiramide.restaurante.entities.Ingrediente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOIngredientesMySQL implements DAOIngredientes {
    @Override
    public List<Ingrediente> getAll() {
        List<Ingrediente> ingredientes = new ArrayList<>();
        try (PreparedStatement statement = DBConnector.getInstance()
                .prepareStatement("SELECT nombre FROM ingredientes");
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ingredientes.add(new Ingrediente(resultSet.getString("nombre")));
            }
            return ingredientes;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron obtener los ingredientes", e);
        }
    }
}
