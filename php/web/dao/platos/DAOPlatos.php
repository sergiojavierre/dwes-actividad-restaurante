<?

interface DAOPlatos
{
    public function save($plato);

    public function getAll();
    public function getByTipoAndIngrediente($tipo, $ingrediente);
    public function getPlatoConIngredientes($nombre);
    public function updatePlato($nombreOriginal, $nombre, $tipo, $precio, $ingredientes);
    public function delete($nombre);
}
