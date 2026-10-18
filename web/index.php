<?

//data
include_once './dao/DaoFactory.php';

$tipo = $_GET['tipo'] ?? '';
$ingrediente = $_GET['ingrediente'] ?? '';

if ($tipo || $ingrediente) {
    $platos = DaoFactory::getDao()->getPlatosDao()->getByTipoAndIngrediente($tipo, $ingrediente);
} else {
    $platos = DaoFactory::getDao()->getPlatosDao()->getAll();
}

//interface
include 'components/nav.php';
?>

<form method="GET" action="index.php">
    <label for="tipo">Filtrar por tipo:</label>
    <select name="tipo">
        <option value="">Tipo de plato</option>
        <option value="primero">Primero</option>
        <option value="segundo">Segundo</option>
        <option value="postre">Postre</option>
    </select>
    <label for="precio">Filtrar por ingrediente:</label>
    <input type="text" name="ingrediente" placeholder="Ingrediente">
    <button type="submit">Filtrar</button>
</form>
<ul>
    <?
    foreach ($platos as $plato) {
        echo '<li>' . $plato->getNombre() . ' - ' . $plato->getPrecio() . '€ <br>' . '<a href="detalles.php?nombre=' . $plato->getNombre() . '"><button>Detalles</button></a><a href="editar.php?nombre=' . $plato->getNombre() . '"><button>Editar</button></a> <a href="controller/eliminar.php?nombre=' . $plato->getNombre() . '"><button>Eliminar</button></a></li>';
    }
    ?>
</ul>