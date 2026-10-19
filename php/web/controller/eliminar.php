<?

include_once '../dao/DaoFactory.php';

$nombre = $_GET['nombre'] ?? null;

if ($nombre) {
    $daoPlatos = DaoFactory::getDao()->getPlatosDao();
    $daoPlatos->delete($nombre);
}

header('Location: ../index.php');
