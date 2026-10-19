package com.cpifppiramide.restaurante.www;

import com.cpifppiramide.restaurante.dao.DaoFactory;
import com.cpifppiramide.restaurante.entities.Ingrediente;
import com.cpifppiramide.restaurante.entities.Plato;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class PlatosController {
    @GetMapping({"/", "/index"})
    public String index(
            @RequestParam(defaultValue = "") String tipo,
            @RequestParam(defaultValue = "") String ingrediente,
            Model model) {
        model.addAttribute("platos", DaoFactory.getInstance().getPlatosDao()
                .getByTipoAndIngrediente(tipo, ingrediente));
        model.addAttribute("tipo", tipo);
        model.addAttribute("ingrediente", ingrediente);
        return "index";
    }

    @GetMapping("/platos/{nombre}")
    public String detalles(@PathVariable String nombre, Model model) {
        model.addAttribute("plato", DaoFactory.getInstance().getPlatosDao().getPlatoConIngredientes(nombre));
        return "detalles";
    }

    @GetMapping("/platos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("ingredientes", DaoFactory.getInstance().getIngredientesDao().getAll());
        return "nuevo";
    }

    @PostMapping("/platos")
    public String guardar(
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam String tipo,
            @RequestParam(required = false) List<String> ingredientes) {
        List<Ingrediente> seleccionados = ingredientes == null
                ? List.of()
                : ingredientes.stream().map(Ingrediente::new).toList();
        DaoFactory.getInstance().getPlatosDao().save(new Plato(nombre, precio, tipo, seleccionados));
        return "redirect:/";
    }

    @GetMapping("/platos/{nombre}/editar")
    public String editar(@PathVariable String nombre, Model model) {
        Plato plato = DaoFactory.getInstance().getPlatosDao().getPlatoConIngredientes(nombre);
        if (plato == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado");
        }
        model.addAttribute("plato", plato);
        model.addAttribute("ingredientes", DaoFactory.getInstance().getIngredientesDao().getAll());
        model.addAttribute(
                "ingredientesSeleccionados",
                plato.getIngredientes().stream().map(Ingrediente::getNombre).toList());
        return "editar";
    }

    @PostMapping("/platos/actualizar")
    public String actualizar(
            @RequestParam("nombre_original") String nombreOriginal,
            @RequestParam String nombre,
            @RequestParam BigDecimal precio,
            @RequestParam String tipo,
            @RequestParam(required = false) List<String> ingredientes) {
        DaoFactory.getInstance().getPlatosDao().updatePlato(
                nombreOriginal,
                nombre,
                tipo,
                precio,
                ingredientes == null ? List.of() : ingredientes);
        return "redirect:/";
    }

    @PostMapping("/platos/{nombre}/eliminar")
    public String eliminar(@PathVariable String nombre) {
        DaoFactory.getInstance().getPlatosDao().delete(nombre);
        return "redirect:/";
    }
}
