<?

include_once '../dao/DaoFactory.php';

$nombre = $_POST['nombre'] ?? '';
$tipo = $_POST['tipo'] ?? '';
$precio = $_POST['precio'] ?? '';
$ingredientesData = $_POST['ingredientes'] ?? [];

$ingredientes = [];
foreach ($ingredientesData as $ingredienteData) {
    $ingredientes[] = new Ingrediente($ingredienteData);
}

$plato = new Plato($nombre, $precio, $tipo, $ingredientes);
DaoFactory::getDao()->getPlatosDao()->save($plato);
header('Location: ../index.php');
