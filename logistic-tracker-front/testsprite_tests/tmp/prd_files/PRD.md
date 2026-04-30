# Product Requirements Document (PRD)

## 1. Resumen del producto

**SmartShip Frontend** es una aplicacion web construida en Angular 21 para gestionar y monitorear paquetes logisticos en un tablero visual con estados operativos.

El MVP permite:
- autenticacion con JWT,
- visualizacion de paquetes por estado,
- actualizacion de estado via drag & drop,
- gestion administrativa para creacion y mantenimiento.

## 2. Problema a resolver

Equipos de operacion y administracion necesitan una forma rapida y centralizada para:
- ver el estado actual de envios,
- mover paquetes entre etapas del flujo logistico,
- crear y administrar registros de envio,
- reducir errores manuales de seguimiento.

## 3. Objetivos

### Objetivos de negocio
- Disminuir tiempos de actualizacion de estado de paquetes.
- Mejorar trazabilidad operativa en tiempo real.
- Habilitar control de acceso por rol (OPERADOR/ADMIN).

### Objetivos de producto
- Entregar una experiencia clara de tablero logistico.
- Garantizar flujo de autenticacion seguro para el MVP.
- Mantener una arquitectura escalable con componentes standalone y state management con Signals.

## 4. Alcance

### En alcance (MVP)
- Login y manejo de sesion.
- Rutas protegidas por autenticacion y rol.
- Tablero con columnas por estado (`RECEIVED`, `IN_TRANSIT`, `DELIVERED`).
- Drag & drop de paquetes entre columnas con validacion de transiciones.
- Actualizacion optimista de UI con rollback en caso de error backend.
- Vista administrativa para crear/gestionar paquetes.
- Integracion API REST (base URL configurable por environment).

### Fuera de alcance (por ahora)
- Analitica avanzada (SLAs, dashboards BI).
- Notificaciones push/email/SMS.
- Multi-tenant o gestion de multiples organizaciones.
- Auditoria avanzada y exportaciones masivas.

## 5. Usuarios y roles

### Operador logistico
- Consulta y mueve paquetes en tablero.
- Requiere rapidez y feedback inmediato.

### Administrador
- Todo lo del operador.
- Acceso a funcionalidades de administracion/creacion.

## 6. Flujos principales

1. **Inicio de sesion**
   - Usuario accede a `/login`.
   - Se autentica contra backend.
   - Con sesion valida, redireccion a `/board`.

2. **Monitoreo de paquetes**
   - Usuario visualiza paquetes agrupados por estado.
   - Puede filtrar/buscar segun implementacion vigente.

3. **Cambio de estado (drag & drop)**
   - Usuario arrastra paquete entre columnas.
   - Frontend valida transicion.
   - UI aplica cambio optimista.
   - Si backend falla, se revierte cambio y se muestra error.

4. **Gestion administrativa**
   - Usuario ADMIN accede a `/admin`.
   - Crea/gestiona paquetes via formulario y servicios.

## 7. Requisitos funcionales

### RF-01 Autenticacion
- El sistema debe permitir login con credenciales.
- Debe almacenar token de sesion y estado de usuario.

### RF-02 Autorizacion
- El sistema debe restringir rutas con `authGuard`.
- La ruta `/admin` debe requerir rol administrador con `adminGuard`.

### RF-03 Tablero logistico
- El sistema debe mostrar paquetes agrupados por estado.
- Debe soportar actualizacion de estado por interaccion drag & drop.

### RF-04 Validacion de transiciones
- El sistema debe validar transiciones permitidas antes de persistir en API.

### RF-05 Manejo de errores
- Si falla una actualizacion, el sistema debe revertir UI y mostrar mensaje.

### RF-06 Administracion de paquetes
- El sistema debe permitir a ADMIN crear/editar segun capacidades habilitadas en backend y frontend.

## 8. Requisitos no funcionales

### RNF-01 Rendimiento
- Tiempo de carga inicial objetivo: < 3s en red estable local/corporativa.
- Interacciones de cambio de estado deben ser percibidas como inmediatas (optimistic UI).

### RNF-02 Seguridad
- Uso de JWT en cabecera `Authorization` via interceptor.
- Redireccion ante `401` a flujo de autenticacion.

### RNF-03 Mantenibilidad
- Arquitectura basada en capas (componentes, servicios, store).
- Uso de Angular standalone components y Signals.

### RNF-04 Calidad
- Cobertura de pruebas unitarias en modelos, stores y servicios criticos.

## 9. Arquitectura funcional (alto nivel)

- **UI**: componentes standalone para login, board y admin.
- **Estado**: `AuthStore` y `PackageStore` con Signals.
- **Dominio/negocio**: `PackageService` para logica operativa.
- **Infra/API**: `PackageApiService` y `AdminApiService` para llamadas HTTP.
- **Cross-cutting**: interceptor JWT + guards de autenticacion/rol.

## 10. Dependencias

- Backend API disponible y estable (`/api/v1` por environment actual).
- Contratos API consistentes para auth y paquetes.
- Entorno Node.js y Angular CLI compatibles.

## 10.1 Credenciales de acceso (MVP)

- Administrador: `admin` / `admin123`
- Operador (driver): `driver` / `driver123`

## 11. KPIs y metricas de exito

- Tasa de actualizaciones exitosas de estado > 98%.
- Tiempo promedio de cambio de estado (UI) < 1s percibido.
- Tasa de errores de autorizacion no esperados < 1%.
- Tiempo de onboarding de operador nuevo < 30 min.

## 12. Riesgos y mitigaciones

- **Inconsistencia UI/API en optimistic updates**
  - Mitigacion: rollback confiable + mensajes de error claros.
- **Cambios en contratos backend**
  - Mitigacion: tipado estricto y pruebas de servicios.
- **Accesos indebidos por rol**
  - Mitigacion: validacion doble (frontend guards + backend authorization).

## 13. Roadmap sugerido

### Fase 1 (actual MVP)
- Login, guards, board, drag & drop, admin basico.

### Fase 2
- Filtros avanzados, historial de cambios, auditoria basica.

### Fase 3
- Alertas/notificaciones, reportes operativos y panel de metricas.

## 14. Criterios de aceptacion generales

- Usuario no autenticado es redirigido a `/login`.
- Usuario autenticado accede a `/board`.
- Solo ADMIN accede a `/admin`.
- Movimiento de paquete refleja cambio inmediato y persiste en backend.
- Ante error backend, estado se revierte sin romper la interfaz.
