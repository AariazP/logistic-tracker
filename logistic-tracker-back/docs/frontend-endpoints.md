# SmartShip API - Endpoints para Integracion Frontend

Este documento describe el contrato real actual de la API backend para que el frontend pueda integrarse sin asumir campos o flujos que ya no existen.

## Base URL

- Local (Docker): `http://localhost:8080`
- Prefijo API: `/api/v1`
- Swagger UI disponible en: `/swagger-ui.html`
- OpenAPI JSON disponible en: `/v3/api-docs`

## CORS

Por defecto, el backend permite solicitudes desde `http://localhost:4200`.

Si se configura `app.cors.allowed-origins`, pueden habilitarse multiples origenes separados por coma.

## Autenticacion y Autorizacion

La API usa JWT Bearer Token.

Flujo general:

1. El frontend hace login en `POST /api/v1/auth/login`.
2. Guarda `token` y `role`.
3. En cada endpoint protegido, envia el header:

```http
Authorization: Bearer <token>
```

Roles actuales:

- `ADMIN`: gestiona recipients, drivers y crea paquetes. Tambien puede listar y consultar detalle de paquetes.
- `DRIVER`: puede listar paquetes, consultar detalle y actualizar su estado.

## Formato de Error Estandar

Cuando el error es manejado por el `GlobalExceptionHandler`, la respuesta tiene este formato:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error",
  "timestamp": "2026-04-30T12:00:00Z",
  "fieldErrors": {
    "trackingId": "Tracking ID must not be blank"
  }
}
```

Notas:

- `fieldErrors` puede ser `null` cuando no hay errores de validacion por campo.
- `401 Unauthorized` y `403 Forbidden` pueden venir con un formato distinto porque los resuelve Spring Security.
- Algunas reglas de negocio que hoy lanzan `IllegalArgumentException` responden `400 Bad Request`, no `409 Conflict`.

## Resumen de recursos

### Auth

- `POST /api/v1/auth/login`

### Recipients (`ADMIN`)

- `POST /api/v1/admin/recipients`
- `GET /api/v1/admin/recipients`
- `PUT /api/v1/admin/recipients/{recipientId}`
- `DELETE /api/v1/admin/recipients/{recipientId}`

### Drivers (`ADMIN`)

- `POST /api/v1/admin/drivers`
- `GET /api/v1/admin/drivers`
- `DELETE /api/v1/admin/drivers/{driverId}`

### Packages

- `POST /api/v1/packages` (`ADMIN`)
- `GET /api/v1/packages` (`ADMIN` o `DRIVER`)
- `GET /api/v1/packages/{trackingId}` (`ADMIN` o `DRIVER`)
- `PATCH /api/v1/packages/{trackingId}/status` (`DRIVER`)

---

## 1) Login

### Endpoint

- Metodo: `POST`
- Ruta: `/api/v1/auth/login`
- Auth requerida: No

### Request Body

```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Validaciones

- `username`: requerido, no vacio.
- `password`: requerido, no vacio.

### Response 200

```json
{
  "token": "<JWT>",
  "role": "ADMIN"
}
```

Campos:

- `token`: JWT para usar en `Authorization: Bearer`.
- `role`: rol resuelto del usuario autenticado. Valores esperados: `ADMIN` o `DRIVER`.

### Logica para frontend

- Guardar `token` y `role` al iniciar sesion.
- Definir navegacion y acciones visibles segun el `role`.
- Si el login falla por credenciales invalidas, mostrar error general, no error de formulario por campo.

### Errores comunes

- `400 Bad Request`: body invalido o campos vacios.
- `401 Unauthorized`: credenciales incorrectas.

---

## 2) Crear recipient

### Endpoint

- Metodo: `POST`
- Ruta: `/api/v1/admin/recipients`
- Auth requerida: Si (`ADMIN`)

### Headers

```http
Authorization: Bearer <token_admin>
Content-Type: application/json
```

### Request Body

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+57 3000000000",
  "address": "Calle 123 #45-67, Bogota",
  "documentNumber": "CC-123456789"
}
```

### Validaciones

- `name`: requerido, maximo 255 caracteres.
- `email`: requerido, formato email valido, maximo 255 caracteres.
- `phone`: requerido, maximo 50 caracteres.
- `address`: requerido, maximo 255 caracteres.
- `documentNumber`: requerido, maximo 50 caracteres.

### Response 201

```json
{
  "id": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "phone": "+57 3000000000",
  "address": "Calle 123 #45-67, Bogota",
  "documentNumber": "CC-123456789",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T12:00:00Z"
}
```

### Logica para frontend

- Este recurso debe existir antes de crear un paquete, porque `POST /api/v1/packages` ahora recibe `recipientId` y no `recipientName`.
- El frontend puede permitir crear recipient desde un modal o pantalla previa al formulario de creacion de paquete.

### Errores comunes

- `400 Bad Request`: validacion fallida.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.

---

## 3) Listar recipients

### Endpoint

- Metodo: `GET`
- Ruta: `/api/v1/admin/recipients`
- Auth requerida: Si (`ADMIN`)

### Headers

```http
Authorization: Bearer <token_admin>
```

### Response 200

```json
[
  {
    "id": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "phone": "+57 3000000000",
    "address": "Calle 123 #45-67, Bogota",
    "documentNumber": "CC-123456789",
    "createdAt": "2026-04-30T12:00:00Z",
    "updatedAt": "2026-04-30T12:00:00Z"
  }
]
```

### Logica para frontend

- Usar este endpoint para poblar el selector de destinatario al crear paquetes.
- Conviene cachear la lista si la UI de admin reutiliza recipients en varias pantallas.

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.

---

## 4) Actualizar recipient

### Endpoint

- Metodo: `PUT`
- Ruta: `/api/v1/admin/recipients/{recipientId}`
- Auth requerida: Si (`ADMIN`)

### Path Params

- `recipientId`: UUID del recipient.

### Headers

```http
Authorization: Bearer <token_admin>
Content-Type: application/json
```

### Request Body

```json
{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "+57 3000000001",
  "address": "Carrera 10 #20-30, Medellin",
  "documentNumber": "CC-123456789"
}
```

### Validaciones

- Las mismas de creacion: todos los campos son obligatorios.
- Este endpoint no soporta update parcial. El frontend debe enviar el objeto completo.

### Response 200

```json
{
  "id": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "phone": "+57 3000000001",
  "address": "Carrera 10 #20-30, Medellin",
  "documentNumber": "CC-123456789",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T13:30:00Z"
}
```

### Logica para frontend

- Si el recipient no existe, la API responde `404`.
- Si la UI edita recipients asociados a paquetes ya creados, recordar que los paquetes guardan `recipientName` como snapshot al momento de creacion. Actualizar el recipient no reescribe paquetes historicos.

### Errores comunes

- `400 Bad Request`: validacion fallida.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.
- `404 Not Found`: recipient no existe.

---

## 5) Eliminar recipient

### Endpoint

- Metodo: `DELETE`
- Ruta: `/api/v1/admin/recipients/{recipientId}`
- Auth requerida: Si (`ADMIN`)

### Path Params

- `recipientId`: UUID del recipient.

### Headers

```http
Authorization: Bearer <token_admin>
```

### Response 204

Sin body.

### Logica para frontend

- Un recipient solo puede eliminarse si no tiene paquetes activos asociados.
- La API considera activo cualquier paquete con estado distinto de `DELIVERED`.
- Si el delete falla con `409 Conflict`, el frontend debe informar que primero deben cerrarse o entregarse los paquetes pendientes de ese recipient.

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.
- `404 Not Found`: recipient no existe.
- `409 Conflict`: el recipient tiene paquetes en `RECEIVED` o `IN_TRANSIT`.

---

## 6) Crear driver

### Endpoint

- Metodo: `POST`
- Ruta: `/api/v1/admin/drivers`
- Auth requerida: Si (`ADMIN`)

### Headers

```http
Authorization: Bearer <token_admin>
Content-Type: application/json
```

### Request Body

```json
{
  "username": "driver01",
  "password": "driver123"
}
```

### Validaciones

- `username`: requerido, maximo 100 caracteres.
- `password`: requerido, entre 6 y 255 caracteres.

### Response 201

```json
{
  "id": "5db3d9d1-1d5a-458f-95c0-f064b5bce3bc",
  "username": "driver01",
  "role": "DRIVER",
  "createdAt": "2026-04-30T12:00:00Z"
}
```

### Logica para frontend

- Este endpoint solo crea usuarios con rol `DRIVER`.
- La password nunca vuelve en la respuesta.
- Si el username ya existe, hoy el backend responde `400 Bad Request`.

### Errores comunes

- `400 Bad Request`: validacion fallida o username ya existente.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.

---

## 7) Listar drivers

### Endpoint

- Metodo: `GET`
- Ruta: `/api/v1/admin/drivers`
- Auth requerida: Si (`ADMIN`)

### Headers

```http
Authorization: Bearer <token_admin>
```

### Response 200

```json
[
  {
    "id": "5db3d9d1-1d5a-458f-95c0-f064b5bce3bc",
    "username": "driver01",
    "role": "DRIVER",
    "createdAt": "2026-04-30T12:00:00Z"
  }
]
```

### Logica para frontend

- Sirve para pantallas administrativas de mantenimiento de conductores.
- El frontend no necesita inferir el rol: ya viene en la respuesta.

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.

---

## 8) Eliminar driver

### Endpoint

- Metodo: `DELETE`
- Ruta: `/api/v1/admin/drivers/{driverId}`
- Auth requerida: Si (`ADMIN`)

### Path Params

- `driverId`: UUID del driver.

### Headers

```http
Authorization: Bearer <token_admin>
```

### Response 204

Sin body.

### Logica para frontend

- Solo elimina usuarios con rol `DRIVER`.
- Si el UUID no existe o pertenece a otro tipo de usuario, la API responde `404`.

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.
- `404 Not Found`: driver no existe.

---

## 9) Crear paquete

### Endpoint

- Metodo: `POST`
- Ruta: `/api/v1/packages`
- Auth requerida: Si (`ADMIN`)

### Headers

```http
Authorization: Bearer <token_admin>
Content-Type: application/json
```

### Request Body

```json
{
  "trackingId": "TRK-2026-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientId": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f"
}
```

### Validaciones

- `trackingId`: requerido, maximo 100 caracteres.
- `weight`: debe ser mayor que 0.
- `dimensions`: requerido, maximo 255 caracteres.
- `recipientId`: requerido, debe ser UUID valido.

### Response 201

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "trackingId": "TRK-2026-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientId": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
  "recipientName": "John Doe",
  "status": "RECEIVED",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T12:00:00Z"
}
```

### Logica para frontend

- El frontend debe seleccionar un recipient existente y enviar su `recipientId`.
- La API resuelve internamente el `recipientName` a partir del recipient seleccionado y lo devuelve en la respuesta.
- Un paquete nuevo siempre nace en estado `RECEIVED`.
- Si el `trackingId` ya existe, el backend hoy responde `400 Bad Request`.

### Errores comunes

- `400 Bad Request`: validacion fallida o `trackingId` duplicado.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.
- `404 Not Found`: `recipientId` no existe.

---

## 10) Listar paquetes

### Endpoint

- Metodo: `GET`
- Ruta: `/api/v1/packages`
- Auth requerida: Si (`ADMIN` o `DRIVER`)

### Query Params

- `status` (opcional): `RECEIVED`, `IN_TRANSIT`, `DELIVERED`

Ejemplos:

- `/api/v1/packages`
- `/api/v1/packages?status=IN_TRANSIT`

### Headers

```http
Authorization: Bearer <token>
```

### Response 200

```json
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "trackingId": "TRK-2026-001",
    "weight": 2.5,
    "dimensions": "30x20x10cm",
    "recipientId": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
    "recipientName": "John Doe",
    "status": "IN_TRANSIT",
    "createdAt": "2026-04-30T12:00:00Z",
    "updatedAt": "2026-04-30T14:30:00Z"
  }
]
```

### Logica para frontend

- Si se envia `status`, la lista viene filtrada exactamente por ese estado.
- Si no se envia `status`, la API devuelve todos los paquetes.
- Este endpoint es la base para vistas de tablero, listado general o tabs por estado.

### Errores comunes

- `400 Bad Request`: valor de `status` invalido.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: rol no autorizado.

---

## 11) Obtener paquete por trackingId

### Endpoint

- Metodo: `GET`
- Ruta: `/api/v1/packages/{trackingId}`
- Auth requerida: Si (`ADMIN` o `DRIVER`)

### Path Params

- `trackingId`: identificador de tracking.

Ejemplo:

- `/api/v1/packages/TRK-2026-001`

### Headers

```http
Authorization: Bearer <token>
```

### Response 200

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "trackingId": "TRK-2026-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientId": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
  "recipientName": "John Doe",
  "status": "IN_TRANSIT",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T14:30:00Z"
}
```

### Logica para frontend

- Ideal para pantalla de detalle o para resolver una busqueda directa por tracking.
- La consulta se hace por `trackingId`, no por `id` UUID del paquete.

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: rol no autorizado.
- `404 Not Found`: `trackingId` no existe.

---

## 12) Actualizar estado de paquete

### Endpoint

- Metodo: `PATCH`
- Ruta: `/api/v1/packages/{trackingId}/status`
- Auth requerida: Si (`DRIVER`)

### Headers

```http
Authorization: Bearer <token_driver>
Content-Type: application/json
```

### Request Body

```json
{
  "status": "IN_TRANSIT"
}
```

Valores permitidos de `status`:

- `RECEIVED`
- `IN_TRANSIT`
- `DELIVERED`

### Response 200

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "trackingId": "TRK-2026-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientId": "6f1d4eb9-29fb-4606-97ef-c7fbc3bb248f",
  "recipientName": "John Doe",
  "status": "DELIVERED",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T18:00:00Z"
}
```

### Reglas de transicion

- Permitido: `RECEIVED -> IN_TRANSIT -> DELIVERED`
- No permitido:
  - `RECEIVED -> DELIVERED`
  - Cualquier transicion reversa
  - Cualquier transicion desde `DELIVERED`

### Logica para frontend

- El frontend debe calcular las acciones disponibles segun el estado actual:
  - Si esta en `RECEIVED`, solo mostrar opcion para pasar a `IN_TRANSIT`.
  - Si esta en `IN_TRANSIT`, solo mostrar opcion para pasar a `DELIVERED`.
  - Si esta en `DELIVERED`, no mostrar acciones de cambio.
- Aunque el frontend oculte acciones invalidas, debe seguir manejando `409 Conflict` por seguridad.

### Errores comunes

- `400 Bad Request`: body invalido o `status` nulo.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `DRIVER`.
- `404 Not Found`: `trackingId` no existe.
- `409 Conflict`: transicion de estado no permitida.

---

## Flujos recomendados para frontend

### Flujo ADMIN

1. Hacer login y validar que `role === "ADMIN"`.
2. Cargar recipients con `GET /api/v1/admin/recipients`.
3. Permitir crear o editar recipients desde el modulo administrativo.
4. Al crear un paquete, usar el `recipientId` del recipient seleccionado.
5. Gestionar drivers desde `GET/POST/DELETE /api/v1/admin/drivers`.

### Flujo DRIVER

1. Hacer login y validar que `role === "DRIVER"`.
2. Listar paquetes con `GET /api/v1/packages`.
3. Filtrar por estado con el query param `status` cuando sea necesario.
4. En detalle o listado, habilitar solo la siguiente transicion valida.
5. Refrescar la fila o detalle del paquete despues de `PATCH /status`.

### Manejo de errores en UI

1. Para `400` con `fieldErrors`, pintar errores por campo.
2. Para `400` sin `fieldErrors`, mostrar `message` como error general.
3. Para `401`, limpiar sesion y redirigir a login.
4. Para `403`, mostrar vista o mensaje de permisos insuficientes.
5. Para `404`, mostrar mensaje de recurso inexistente.
6. Para `409`, mostrar mensaje contextual de conflicto de negocio.
