# MS-09 Control de Inventario y Herramientas

> **CONAF — Coordinación de Emergencias por Incendios Forestales**  
> Microservicio encargado de gestionar el stock de herramientas, 
> asignaciones a brigadistas y reportes de desgaste al cierre de turno.

---

## Contexto

El brigadista Felipe necesita registrar qué herramientas utilizó 
(motosierras, palas, hachas) y reportar su nivel de desgaste al 
cerrar su turno, para asegurar el correcto reabastecimiento y 
mantenimiento del equipamiento para el siguiente turno.

---

## Stack Tecnológico

| Lenguaje | Java 21 |
| Framework | Spring Boot 3.4.5 |
| Base de datos | PostgreSQL (NeonTech) |
| ORM | Spring Data JPA / Hibernate |
| Validaciones | Bean Validation (@NotBlank, @Min, @Max) |
| Manejo de errores | @RestControllerAdvice |
| Puerto | 8089 |

---

## Entidades

- **Herramienta** — inventario de herramientas con stock disponible y total
- **AsignacionHerramienta** — registro de qué brigadista usa qué herramienta
- **ReporteDesgaste** — informe de estado al cierre de turno

---

## Endpoint Base
## // http://localhost:8089/api/v1/herramientas

---

## Roadmap de Endpoints

### 1. Crear herramienta
**POST** `/api/v1/herramientas`

```json
{
  "nombre": "Motosierra Stihl MS 170",
  "tipo": "MOTOSIERRA",
  "estado": "DISPONIBLE",
  "stockDisponible": 3,
  "stockTotal": 5,
  "descripcion": "Motosierra para corte de árboles en faenas"
}
```

Respuesta exitosa `201 Created`:
```json
{
  "id": 1,
  "nombre": "Motosierra Stihl MS 170",
  "tipo": "MOTOSIERRA",
  "estado": "DISPONIBLE",
  "stockDisponible": 3,
  "stockTotal": 5,
  "descripcion": "Motosierra para corte de árboles en faenas"
}
```

---

### 2. Obtener todas las herramientas
**GET** `/api/v1/herramientas`

Respuesta exitosa `200 OK`:
```json
[
  {
    "id": 1,
    "nombre": "Motosierra Stihl MS 170",
    "tipo": "MOTOSIERRA",
    "estado": "DISPONIBLE",
    "stockDisponible": 3,
    "stockTotal": 5
  },
  {
    "id": 2,
    "nombre": "Pala Forestal",
    "tipo": "PALA",
    "estado": "DISPONIBLE",
    "stockDisponible": 10,
    "stockTotal": 12
  }
]
```

---

### 3. Asignar herramienta a brigadista
**POST** `/api/v1/herramientas/asignaciones`

```json
{
  "herramientaId": 1,
  "brigadistaId": 42,
  "observaciones": "Asignada a Felipe en su rol de motosierrista titular para el resguardo del camión aljibe"
}
```

Respuesta exitosa `201 Created`:
```json
{
  "id": 1,
  "herramienta": { "id": 1, "nombre": "Motosierra Stihl MS 170" },
  "brigadistaId": 42,
  "fechaAsignacion": "2026-05-28T10:30:00",
  "observaciones": "Asignada a Felipe en su rol de motosierrista titular para el resguardo del camión aljibe"
}
```

Error sin stock `400 Bad Request`:
```json
{
  "timestamp": "2026-05-28T10:30:00",
  "status": 400,
  "error": "Sin stock disponible",
  "message": "No hay stock disponible para la herramienta: Motosierra Stihl MS 170"
}
```

---

### 4. Reportar desgaste al cierre de turno
**POST** `/api/v1/herramientas/reportes`

```json
{
  "herramientaId": 1,
  "brigadistaId": 42,
  "nivelDesgaste": 8,
  "descripcionDaño": "Cadena desgastada, requiere reemplazo urgente"
}
```

Respuesta exitosa `201 Created`:
```json
{
  "id": 1,
  "herramienta": { "id": 1, "nombre": "Motosierra Stihl MS 170" },
  "brigadistaId": 42,
  "nivelDesgaste": 8,
  "descripcionDaño": "Cadena desgastada, requiere reemplazo urgente",
  "fechaReporte": "2026-05-28T20:00:00",
  "requiereMantencion": true
}
```

> Si `nivelDesgaste >= 7`, el sistema cambia automáticamente el estado
> de la herramienta a `EN_MANTENCION`.

---

### 5. Ver herramientas dañadas
**GET** `/api/v1/herramientas/reportes/dañadas`

Respuesta exitosa `200 OK`:
```json
[
  {
    "id": 1,
    "herramienta": { "id": 1, "nombre": "Motosierra Stihl MS 170" },
    "brigadistaId": 42,
    "nivelDesgaste": 8,
    "requiereMantencion": true,
    "fechaReporte": "2026-05-28T20:00:00"
  }
]
```

---

### 6. Validación — stock negativo rechazado
**POST** `/api/v1/herramientas` con stock negativo:

```json
{
  "nombre": "Pala",
  "tipo": "PALA",
  "estado": "DISPONIBLE",
  "stockDisponible": -1,
  "stockTotal": 10
}
```

Error `400 Bad Request`:
```json
{
  "timestamp": "2026-05-28T10:00:00",
  "status": 400,
  "error": "Error de validación",
  "message": ["stockDisponible: El stock no puede ser negativo"]
}
```

---