<?

require_once __DIR__ . '/platos/DAOPlatos.php';
require_once __DIR__ . '/platos/DAOPlatosMySQL.php';
require_once __DIR__ . '/ingredientes/DAOIngredientes.php';
require_once __DIR__ . '/ingredientes/DAOIngredientesMySQL.php';

class DaoFactory
{
    private static $dao;
    private $conn;
    private $platosDao;
    private $ingredientesDao;

    public static function getDao()
    {
        if (!self::$dao) {
            self::$dao = new DaoFactory();
        }
        return self::$dao;
    }

    private function __construct()
    {
        try {
            $this->conn = new PDO('mysql:host=maria;dbname=restaurante', 'root', 'root');
        } catch (PDOException $e) {
            die("Error de conexión: " . $e->getMessage());
        }
    }

    public function getPlatosDao()
    {
        if (!$this->platosDao) {
            $this->platosDao = new DAOPlatosMySQL($this->conn);
        }
        return $this->platosDao;
    }

    public function getIngredientesDao()
    {
        if (!$this->ingredientesDao) {
            $this->ingredientesDao = new DAOIngredientesMySQL($this->conn);
        }
        return $this->ingredientesDao;
    }
}
