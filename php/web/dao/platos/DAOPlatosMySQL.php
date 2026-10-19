<?

require_once __DIR__ . '/DAOPlatos.php';
require_once __DIR__ . '/../../entities/Plato.php';

class DAOPlatosMySQL implements DAOPlatos
{
    private $conn;

    public function __construct($conn)
    {
        $this->conn = $conn;
    }

    public function save($plato)
    {
        $stmt = $this->conn->prepare(
            'INSERT INTO platos (nombre, precio, tipo) VALUES (:nombre, :precio, :tipo)'
        );

        $stmt->execute([
            'nombre' => $plato->getNombre(),
            'precio' => $plato->getPrecio(),
            'tipo' => $plato->getTipo(),
        ]);

        foreach ($plato->getIngredientes() as $ingrediente) {
            $stmt = $this->conn->prepare(
                'INSERT INTO platos_ingredientes (plato, ingrediente) VALUES (:plato, :ingrediente)'
            );
            $stmt->execute([
                'plato' => $plato->getNombre(),
                'ingrediente' => $ingrediente->getNombre()
            ]);
        }
    }

    public function getAll()
    {
        $stmt = $this->conn->query(
            'SELECT p.nombre, p.precio, p.tipo
            FROM platos p'
        );

        $platos = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            array_push($platos, new Plato(
                $row['nombre'],
                $row['precio'],
                $row['tipo'],
                []
            ));
        }

        return $platos;
    }

    public function getByTipoAndIngrediente($tipo, $ingrediente)
    {
        $query = 'SELECT p.nombre, p.precio, p.tipo
                  FROM platos p
                  JOIN platos_ingredientes pi ON p.nombre = pi.plato
                  JOIN ingredientes i ON pi.ingrediente = i.nombre
                  WHERE 1=1';

        $params = [];

        if ($tipo) {
            $query .= ' AND p.tipo = :tipo';
            $params['tipo'] = $tipo;
        }

        if ($ingrediente) {
            $query .= ' AND i.nombre = :ingrediente';
            $params['ingrediente'] = $ingrediente;
        }

        $stmt = $this->conn->prepare($query);
        $stmt->execute($params);

        $platos = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $existe = false;
            foreach ($platos as $plato) {
                if ($plato->getNombre() === $row['nombre']) {
                    $existe = true;
                    break;
                }
            }

            if (!$existe) {
                array_push($platos, new Plato(
                    $row['nombre'],
                    $row['precio'],
                    $row['tipo'],
                    []
                ));
            }
        }

        return $platos;
    }

    public function getPlatoConIngredientes($nombre)
    {
        $stmt = $this->conn->prepare(
            'SELECT p.nombre, p.precio, p.tipo, i.nombre AS ingrediente
            FROM platos p
            LEFT JOIN platos_ingredientes pi ON p.nombre = pi.plato
            LEFT JOIN ingredientes i ON pi.ingrediente = i.nombre
            WHERE p.nombre = :nombre'
        );

        $stmt->execute(['nombre' => $nombre]);

        $plato = null;
        $ingredientes = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            if (!$plato) {
                $plato = new Plato(
                    $row['nombre'],
                    $row['precio'],
                    $row['tipo'],
                    []
                );
            }
            if ($row['ingrediente']) {
                array_push($ingredientes, new Ingrediente($row['ingrediente']));
            }
        }

        if ($plato) {
            return new Plato(
                $plato->getNombre(),
                $plato->getPrecio(),
                $plato->getTipo(),
                $ingredientes
            );
        }

        return null;
    }

    public function delete($nombre)
    {
        $stmt = $this->conn->prepare(
            'DELETE FROM platos WHERE nombre = :nombre'
        );

        return $stmt->execute(['nombre' => $nombre]);
    }

    public function updatePlato($nombreOriginal, $nombre, $tipo, $precio, $ingredientes)
    {
        $this->conn->prepare('DELETE FROM platos_ingredientes WHERE plato = :plato')->execute(['plato' => $nombreOriginal]);

        $stmt = $this->conn->prepare(
            'UPDATE platos SET nombre = :nombre, tipo = :tipo, precio = :precio WHERE nombre = :nombreOriginal'
        );
        $stmt->execute([
            'nombre' => $nombre,
            'tipo' => $tipo,
            'precio' => $precio,
            'nombreOriginal' => $nombreOriginal
        ]);

        foreach ($ingredientes as $ingrediente) {
            $this->conn->prepare('INSERT INTO platos_ingredientes (plato, ingrediente) VALUES (:plato, :ingrediente)')->execute([
                'plato' => $nombre,
                'ingrediente' => $ingrediente
            ]);
        }
    }
}
