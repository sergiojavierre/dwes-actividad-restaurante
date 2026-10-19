package com.cpifppiramide.restaurante.dao;

import com.cpifppiramide.restaurante.dao.ingredientes.DAOIngredientes;
import com.cpifppiramide.restaurante.dao.ingredientes.DAOIngredientesMySQL;
import com.cpifppiramide.restaurante.dao.platos.DAOPlatos;
import com.cpifppiramide.restaurante.dao.platos.DAOPlatosMySQL;

public class DaoFactory {
    private static DaoFactory instance;

    private DAOPlatos platosDao;
    private DAOIngredientes ingredientesDao;

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public DAOPlatos getPlatosDao() {
        if (platosDao == null) {
            platosDao = new DAOPlatosMySQL();
        }
        return platosDao;
    }

    public DAOIngredientes getIngredientesDao() {
        if (ingredientesDao == null) {
            ingredientesDao = new DAOIngredientesMySQL();
        }
        return ingredientesDao;
    }
}
