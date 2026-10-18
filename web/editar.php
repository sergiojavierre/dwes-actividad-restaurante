<?

//data

include_once './dao/DaoFactory.php';

$nombre = $_GET['nombre'] ?? null;
$platoConIngredientes = DaoFactory::getDao()->getPlatosDao()->getPlatoConIngredientes($nombre);
$ingredientes = DaoFactory::getDao()->getIngredientesDao()->getAll();

//vista

include_once './components/nav.php';

?>
<h1>Editar Plato</h1>
<form action="controller/actualizar.php" method="POST">
    <input type="hidden" name="nombre_original" value="<?= $platoConIngredientes->getNombre() ?>">
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" value="<?= $platoConIngredientes->getNombre() ?>" required>
    <label for="tipo">Tipo:</label>
    <select name="tipo" required>
        <option value="">Seleccione un tipo</option>
        <option value="primero" <?= $platoConIngredientes->getTipo() === 'primero' ? 'selected' : '' ?>>Primero</option>
        <option value="segundo" <?= $platoConIngredientes->getTipo() === 'segundo' ? 'selected' : '' ?>>Segundo</option>
        <option value="postre" <?= $platoConIngredientes->getTipo() === 'postre' ? 'selected' : '' ?>>Postre</option>
    </select>
    <label for="precio">Precio:</label>
    <input type="number" name="precio" step="0.01" value="<?= $platoConIngredientes->getPrecio() ?>" required>
    <br>
    <label for="ingredientes">Ingredientes:</label><br>
    <?
    $ingredientesSeleccionados = [];
    foreach ($platoConIngredientes->getIngredientes() as $ingredienteSeleccionado) {
        $ingredientesSeleccionados[] = $ingredienteSeleccionado->getNombre();
    }

    foreach ($ingredientes as $ingrediente) {
        $checked = in_array($ingrediente->getNombre(), $ingredientesSeleccionados) ? 'checked' : '';
        echo '<input id="' . $ingrediente->getNombre() . '" type="checkbox" name="ingredientes[]" value="' . $ingrediente->getNombre() . '" ' . $checked . '> <label for="' . $ingrediente->getNombre() . '">' . $ingrediente->getNombre() . '</label><br>';
    }
    ?>
    <button type="submit">Guardar</button>