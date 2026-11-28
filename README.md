# UrbanStyleBackend — API REST

Backend del proyecto académico **UrbanStyle**, desarrollado en **Desarrollo FullStack II (DSY1104), DUOC UC**. La API integra el frontend React con MongoDB Atlas y permite gestionar productos, cuentas de usuario y pedidos.

## Tecnologías

- Java 17 y Spring Boot.
- Spring Web / API REST, Spring Data MongoDB, Spring Security.
- JWT para autenticación, contraseñas cifradas con BCrypt y autorización por roles.
- MongoDB Atlas como base de datos.
- Swagger / OpenAPI para consultar la documentación de endpoints.
- JUnit 5 y Mockito para las pruebas automatizadas.

## Estructura del backend

```text
src/main/java/cl/duoc/UrbanStyleBackend/
  controller/      # Endpoints de productos, usuarios y pedidos
  model/           # Producto, Usuario, Pedido, DetallePedido y roles
  repository/      # Acceso a MongoDB
  security/        # JWT, CORS y configuración de permisos
  service/         # Lógica de negocio
src/test/java/cl/duoc/UrbanStyleBackend/
  service/         # PedidoServiceTest
```

La base de datos de desarrollo se llama **`urbanstyle_db`** y utiliza las colecciones `productos`, `usuarios` y `pedidos`.

## Configuración

El proyecto requiere tener configuradas las variables de entorno **`MONGODB_URI`** y **`JWT_SECRET`**. Sus valores no deben subirse al repositorio ni publicarse en capturas.

La aplicación se ejecuta normalmente en `http://localhost:8080`.

En Windows, desde la carpeta donde está `pom.xml`:

```powershell
.\mvnw.cmd spring-boot:run
```

La variable `MONGODB_URI` debe incluir la conexión autorizada de MongoDB Atlas y la base de datos configurada. `JWT_SECRET` debe contener un secreto de longitud y formato válidos para la firma JWT de la aplicación.

## API REST

### Productos

| Método | Ruta | Permisos | Respuesta habitual |
| --- | --- | --- | --- |
| GET | `/api/productos` | Público | 200 |
| GET | `/api/v1/productos/{id}` | Público | 200 / 404 |
| POST | `/api/v1/productos` | ADMIN | 201 |
| PUT | `/api/v1/productos/{id}` | ADMIN | 200 / 404 |
| DELETE | `/api/v1/productos/{id}` | ADMIN | 204 / 404 |

### Autenticación

| Método | Ruta | Permisos | Respuesta habitual |
| --- | --- | --- | --- |
| POST | `/api/v1/autenticacion/registro` | Público | 201 / 400 / 409 |
| POST | `/api/v1/autenticacion/login` | Público | 200 / 401 |

El registro crea una cuenta con rol `CLIENTE`. Para entrar, el usuario envía correo y contraseña; el backend devuelve un token JWT que React adjunta como `Authorization: Bearer <token>` en solicitudes protegidas.

### Pedidos

| Método | Ruta | Permisos | Respuesta habitual |
| --- | --- | --- | --- |
| POST | `/api/v1/pedidos` | CLIENTE o ADMIN | 201 / 400 / 401 / 403 / 404 |
| GET | `/api/v1/pedidos` | VENDEDOR o ADMIN | 200 |
| GET | `/api/v1/pedidos/{id}` | VENDEDOR o ADMIN | 200 / 404 |

**Reglas importantes:** un pedido vacío o con cantidad no válida se rechaza; se comprueba que el producto exista; los precios y subtotales se calculan en el servidor con los datos de la base, no con valores enviados desde React. Al crearlo queda con estado `PENDIENTE`. No se procesa dinero real.

## Seguridad

- Los passwords se almacenan mediante hash BCrypt.
- La API utiliza JWT y sesiones sin estado en el servidor.
- Las solicitudes protegidas comprueban autenticación y rol actual del usuario.
- El catálogo es público; el CRUD corresponde a ADMIN.
- La creación de pedidos corresponde a CLIENTE y ADMIN; la consulta, a VENDEDOR y ADMIN.
- Spring Security bloquea las demás rutas no habilitadas.
- El frontend local se autoriza mediante la configuración CORS del proyecto.

## Swagger y Postman

Con el servidor iniciado, abrir:

- Swagger: http://localhost:8080/swagger-ui/index.html
- Especificación OpenAPI: http://localhost:8080/v3/api-docs

Para probar rutas protegidas en Postman, primero ejecutar el login y utilizar el JWT recibido como **Bearer Token**. En las pruebas manuales se comprobaron respuestas 200, 201, 204, 400, 401, 403 y 404 según la operación.

## Pruebas automatizadas

Desde la carpeta con `pom.xml`:

```powershell
.\mvnw.cmd test
```

**Última ejecución verificada, 27 de noviembre de 2026:** **5 de 5 pruebas exitosas**, sin fallos ni errores, `BUILD SUCCESS`.

- Cuatro casos de `PedidoServiceTest` con JUnit y Mockito: cálculo de total, cantidad negativa, producto inexistente y pedido vacío.
- Un caso `UrbanStyleBackendApplicationTests` que comprueba la carga del contexto de Spring Boot con la configuración disponible.

Las pruebas del servicio emplean repositorios simulados, por lo que no crean pedidos reales en MongoDB. Las comprobaciones integradas con Postman se realizaron por separado.

## Repositorios

- Backend (este proyecto): https://github.com/Guillermo223101/UrbanStyleBackend — rama `main`.
- Frontend: https://github.com/Guillermo223101/Urban-Style-React — rama `main`.

Proyecto desarrollado para evaluación académica. La API no incluye una pasarela de pagos ni despliegue productivo público.
