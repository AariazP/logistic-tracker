# SmartShip - Logistics Tracking Frontend

Frontend web para la gestion de paquetes logisticos, construido con Angular 21, componentes standalone, Angular Signals y Angular CDK Drag and Drop.

Este repositorio implementa un MVP orientado a operaciones diarias: autenticacion por rol, visualizacion de paquetes por estado, cambios de estado con validacion de negocio y un modulo administrativo para gestionar destinatarios y conductores.

---

## Tabla De Contenido

- [Vision General](#vision-general)
- [Stack Tecnologico](#stack-tecnologico)
- [Arquitectura Del Proyecto](#arquitectura-del-proyecto)
- [Flujo Funcional End To End](#flujo-funcional-end-to-end)
- [Rutas Y Control De Acceso](#rutas-y-control-de-acceso)
- [Gestion De Estado Con Signals](#gestion-de-estado-con-signals)
- [Reglas De Negocio](#reglas-de-negocio)
- [Integracion Con API](#integracion-con-api)
- [Ejecucion Local](#ejecucion-local)
- [Testing Y Calidad](#testing-y-calidad)
- [Despliegue Con Docker](#despliegue-con-docker)
- [Bitacora De Prompts Y Criterio Senior](#bitacora-de-prompts-y-criterio-senior)
- [Decisiones Tecnicas Relevantes](#decisiones-tecnicas-relevantes)

---

## Vision General

SmartShip permite operar un tablero logistico con tres estados de paquete:

- RECEIVED
- IN_TRANSIT
- DELIVERED

Roles del sistema:

- ADMIN: puede crear paquetes y administrar destinatarios y conductores.
- DRIVER: puede mover paquetes entre columnas respetando transiciones validas.

Objetivos del MVP:

- Reducir friccion operativa en la actualizacion de estados.
- Asegurar reglas de negocio en frontend antes de llamar al backend.
- Mantener una arquitectura mantenible y testeable.

---

## Stack Tecnologico

- Angular 21
- TypeScript 5.9
- Angular CDK (drag and drop)
- Angular Forms (Reactive Forms)
- RxJS (solo para flujos HTTP)
- Signals para estado de aplicacion
- Nginx para servir build de produccion en contenedor

---

## Arquitectura Del Proyecto

Estructura principal:

```text
src/app/
├── core/
│   ├── guards/
│   │   └── auth.guard.ts
│   ├── interceptors/
│   │   └── jwt.interceptor.ts
│   └── services/
│       ├── admin-api.service.ts
│       ├── auth.service.ts
│       ├── auth.store.ts
│       ├── package-api.service.ts
│       └── package.service.ts
├── features/
│   ├── auth/
│   │   └── pages/
│   │       └── login-page.component.ts
│   └── packages/
│       ├── components/
│       │   ├── molecules/
│       │   └── organisms/
│       ├── pages/
│       │   ├── board-page.component.ts
│       │   └── admin-page.component.ts
│       └── store/
│           └── package.store.ts
└── shared/
    └── models/
```

Capas:

- Components: render y eventos de UI.
- Services: casos de uso y coordinacion entre API y store.
- Store: estado reactivo con Signals.
- API services: acceso HTTP puro.
- Guards e interceptors: seguridad transversal.

---

## Flujo Funcional End To End

1. Usuario accede a la app.
2. Si no hay sesion, guard redirige a login.
3. Al autenticar, se guarda token y perfil en sessionStorage.
4. Se carga board con paquetes agrupados por estado.
5. DRIVER puede mover paquetes entre columnas validas.
6. Se aplica actualizacion optimista en UI y se confirma con API.
7. Si API falla, se revierte estado y se muestra error.
8. ADMIN puede entrar a /admin para CRUD de destinatarios y conductores.

---

## Rutas Y Control De Acceso

Rutas definidas:

- /login: acceso publico.
- /board: requiere autenticacion.
- /admin: requiere autenticacion y rol ADMIN.

Controles de seguridad:

- authGuard valida sesion activa.
- adminGuard valida privilegios ADMIN.
- jwtInterceptor adjunta Authorization Bearer token en requests.
- jwtInterceptor limpia sesion y redirige a login ante HTTP 401.

---

## Gestion De Estado Con Signals

Stores principales:

- AuthStore: token, usuario y flags derivados (isAuthenticated, isAdmin, isDriver).
- PackageStore: lista de paquetes, loading, error y paquetes agrupados por estado.

Patron aplicado:

- Signals como unica fuente de verdad del estado de UI.
- Computed para derivaciones (ejemplo: packagesByStatus).
- RxJS restringido a pipelines HTTP en servicios.

Beneficios:

- Menos complejidad accidental por subscripciones manuales.
- Re-render reactivo y predecible en componentes.
- Separacion clara entre estado y transporte de datos.

---

## Reglas De Negocio

Transiciones permitidas de estado:

- RECEIVED -> IN_TRANSIT
- IN_TRANSIT -> DELIVERED
- DELIVERED -> sin salida

Estas reglas viven en modelos compartidos y se validan antes de invocar la API.

En movimiento de paquete:

- Se valida transicion.
- Se aplica cambio optimista.
- Se persiste en backend.
- Se revierte si hay error.

---

## Integracion Con API

Base URL configurable en entorno:

- src/environments/environment.ts

Endpoints consumidos:

- Auth:
  - POST /auth/login
- Packages:
  - GET /packages
  - GET /packages/:trackingId
  - POST /packages
  - PATCH /packages/:trackingId/status
- Admin:
  - GET/POST/PUT/DELETE /admin/recipients
  - GET/POST/DELETE /admin/drivers

---

## Ejecucion Local

Prerequisitos:

- Node.js 20+
- npm 9+

Instalacion:

```bash
npm install
```

Configurar API:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:3000/api',
};
```

Comandos:

```bash
# desarrollo
npm start

# build
npm run build

# pruebas unitarias
npm test
```

Aplicacion local por defecto:

- http://localhost:4200

---

## Testing Y Calidad

Se incluyen pruebas unitarias para piezas clave del dominio y servicios.

Enfoque de testeo:

- Modelos y reglas puras (ejemplo: validacion de transiciones).
- Stores y servicios con foco en estado, errores y casos de negocio.
- Validacion de escenarios de error de API y rollback de estado.

Para ejecutar:

```bash
npm test
```

Carpeta de reporte de cobertura disponible en coverage/.

---

## Despliegue Con Docker

El proyecto usa build multi-stage:

1. Stage builder con Node (compila Angular).
2. Stage runtime con Nginx (sirve estaticos).

Build de imagen:

```bash
docker build -t smartship-frontend .
```

Ejecucion:

```bash
docker run --rm -p 8080:80 smartship-frontend
```

App disponible en:

- http://localhost:8080

---

## Bitacora De Prompts Y Criterio Senior

### Bitacora De Prompts

Registro obligatorio de prompts usados para:

- Arquitectura del flujo de estados.
- Generacion de tests.

Cada entrada debe incluir como minimo:

- Prompt utilizado.
- Resultado generado por IA.
- Evaluacion tecnica del resultado.
- Cambios aplicados tras revision.

### Criterio Senior

Es obligatorio detallar que codigo generado por IA fue refactorizado por no cumplir:

- Logica de negocio.
- Estandares de seguridad.

Para cada refactor registrar:

- Que parte no cumplia.
- Riesgo detectado (funcional o de seguridad).
- Refactor aplicado.
- Resultado final esperado.

### Registro Actual De Ejemplos

#### Caso 1: Store de estado

- Prompt: disenar store con Signals para paquetes y agrupacion por estado.
- Generado inicialmente: estado con BehaviorSubject y conversion a Signal.
- Problema detectado: doble abstraccion, complejidad innecesaria, patron no alineado con Signals-first.
- Refactor aplicado: signal y computed como fuente unica de verdad; RxJS solo en HTTP.

#### Caso 2: Drag and drop

- Prompt: implementar tablero con drag and drop y rollback en error.
- Generado inicialmente: copia local de arrays y recarga completa de API por movimiento.
- Problema detectado: doble fuente de verdad, flicker y sobrecosto de red.
- Refactor aplicado: binding directo al store, update optimista, revert en fallo sin recarga total.

---

## Decisiones Tecnicas Relevantes

| Decision | Razon |
|---|---|
| sessionStorage para token | Persistencia por pestana y menor ventana de riesgo frente a almacenamiento permanente |
| Separar PackageApiService de PackageService | Aisla transporte HTTP de reglas de negocio |
| Reglas de transicion en modelos compartidos | Facilita pruebas unitarias y consistencia del dominio |
| Interceptor funcional | Patron moderno de Angular para provideHttpClient con interceptores |
| Guards por rol | Control de acceso temprano a nivel de routing |

---

## Scripts Disponibles

- npm start: levanta servidor de desarrollo.
- npm run build: genera build de produccion.
- npm run watch: build en modo watch para desarrollo.
- npm test: ejecuta pruebas unitarias.
