<?

class Plato
{
    private $nombre;
    private $precio;
    private $tipo;
    private $ingredientes;

    public function __construct($nombre, $precio, $tipo, $ingredientes = [])
    {
        $this->nombre = $nombre;
        $this->precio = $precio;
        $this->tipo = $tipo;
        $this->ingredientes = $ingredientes;
    }

    public function getNombre()
    {
        return $this->nombre;
    }

    public function getPrecio()
    {
        return $this->precio;
    }

    public function getTipo()
    {
        return $this->tipo;
    }

    public function getIngredientes()
    {
        return $this->ingredientes;
    }
}
