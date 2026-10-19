<?

//data
include_once './dao/DaoFactory.php';
$ingredientes = DaoFactory::getDao()->getIngredientesDao()->getAll();

include_once './components/nav.php';
?>

<form action="controller/guardar.php" method="POST">
    <label for="nombre">Nombre:</label>
    <input type="text" name="nombre" required>
    <label for="tipo">Tipo:</label>
    <select name="tipo" required>
        <option value="">Seleccione un tipo</option>
        <option value="primero">Primero</option>
        <option value="segundo">Segundo</option>
        <option value="postre">Postre</option>
    </select>
    <label for="precio">Precio:</label>
    <input type="number" name="precio" step="0.01" required>
    <br>
    <label for="ingredientes">Ingredientes:</label><br>
    <?
    foreach ($ingredientes as $ingrediente) {
        echo '<input id="' . $ingrediente->getNombre() . '" type="checkbox" name="ingredientes[]" value="' . $ingrediente->getNombre() . '"> <label for="' . $ingrediente->getNombre() . '">' . $ingrediente->getNombre() . '</label><br>';
    }
    ?>
    <button type="submit">Guardar</button>
</form>