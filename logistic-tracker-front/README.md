# SmartShip — Logistics Tracking Frontend

A production-ready Angular 21 MVP for logistics tracking. Built with standalone components, Angular Signals for state management, and Angular CDK Drag & Drop.

---

## Setup Instructions

### Prerequisites

- Node.js 20+ (LTS recommended)
- npm 9+

### Install Dependencies

```bash
npm install
```

### Configure API URL

Edit `src/environments/environment.ts`:

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:3000/api', // point to your backend
};
```

---

## How to Run

### Development Server

```bash
npm start
# or
ng serve
```

App runs at `http://localhost:4200`. It will redirect to `/login` if unauthenticated, then to `/board` after login.

### Build for Production

```bash
npm run build
```

### Run Tests

```bash
npm test
```

---

## Architecture Explanation

```
src/app/
├── core/
│   ├── guards/
│   │   └── auth.guard.ts          # authGuard (JWT check), adminGuard (role check)
│   ├── interceptors/
│   │   └── jwt.interceptor.ts     # Attaches Bearer token; handles 401 → redirect
│   └── services/
│       ├── auth.store.ts          # Signals-based auth state (token, user, roles)
│       ├── auth.service.ts        # HTTP login, delegates to AuthStore
│       ├── package-api.service.ts # Raw HTTP calls (getAll, create, updateStatus)
│       └── package.service.ts     # Business logic (optimistic update, revert, validation)
│
├── features/
│   ├── auth/
│   │   └── pages/
│   │       └── login-page.component.ts
│   └── packages/
│       ├── components/
│       │   ├── package-card.component.ts  # Displays a single package
│       │   └── package-form.component.ts  # Create package (ADMIN only)
│       ├── pages/
│       │   └── board-page.component.ts    # Logistics board with drag & drop
│       └── store/
│           └── package.store.ts           # Signals-based package state
│
└── shared/
    └── models/
        ├── package.model.ts   # Package types + isValidTransition()
        ├── auth.model.ts      # User, AuthCredentials, AuthResponse
        └── api-error.model.ts
```

### Layering

- **Components** → UI only, no business logic, no direct HTTP
- **Services** → business logic, coordinate API + store
- **Store** → reactive state via Angular Signals, no RxJS
- **Interceptors** → cross-cutting HTTP concerns (auth headers, 401 handling)
- **Guards** → route protection based on authentication and roles

---

## Signals Usage Explanation

Angular Signals (`signal()`, `computed()`, `effect()`) replace RxJS-heavy state management patterns.

### `PackageStore` (`features/packages/store/package.store.ts`)

```ts
// Private writable signal
private readonly _packages = signal<Package[]>([]);

// Public read-only derived signal
readonly packages = computed(() => this._packages());

// Derived computation — updates automatically when _packages changes
readonly packagesByStatus = computed(() => {
  const all = this._packages();
  return PACKAGE_STATUSES.reduce<Record<PackageStatus, Package[]>>(...);
});
```

The board component binds directly to `store.packagesByStatus()`. When `PackageService` calls `store.updatePackageStatus(...)`, every dependent computed signal re-evaluates and the template re-renders — no subscriptions needed.

### `AuthStore` (`core/services/auth.store.ts`)

```ts
readonly isAdmin = computed(() => this._user()?.role === 'ADMIN');
```

Used in templates: `@if (authStore.isAdmin()) { <app-package-form /> }` — role-based UI derived purely from signal state.

### Optimistic Update + Revert Pattern

```ts
// Immediately update UI (signal mutation)
this.store.updatePackageStatus(id, toStatus);

// If backend rejects:
catchError((err) => {
  this.store.revertPackageStatus(id, fromStatus); // revert signal
  this.store.setError(err.error?.message);
  return throwError(() => err);
})
```

RxJS is used **only** for HTTP calls (as required by `HttpClient`). All state is managed exclusively via Signals.

---

## 🔥 AI Skill Log

### Skills Applied

This project was built with the following agent skills active:

- `angular-standalone-components` — Enforced `standalone: true` on all components; no NgModules anywhere
- `angular-solid-principles` — Enforced SRP: components have no business logic; services have a single responsibility; `PackageApiService` (raw HTTP) is separated from `PackageService` (business logic)
- `senior-engineering-judgment` — Applied to trade-off decisions around state management

---

### Prompt Log: Signals-Based State Design

**Prompt used:**
> "Design a Signals-based store for Angular 21 that holds package list state, exposes a computed packagesByStatus grouped by RECEIVED/IN_TRANSIT/DELIVERED, and supports optimistic updates with revert on error."

**What was generated:**
An initial version that used `BehaviorSubject` from RxJS to hold state and emitted via `toSignal()`.

**Why it was REJECTED:**
- Violated the Signals philosophy: the state source was RxJS, Signals were just a thin wrapper
- `toSignal()` adds unnecessary observable infrastructure when `signal()` + `computed()` is sufficient
- The `effect()` was used to subscribe to store changes and trigger side effects — this is an anti-pattern for data flow

**What was REFACTORED to:**
- `signal<Package[]>([])` as the single source of truth
- `computed()` for all derived state (`packagesByStatus`, `loading`, `error`)
- Direct mutation via `store.setPackages()` / `store.updatePackageStatus()` — clean imperative API that services call
- RxJS retained **only** in services for `HttpClient` pipelines (`tap`, `catchError`)

---

### Prompt Log: Drag & Drop Implementation

**Prompt used:**
> "Implement Angular CDK drag & drop for a logistics board with 3 columns (RECEIVED, IN_TRANSIT, DELIVERED). On drop, call a service to update status. If backend rejects, revert the UI state and show error."

**What was generated:**
An initial version that called `moveItemInArray` / `transferArrayItem` on a local array copy held in the component, then triggered a full `loadPackages()` refresh from the API on success or failure.

**Why it was REJECTED/REFACTORED:**
- Holding a local copy of packages in the component created a second source of truth alongside the `PackageStore` — violates SRP
- Full API reload after every move is wasteful and creates visible flicker
- The CDK `[cdkDropListData]` binding was pointing to the local copy, not the store's signal — they could diverge

**What was REFACTORED to:**
- `[cdkDropListData]` binds to `store.packagesByStatus()[status]` (derived from signal)
- Optimistic update is done immediately via `store.updatePackageStatus()` (mutates the signal)
- On backend error, `store.revertPackageStatus()` restores the previous status — the CDK list re-renders from the signal automatically
- No local array copies in the component; the store is the single source of truth

---

### Other Notable Decisions

| Decision | Rationale |
|---|---|
| `sessionStorage` for JWT | Secure against XSS compared to `localStorage`; clears on tab close |
| `PackageApiService` + `PackageService` split | ISP: consumers that only need API calls don't get business logic baggage |
| `isValidTransition()` in model layer | Pure function, easily testable, enforces business rules client-side before hitting the API |
| Functional interceptor (`HttpInterceptorFn`) | Required by `provideHttpClient(withInterceptors([...]))` — the modern Angular 17+ pattern; class-based interceptors with `HTTP_INTERCEPTORS` were rejected |
| `adminGuard` redirects to `/board` not `/403` | MVP simplicity; a real implementation would show a proper forbidden page |
