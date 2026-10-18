<?php

include_once '../dao/DaoFactory.php';

$nombreOriginal = $_POST['nombre_original'] ?? null;
$nombre = $_POST['nombre'] ?? null;
$tipo = $_POST['tipo'] ?? null;
$precio = $_POST['precio'] ?? null;
$ingredientes = $_POST['ingredientes'] ?? [];

DaoFactory::getDao()->getPlatosDao()->updatePlato($nombreOriginal, $nombre, $tipo, $precio, $ingredientes);
header('Location: ../index.php');
