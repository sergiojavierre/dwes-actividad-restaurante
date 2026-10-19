package com.cpifppiramide.restaurante.dao.platos;

import com.cpifppiramide.restaurante.dao.DBConnector;
import com.cpifppiramide.restaurante.entities.Ingrediente;
import com.cpifppiramide.restaurante.entities.Plato;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPlatosMySQL implements DAOPlatos {
    @Override
    public void save(Plato plato) {
        Connection connection = DBConnector.getInstance();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO platos (nombre, precio, tipo) VALUES (?, ?, ?)")) {
                statement.setString(1, plato.getNombre());
                statement.setBigDecimal(2, plato.getPrecio());
                statement.setString(3, plato.getTipo());
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO platos_ingredientes (plato, ingrediente) VALUES (?, ?)")) {
                for (Ingrediente ingrediente : plato.getIngredientes()) {
                    statement.setString(1, plato.getNombre());
                    statement.setString(2, ingrediente.getNombre());
                    statement.addBatch();
                }
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection, e);
            throw new IllegalStateException("No se pudo guardar el plato", e);
        } finally {
            restoreAutoCommit(connection);
        }
    }

    @Override
    public List<Plato> getAll() {
        List<Plato> platos = new ArrayList<>();
        try (PreparedStatement statement = DBConnector.getInstance()
                .prepareStatement("SELECT nombre, precio, tipo FROM platos");
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                platos.add(mapPlato(resultSet));
            }
            return platos;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron obtener los platos", e);
        }
    }

    @Override
    public List<Plato> getByTipoAndIngrediente(String tipo, String ingrediente) {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT p.nombre, p.precio, p.tipo FROM platos p");
        List<String> parameters = new ArrayList<>();

        if (ingrediente != null && !ingrediente.isBlank()) {
            sql.append(" JOIN platos_ingredientes pi ON p.nombre = pi.plato");
            sql.append(" JOIN ingredientes i ON pi.ingrediente = i.nombre");
        }
        sql.append(" WHERE 1 = 1");
        if (tipo != null && !tipo.isBlank()) {
            sql.append(" AND p.tipo = ?");
            parameters.add(tipo);
        }
        if (ingrediente != null && !ingrediente.isBlank()) {
            sql.append(" AND i.nombre = ?");
            parameters.add(ingrediente);
        }

        List<Plato> platos = new ArrayList<>();
        try (PreparedStatement statement = DBConnector.getInstance().prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setString(i + 1, parameters.get(i));
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    platos.add(mapPlato(resultSet));
                }
            }
            return platos;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudieron filtrar los platos", e);
        }
    }

    @Override
    public Plato getPlatoConIngredientes(String nombre) {
        String sql = """
                SELECT p.nombre, p.precio, p.tipo, i.nombre AS ingrediente
                FROM platos p
                LEFT JOIN platos_ingredientes pi ON p.nombre = pi.plato
                LEFT JOIN ingredientes i ON pi.ingrediente = i.nombre
                WHERE p.nombre = ?
                """;

        try (PreparedStatement statement = DBConnector.getInstance().prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                Plato plato = null;
                while (resultSet.next()) {
                    if (plato == null) {
                        plato = mapPlato(resultSet);
                    }
                    String nombreIngrediente = resultSet.getString("ingrediente");
                    if (nombreIngrediente != null) {
                        plato.getIngredientes().add(new Ingrediente(nombreIngrediente));
                    }
                }
                return plato;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo obtener el plato", e);
        }
    }

    @Override
    public boolean delete(String nombre) {
        try (PreparedStatement statement = DBConnector.getInstance()
                .prepareStatement("DELETE FROM platos WHERE nombre = ?")) {
            statement.setString(1, nombre);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo eliminar el plato", e);
        }
    }

    @Override
    public void updatePlato(String nombreOriginal, String nombre, String tipo, BigDecimal precio,
            List<String> ingredientes) {
        Connection connection = DBConnector.getInstance();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(
                    "DELETE FROM platos_ingredientes WHERE plato = ?");
                    PreparedStatement updateStatement = connection.prepareStatement(
                            "UPDATE platos SET nombre = ?, tipo = ?, precio = ? WHERE nombre = ?")) {
                deleteStatement.setString(1, nombreOriginal);
                deleteStatement.executeUpdate();
                updateStatement.setString(1, nombre);
                updateStatement.setString(2, tipo);
                updateStatement.setBigDecimal(3, precio);
                updateStatement.setString(4, nombreOriginal);
                updateStatement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO platos_ingredientes (plato, ingrediente) VALUES (?, ?)")) {
                for (String ingrediente : ingredientes) {
                    statement.setString(1, nombre);
                    statement.setString(2, ingrediente);
                    statement.addBatch();
                }
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            rollback(connection, e);
            throw new IllegalStateException("No se pudo actualizar el plato", e);
        } finally {
            restoreAutoCommit(connection);
        }
    }

    private Plato mapPlato(ResultSet resultSet) throws SQLException {
        return new Plato(
                resultSet.getString("nombre"),
                resultSet.getBigDecimal("precio"),
                resultSet.getString("tipo"));
    }

    private void rollback(Connection connection, SQLException exception) {
        try {
            connection.rollback();
        } catch (SQLException rollbackException) {
            exception.addSuppressed(rollbackException);
        }
    }

    private void restoreAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo restaurar el modo de autocommit", e);
        }
    }
}
