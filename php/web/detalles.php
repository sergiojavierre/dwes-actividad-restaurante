<?

//data

include_once './dao/DaoFactory.php';

$nombre = $_GET['nombre'] ?? null;
$platoConIngredientes = DaoFactory::getDao()->getPlatosDao()->getPlatoConIngredientes($nombre);

//vista

include_once './components/nav.php';

?>
<h1><?php echo $platoConIngredientes ? $platoConIngredientes->getNombre() : 'Plato no encontrado'; ?></h1>
<p>Precio: <?php echo $platoConIngredientes ? $platoConIngredientes->getPrecio() : ''; ?></p>
<p>Tipo: <?php echo $platoConIngredientes ? $platoConIngredientes->getTipo() : ''; ?></p>
<p>Ingredientes:</p>
<ul>
    <?php
    if ($platoConIngredientes) {
        foreach ($platoConIngredientes->getIngredientes() as $ingrediente) {
            echo '<li>' . $ingrediente->getNombre() . '</li>';
        }
    }
    ?>
</ul>