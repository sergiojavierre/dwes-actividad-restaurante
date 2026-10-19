<?

include_once __DIR__ . '../../../entities/Ingrediente.php';

class DAOIngredientesMySQL implements DAOIngredientes
{
    private $pdo;

    public function __construct($pdo)
    {
        $this->pdo = $pdo;
    }

    public function getAll()
    {
        $stmt = $this->pdo->query('SELECT * FROM ingredientes');
        $ingredientes = [];
        while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
            $ingredientes[] = new Ingrediente($row['nombre']);
        }
        return $ingredientes;
    }
}
