# SmartShip API - Endpoints para Integracion Frontend

Este documento describe el contrato de integracion del frontend con la API backend.

## Base URL

- Local (Docker): `http://localhost:8080`
- Prefijo API: `/api/v1`

## Autenticacion y Autorizacion

La API usa JWT Bearer Token.

1. El frontend hace login en `POST /api/v1/auth/login`.
2. Guarda el `token` recibido.
3. En endpoints protegidos, enviar header:

```http
Authorization: Bearer <token>
```

Roles:

- `ADMIN`: puede crear paquetes y consultar paquetes.
- `DRIVER`: puede consultar paquetes y actualizar estado.

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
- Para `401 Unauthorized` y `403 Forbidden`, Spring Security puede responder con formato distinto al anterior.

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
- `role`: rol resuelto del usuario (`ADMIN` o `DRIVER`).

### Errores comunes

- `400 Bad Request`: body invalido (campos faltantes o vacios).
- `401 Unauthorized`: credenciales incorrectas.

---

## 2) Crear paquete

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
  "recipientName": "John Doe"
}
```

### Validaciones

- `trackingId`: requerido, maximo 100 caracteres.
- `weight`: debe ser mayor que 0.
- `dimensions`: requerido, maximo 255 caracteres.
- `recipientName`: requerido, maximo 255 caracteres.

### Response 201

```json
{
  "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "trackingId": "TRK-2026-001",
  "weight": 2.5,
  "dimensions": "30x20x10cm",
  "recipientName": "John Doe",
  "status": "RECEIVED",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T12:00:00Z"
}
```

### Errores comunes

- `400 Bad Request`: validacion fallida.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `ADMIN`.
- `409 Conflict`: `trackingId` duplicado.

---

## 3) Listar paquetes

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
    "recipientName": "John Doe",
    "status": "IN_TRANSIT",
    "createdAt": "2026-04-30T12:00:00Z",
    "updatedAt": "2026-04-30T14:30:00Z"
  }
]
```

### Errores comunes

- `400 Bad Request`: valor de `status` invalido.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: rol no autorizado.

---

## 4) Obtener paquete por trackingId

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
  "recipientName": "John Doe",
  "status": "IN_TRANSIT",
  "createdAt": "2026-04-30T12:00:00Z",
  "updatedAt": "2026-04-30T14:30:00Z"
}
```

### Errores comunes

- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: rol no autorizado.
- `404 Not Found`: trackingId no existe.

---

## 5) Actualizar estado de paquete

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

### Errores comunes

- `400 Bad Request`: body invalido o `status` nulo.
- `401 Unauthorized`: token ausente o invalido.
- `403 Forbidden`: token valido pero sin rol `DRIVER`.
- `404 Not Found`: trackingId no existe.
- `409 Conflict`: transicion de estado no permitida.

---

## Resumen Rapido para Frontend

1. Hacer login y guardar `token` + `role`.
2. Adjuntar `Authorization: Bearer <token>` en todos los endpoints de paquetes.
3. Mostrar acciones segun rol:
   - `ADMIN`: crear + listar + detalle.
   - `DRIVER`: listar + detalle + actualizar estado.
4. Manejar errores por `status` HTTP y, cuando exista, leer `message` y `fieldErrors`.
