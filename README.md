# Restaurante: aplicación web de gestión de platos

Aplicación didáctica para practicar desarrollo web en servidor. Permite consultar,
filtrar, crear, editar y eliminar platos, y gestionar la relación entre platos e
ingredientes.

El repositorio contiene dos implementaciones de la misma aplicación:

- **PHP**: aplicación basada en páginas PHP, PDO y Apache en Docker.
- **Java**: aplicación Spring Boot MVC con vistas Thymeleaf y acceso JDBC mediante
  `DriverManager`, `Connection` y `PreparedStatement`.

La intención es que se pueda comparar cómo se resuelven las mismas responsabilidades
en ambos entornos: recibir una petición HTTP, trabajar con datos, consultar la base
de datos y generar una respuesta HTML.

## Índice

- [Restaurante: aplicación web de gestión de platos](#restaurante-aplicación-web-de-gestión-de-platos)
  - [Índice](#índice)
  - [Qué permite hacer](#qué-permite-hacer)
  - [Tecnologías](#tecnologías)
    - [PHP](#php)
    - [Java](#java)
  - [Estructura del proyecto](#estructura-del-proyecto)
  - [Modelo de datos](#modelo-de-datos)
  - [Ejecutar la versión PHP](#ejecutar-la-versión-php)
  - [Ejecutar la versión Java](#ejecutar-la-versión-java)
    - [Configuración de conexión JDBC](#configuración-de-conexión-jdbc)
  - [Recorrido de una petición](#recorrido-de-una-petición)
  - [Arquitectura y responsabilidades](#arquitectura-y-responsabilidades)
    - [Entidades](#entidades)
    - [Controlador y vistas](#controlador-y-vistas)
    - [Contratos DAO e implementaciones](#contratos-dao-e-implementaciones)
    - [Factoría y conexión JDBC](#factoría-y-conexión-jdbc)
    - [Equivalencias PHP y Java](#equivalencias-php-y-java)
  - [Rutas de la versión Java](#rutas-de-la-versión-java)

## Qué permite hacer

- Ver el listado de platos.
- Filtrar los platos por tipo (`primero`, `segundo` o `postre`) y por ingrediente.
- Consultar el detalle de un plato y sus ingredientes.
- Crear un plato y asociarle cero o más ingredientes.
- Editar el nombre, tipo, precio e ingredientes de un plato.
- Eliminar un plato.

Los ingredientes disponibles se cargan de la base de datos. Cada plato tiene un
nombre, un precio decimal, un tipo y una lista de ingredientes.

## Tecnologías

### PHP

- PHP 8.3 con Apache.
- PDO para las consultas parametrizadas.
- MariaDB como base de datos.
- Docker Compose para iniciar el servidor web y la base de datos.

### Java

- Java 27, tal como está configurado en el proyecto Maven.
- Spring Boot 4.1.1.
- Spring MVC para las rutas HTTP y Thymeleaf para generar HTML en el servidor.
- MySQL Connector/J y JDBC de bajo nivel (`DriverManager`, `Connection`,
  `PreparedStatement` y `ResultSet`).
- Maven Wrapper, incluido en el proyecto.

## Estructura del proyecto

```text
.
├── php/
│   ├── compose.yml
│   ├── db/
│   │   ├── Dockerfile
│   │   └── schema.sql
│   └── web/
│       ├── components/
│       ├── controller/
│       ├── dao/
│       ├── entities/
│       ├── detalles.php
│       ├── editar.php
│       ├── index.php
│       └── nuevo.php
└── java/
    └── restaurante/
        ├── pom.xml
        └── src/
            ├── main/
            │   ├── java/com/cpifppiramide/restaurante/
            │   │   ├── dao/
            │   │   ├── entities/
            │   │   ├── www/
            │   │   └── RestauranteApplication.java
            │   └── resources/
            │       └── templates/
            └── test/
```

El esquema de la base de datos y los datos iniciales están en
[`php/db/schema.sql`](php/db/schema.sql). El archivo se ejecuta automáticamente
cuando MariaDB inicializa por primera vez su volumen de datos.

## Modelo de datos

El esquema contiene tres tablas:

| Tabla | Propósito |
|---|---|
| `platos` | Datos principales del plato: nombre, precio y tipo. |
| `ingredientes` | Catálogo de ingredientes que se pueden asignar. |
| `platos_ingredientes` | Relación entre platos e ingredientes. |

La relación entre platos e ingredientes es de **muchos a muchos**: un plato puede
tener varios ingredientes y un ingrediente puede formar parte de varios platos.
Por eso se utiliza `platos_ingredientes`, cuya clave primaria compuesta impide
duplicar la misma asociación. Sus claves foráneas eliminan las asociaciones cuando
se borra el plato o el ingrediente correspondiente.

El nombre es la clave primaria de `platos` e `ingredientes` en este ejercicio.
Los tipos permitidos para un plato son `primero`, `segundo` y `postre`.

## Ejecutar la versión PHP

Requisitos: Docker y Docker Compose.

Desde la raíz del repositorio:

```bash
cd php
docker compose up --build
```

Cuando los contenedores estén listos, abre [http://localhost](http://localhost).
La aplicación PHP se sirve en el puerto 80 y MariaDB publica el puerto 3306.

Para detener los servicios, pulsa `Ctrl+C` si Compose está en primer plano o, en
otra terminal, ejecuta:

```bash
cd php
docker compose down
```

Compose conserva los datos en el volumen `mariadb_data`. El esquema y los datos
iniciales se cargan cuando la base de datos crea ese volumen por primera vez; no
se vuelven a insertar en cada inicio del contenedor.

En la versión PHP, la conexión se configura en
[`php/web/dao/DaoFactory.php`](php/web/dao/DaoFactory.php). El host `maria` es el
nombre del servicio de base de datos dentro de la red de Compose.

## Ejecutar la versión Java

Requisitos:

- Java 27.
- La base de datos MariaDB/MySQL disponible en `localhost:3306`.
- El esquema de [`php/db/schema.sql`](php/db/schema.sql) aplicado a una base de
  datos llamada `restaurante`.

Una forma práctica de iniciar la base de datos es usar el servicio MariaDB que
define Compose. Desde la raíz del repositorio:

```bash
cd php
docker compose up --build -d maria
```

Una vez que la base de datos esté lista, inicia Spring Boot desde otra terminal:

```bash
cd java/restaurante
./mvnw spring-boot:run
```

En Windows se puede utilizar el wrapper equivalente:

```bat
cd java\restaurante
mvnw.cmd spring-boot:run
```

Abre [http://localhost:8080](http://localhost:8080).

### Configuración de conexión JDBC

La clase
[`DBConnector`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/DBConnector.java)
lee estas variables de entorno:

| Variable | Valor predeterminado | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/restaurante` | URL JDBC, incluyendo host, puerto y base de datos. |
| `DB_USERNAME` | `root` | Usuario de la base de datos. |
| `DB_PASSWORD` | `root` | Contraseña de la base de datos. |

Por ejemplo, en macOS o Linux:

```bash
cd java/restaurante
DB_URL='jdbc:mysql://localhost:3306/restaurante' \
DB_USERNAME='root' \
DB_PASSWORD='root' \
./mvnw spring-boot:run
```

Si Java se ejecutara dentro de otro contenedor de Compose, `localhost` apuntaría
a ese contenedor, no al servicio de base de datos. En ese caso, se debe usar el
nombre de servicio `maria` en la URL y conectar ambos servicios a la misma red.

El servicio MariaDB del Compose publica el puerto 3306 en el equipo. El PHP dentro
de Compose usa el nombre de servicio `maria`; Java ejecutado directamente en el
equipo usa `localhost`.

## Recorrido de una petición

La versión Java permite ver claramente el patrón **Modelo-Vista-Controlador
(MVC)**:

1. El navegador envía una petición HTTP, por ejemplo `GET /?tipo=postre`.
2. Spring MVC localiza el método del controlador asociado a esa ruta.
3. El controlador solicita los datos al DAO adecuado mediante `DaoFactory`.
4. El DAO ejecuta una consulta SQL parametrizada y convierte el resultado a
   objetos Java.
5. El controlador añade los objetos al `Model` y devuelve el nombre de una vista.
6. Thymeleaf procesa la plantilla HTML con los datos y Spring devuelve la
   respuesta al navegador.

En PHP el recorrido es similar, aunque las páginas y el controlador están
representados por scripts PHP que incluyen el DAO, leen parámetros de la petición
y generan o redirigen la respuesta.

## Arquitectura y responsabilidades

### Entidades

Las clases
[`Plato`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/entities/Plato.java)
y
[`Ingrediente`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/entities/Ingrediente.java)
representan los datos del dominio. No son responsables de consultar la base de
datos ni de gestionar peticiones web.

`Plato` utiliza `BigDecimal` para representar el precio, evitando los errores de
precisión habituales al usar tipos binarios de coma flotante para importes
decimales.

### Controlador y vistas

[`PlatosController`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/www/PlatosController.java)
recibe las peticiones web, obtiene los parámetros, llama a la capa DAO y elige
una vista. No construye directamente el SQL.

Las plantillas Thymeleaf están en `java/restaurante/src/main/resources/templates/`:

- `index.html`: listado y filtros.
- `detalles.html`: datos del plato y sus ingredientes.
- `nuevo.html`: formulario de alta.
- `editar.html`: formulario de edición.
- `components/nav.html`: navegación compartida.

Thymeleaf permite insertar valores del modelo en HTML y generar URL con
expresiones como `th:text`, `th:each`, `th:href` y `th:action`.

### Contratos DAO e implementaciones

Un **DAO** (Data Access Object) encapsula el acceso a los datos y evita que el
controlador dependa de los detalles de SQL.

- [`DAOPlatos`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/platos/DAOPlatos.java)
  define las operaciones de platos.
- [`DAOPlatosMySQL`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/platos/DAOPlatosMySQL.java)
  implementa esas operaciones con JDBC.
- [`DAOIngredientes`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/ingredientes/DAOIngredientes.java)
  define la consulta del catálogo de ingredientes.
- [`DAOIngredientesMySQL`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/ingredientes/DAOIngredientesMySQL.java)
  implementa esa consulta.

Las interfaces hacen explícito qué operaciones ofrece cada DAO, mientras que las
clases `*MySQL` contienen el detalle de cómo se guardan y recuperan los datos.

### Factoría y conexión JDBC

[`DaoFactory`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/DaoFactory.java)
es una factoría singleton: ofrece una instancia única y crea bajo demanda los DAO
MySQL. El controlador solicita los DAO a esta factoría.

[`DBConnector`](java/restaurante/src/main/java/com/cpifppiramide/restaurante/dao/DBConnector.java)
centraliza la obtención de la conexión JDBC. Utiliza `DriverManager` para abrir
una conexión y la mantiene para las consultas posteriores.

Los DAO emplean `PreparedStatement` y parámetros `?` para enviar los valores.
Separar los valores del texto SQL facilita la lectura, maneja correctamente los
tipos y evita construir las consultas concatenando datos recibidos del usuario.
Los `PreparedStatement` y `ResultSet` se cierran con `try`-con-resources.

En las operaciones Java de alta y edición que modifican tanto `platos` como
`platos_ingredientes`, se desactiva temporalmente `autoCommit`: las sentencias se
confirman juntas con `commit()` y, si falla una consulta, se intenta deshacer el
cambio con `rollback()`. De este modo no debería quedar guardado un plato sin las
asociaciones que formaban parte de la misma operación.

### Equivalencias PHP y Java

| Responsabilidad | PHP | Java |
|---|---|---|
| Datos del dominio | `php/web/entities/` | `java/restaurante/.../entities/` |
| Contratos de acceso | `php/web/dao/` | `java/restaurante/.../dao/` |
| Implementación SQL | `DAO*MySQL` con PDO | `DAO*MySQL` con JDBC |
| Creación de DAOs/conexión | `DaoFactory.php` | `DaoFactory.java` y `DBConnector.java` |
| Página/controlador web | Páginas y scripts `php/web/controller/` | `PlatosController.java` |
| Presentación | PHP y HTML en las páginas | Plantillas Thymeleaf |

## Rutas de la versión Java

| Método HTTP | Ruta | Uso |
|---|---|---|
| `GET` | `/` o `/index` | Lista los platos; acepta `tipo` e `ingrediente` como filtros opcionales. |
| `GET` | `/platos/{nombre}` | Muestra los detalles de un plato. |
| `GET` | `/platos/nuevo` | Muestra el formulario de alta. |
| `POST` | `/platos` | Guarda un plato y sus ingredientes. |
| `GET` | `/platos/{nombre}/editar` | Muestra el formulario de edición. |
| `POST` | `/platos/actualizar` | Actualiza un plato y sus ingredientes. |
| `POST` | `/platos/{nombre}/eliminar` | Elimina un plato. |

Los formularios de cambio de estado usan `POST`, mientras que las consultas de
listado, detalle y formularios usan `GET`.

Ejemplo de filtro por tipo:

```text
http://localhost:8080/?tipo=postre
```

Los nombres de plato se incluyen en la ruta como una variable. Spring MVC recibe
el valor mediante `@PathVariable`; el nombre del parámetro de filtro y los datos
de formulario se reciben con `@RequestParam`.
