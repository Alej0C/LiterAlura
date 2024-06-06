# Proyecto Literalura

Este proyecto es una aplicación Java para gestionar libros y autores utilizando Spring Boot y JPA con PostgreSQL como base de datos. La aplicación permite buscar libros por título, listar libros registrados, listar autores registrados, listar autores vivos en un determinado año y listar libros por idiomas.

## Requisitos

- Java 11 o superior
- Maven 3.6.3 o superior
- PostgreSQL 12 o superior

## Instalación

1. Clona este repositorio en tu máquina local:
    ```bash
    git clone https://github.com/tu_usuario/literalura.git
    ```

2. Navega al directorio del proyecto:
    ```bash
    cd literalura
    ```

3. Configura la base de datos PostgreSQL:
    - Crea una base de datos para el proyecto:
        ```sql
        CREATE DATABASE literalura;
        ```
    - Asegúrate de tener el usuario y contraseña configurados en PostgreSQL.

4. Configura las propiedades de la base de datos en `src/main/resources/application.properties`:
    ```properties
    # Configuración de la base de datos PostgreSQL
    spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    # Configuración de H2 para desarrollo (opcional)
    # spring.datasource.url=jdbc:h2:mem:testdb
    # spring.datasource.driverClassName=org.h2.Driver
    # spring.datasource.username=sa
    # spring.datasource.password=password
    # spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    ```

5. Construye y ejecuta la aplicación:
    ```bash
    mvn spring-boot:run
    ```

## Uso

1. Una vez que la aplicación está en funcionamiento, se mostrará un menú en la consola con las siguientes opciones:

    ```
    1 - Buscar libro por título
    2 - Listar libros registrados
    3 - Listar autores registrados
    4 - Listar autores vivos en un determinado año
    5 - Listar libros por idiomas
    0 - Salir
    ```

2. Sigue las indicaciones en la consola para realizar las diferentes operaciones.

## Estructura del Proyecto

- `src/main/java/com/alura/literalura/model`: Contiene las clases de modelo (`Autor`, `Libro`, `Idioma`).
- `src/main/java/com/alura/literalura/repository`: Contiene los repositorios JPA (`AutorRepository`, `LibroRepository`).
- `src/main/java/com/alura/literalura/service`: Contiene los servicios (`ConsumoAPI`, `ConvierteDatos`).
- `src/main/java/com/alura/literalura/principal`: Contiene la clase principal (`Principal`).

## Contribuciones

Las contribuciones son bienvenidas. Por favor, sigue los siguientes pasos:

1. Haz un fork del proyecto.
2. Crea una nueva rama (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza los cambios necesarios y haz commits (`git commit -m 'Agrega nueva funcionalidad'`).
4. Haz push a la rama (`git push origin feature/nueva-funcionalidad`).
5. Crea un Pull Request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Para más detalles, consulta el archivo [LICENSE](LICENSE).

## Contacto

Si tienes alguna pregunta o sugerencia, por favor contacta a [hansalejandroca@gmail.com](mailto:tu_email@dominio.com).
