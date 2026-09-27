# Sistema de gestión de tickets en línea

**Primera práctica de implementación de estructuras de datos**

| | |
|---|---|
| **Universidad** | CENFOTEC |
| **Curso** | SOFT-10 Estructuras de Datos |
| **Sección** | SCV5 |
| **Periodo** | C3-2026 |
| **Docente** | Romario Salas Cerdas |
| **Estudiante** | Braden Knuter Córdoba |
| **Lenguaje** | Java (JDK 17 o superior) |

> El programa es **Java estándar**: no depende de ningún IDE ni editor. Se compila con
> `javac` y se ejecuta con `java`, así que corre igual desde la consola, desde Visual Studio
> Code o desde cualquier otro editor.

---

## 1. Descripción del problema

Una empresa necesita un sistema sencillo de **gestión de tickets de soporte** que funcione
por **línea de comandos (CLI)** y que simule el ciclo de vida completo de un ticket.

El ciclo de vida de un ticket es el siguiente:

1. Un **usuario** crea el ticket. En ese momento el ticket recibe su `id`, su `descripcion`,
   el `nombreCompleto` de quien lo reportó, su `prioridad` y su `fechaCreacion`.
   La `fechaResolucion` queda en `null` porque todavía nadie lo ha atendido.
2. El ticket entra a la **cola de tickets pendientes**, colocado según su urgencia.
3. El **administrador** revisa cuál ticket está al frente de la cola y lo **resuelve**.
   Al resolverlo se le asigna la `fechaResolucion`, se saca de la cola de pendientes y se
   guarda en la **lista de tickets resueltos**.
4. El usuario puede **buscar su ticket por el `id`**. Si el ticket aparece en la lista de
   resueltos se muestran todos sus datos; si no aparece, el sistema le avisa que su ticket
   todavía está pendiente de resolución.

El programa tiene entonces dos menús: uno para el **usuario** y otro para el **administrador**.

---

## 2. Estructuras de datos utilizadas y por qué

Ambas estructuras son **dinámicas** y están construidas con **nodos propios**. No se usa
ninguna colección de Java (`PriorityQueue`, `LinkedList`, `ArrayList`, `Queue`, etc.).

### Cola de prioridad → tickets **pendientes**

Los tickets pendientes se guardan en una **cola de prioridad** (`ColaPrioridad`) porque:

- En un sistema de soporte real **no se atiende por orden de llegada nada más**: un servidor
  caído es más urgente que un cambio de color en una pantalla. La cola de prioridad permite
  que el ticket más urgente quede siempre **al frente**.
- El administrador siempre trabaja con **un solo ticket a la vez: el del frente**. La cola
  da exactamente esa operación (`verFrente()` y `remover()`) y nada más, así que no sobra
  ninguna funcionalidad.
- Al ser dinámica, la cola **crece y se encoge sola** según la cantidad de tickets que la
  gente vaya reportando; no hay que definir un tamaño máximo desde el inicio.

### Lista enlazada simple → tickets **resueltos**

Los tickets resueltos se guardan en una **lista enlazada simple** (`ListaEnlazadaSimple`) porque:

- Un ticket resuelto ya no se atiende: solamente se **guarda como historial** y se
  **consulta por su `id`**. Para eso no se necesita ningún orden de urgencia, basta con
  poder recorrer la lista de inicio a fin comparando ids (`buscarNodo()`).
- Los tickets se van insertando **al final** (`insertarNodoFinal()`), así el historial queda
  en el mismo orden en que se fueron resolviendo.
- También es dinámica: se pueden agregar todos los tickets resueltos que hagan falta sin
  definir un tamaño máximo.

---

## 3. Criterio de prioridad

La consigna no definía el criterio de prioridad, así que se usó el siguiente:

Cada ticket tiene un atributo `int prioridad` que el usuario escoge al crearlo:

| Valor | Prioridad |
|---|---|
| `1` | Alta |
| `2` | Media |
| `3` | Baja |

- **Mientras más pequeño es el número, más urgente es el ticket.** Por eso los tickets de
  prioridad **Alta** se colocan más cerca del frente de la cola y se atienden primero.
- Cuando dos tickets tienen **la misma prioridad** se respeta el **orden de llegada (FIFO)**:
  el que se creó primero sale primero. Esto se logra insertando el ticket nuevo **después**
  de todos los tickets que ya tienen una prioridad igual o más urgente, nunca antes de ellos.
- El menú **valida** que la prioridad digitada sea 1, 2 o 3; si no, vuelve a preguntar.

**Ejemplo.** Si se crean los tickets en este orden:

| Orden de creación | id | Prioridad |
|---|---|---|
| 1° | #1 | Baja (3) |
| 2° | #2 | Alta (1) |
| 3° | #3 | Media (2) |
| 4° | #4 | Alta (1) |

La cola queda así: **#2 (Alta) → #4 (Alta) → #3 (Media) → #1 (Baja)**.

El #2 va antes del #4 porque los dos son Alta, pero el #2 llegó primero (FIFO).

---

## 4. Descripción de las clases

El proyecto tiene **6 clases**, cada una en su propio archivo dentro de `src/`.

### `Ticket.java`
Representa un ticket de soporte. Guarda `id`, `descripcion`, `nombreCompleto`, `prioridad`,
`fechaCreacion` y `fechaResolucion`.

- Tiene un contador `private static int cantidad = 0;` **compartido por todos los tickets**.
  Cada vez que se crea un ticket el constructor hace `cantidad++` y usa ese valor como `id`,
  por eso **ningún id se repite nunca**.
- La `fechaCreacion` se toma del reloj con `LocalDateTime.now()` y la `fechaResolucion`
  arranca en `null`.
- `getPrioridadTexto()` traduce el número de prioridad a la palabra "Alta", "Media" o "Baja".
- `toString()` muestra todos los datos del ticket con las fechas formateadas
  (`dd/MM/yyyy HH:mm:ss`). Si la `fechaResolucion` es `null`, muestra la palabra
  **"Pendiente"**.
- **Todos los atributos tienen su getter**, porque son `private` y el resto del programa
  necesita leerlos. En cambio **el único setter es `setFechaResolucion()`**: es el único dato
  que cambia durante el ciclo de vida del ticket, cuando el administrador lo resuelve. El
  `id`, la `descripcion`, el `nombreCompleto`, la `prioridad` y la `fechaCreacion` se asignan
  una sola vez en el constructor y no deben poder modificarse después, porque eso alteraría
  el reporte original del usuario.

### `Nodo.java`
Es la pieza básica de las dos estructuras dinámicas. Guarda un `Ticket` (el dato) y una
referencia `siguiente` que apunta al nodo que va después. Cuando `siguiente` es `null`
significa que ese nodo es el último. **La misma clase `Nodo` se reutiliza** en la cola y en
la lista, porque las dos guardan tickets.

El `ticket` se asigna al crear el nodo y no cambia, por eso solo tiene getter. El
`siguiente` sí tiene setter, porque es justamente lo que las estructuras van reconectando
cada vez que insertan o sacan un nodo.

### `ColaPrioridad.java`
Cola de prioridad con los tickets **pendientes**. Su único atributo es `frente`, el primer
nodo de la cola.

| Método | Qué hace |
|---|---|
| `estaVacia()` | Retorna `true` si no hay ningún ticket pendiente. |
| `insertar(Ticket)` | Coloca el ticket en la posición que le corresponde según su prioridad (respetando FIFO en los empates). |
| `verFrente()` | Retorna el ticket del frente **sin sacarlo**, o `null` si la cola está vacía. |
| `remover()` | Saca el ticket del frente y lo retorna, o `null` si la cola está vacía. |
| `mostrarCola()` | Recorre la cola con un nodo auxiliar e imprime todos los pendientes en el orden en que van a ser atendidos. |

### `ListaEnlazadaSimple.java`
Lista enlazada simple con los tickets **resueltos**. Su único atributo es `primero`, el
primer nodo de la lista.

| Método | Qué hace |
|---|---|
| `estaVacia()` | Retorna `true` si no hay ningún ticket resuelto. |
| `insertarNodoInicio(Ticket)` | Agrega un ticket al principio de la lista. |
| `insertarNodoFinal(Ticket)` | Agrega un ticket al final. **Este es el que usa el administrador**, para que el historial quede en orden de resolución. |
| `buscarNodo(int idBuscar)` | Recorre la lista comparando ids y retorna el ticket, o `null` si no está. |
| `mostrarLista()` | Recorre la lista con un nodo auxiliar e imprime todos los tickets resueltos. |

### `Menu.java`
Tiene **toda la interacción con la persona** y un solo objeto `Scanner`.

- **Menú principal:** 1. Menú de usuario · 2. Menú de administrador · 0. Salir.
- **Menú de usuario:** 1. Crear un ticket nuevo · 2. Buscar un ticket por su id · 0. Volver.
- **Menú de administrador:** 1. Ver el ticket al frente · 2. Resolver el ticket al frente ·
  3. Ver todos los pendientes · 4. Ver todos los resueltos · 0. Volver.
- Valida los datos: el nombre y la descripción **no pueden quedar vacíos**, la prioridad
  **debe ser 1, 2 o 3**, y si la persona escribe letras donde va un número el programa
  **no se cae**: atrapa la excepción `NumberFormatException` y vuelve a mostrar el menú.
- El programa termina **únicamente con la opción 0**; no se usa `System.exit()`.

### `Main.java`
Rutina principal. Solo crea la `ColaPrioridad`, la `ListaEnlazadaSimple` y el `Menu`, y
llama a `menu.mostrarMenuPrincipal()`. **No tiene lógica del programa**, para que cada clase
tenga una sola responsabilidad.

---

## 5. Pasos realizados para obtener la solución

### Paso 1 — Análisis del problema
Se leyó la consigna y se sacaron los requisitos, separando los **datos** de las **acciones**:

- **Datos de un ticket:** `id`, `descripcion`, `nombreCompleto`, `fechaCreacion`,
  `fechaResolucion` (inicia en `null`) y `prioridad`.
- **Acciones del usuario:** crear un ticket y buscar un ticket por su `id`.
- **Acciones del administrador:** ver el ticket al frente de la cola y resolverlo.
- **Requisito clave:** el `id` debe ser único, y tiene que salir de un contador `static`
  dentro de la clase `Ticket` llamado `cantidad`.
- **Estructuras obligatorias:** cola de prioridad para pendientes y lista enlazada simple
  para resueltos, ambas dinámicas y hechas con nodos propios.

### Paso 2 — Diseño de las clases
Se decidió separar el programa en 6 clases, cada una con **una sola responsabilidad**:

| Clase | Responsabilidad |
|---|---|
| `Ticket` | Guardar los datos de un ticket. |
| `Nodo` | Guardar un ticket y la referencia al siguiente. |
| `ColaPrioridad` | Administrar los tickets pendientes. |
| `ListaEnlazadaSimple` | Administrar los tickets resueltos. |
| `Menu` | Hablar con la persona (entradas, validaciones y mensajes). |
| `Main` | Arrancar el programa. |

También se definió en este paso el **criterio de prioridad** (sección 3), porque la consigna
no lo especificaba, y se decidió reutilizar una sola clase `Nodo` para las dos estructuras.

### Paso 3 — Implementación
Se programaron las clases en este orden, probando cada una antes de seguir con la siguiente:

1. `Ticket` — los datos, el contador `static` y el `toString()`.
2. `Nodo` — la pieza base de las estructuras.
3. `ColaPrioridad` — la parte más difícil fue el `insertar()`, porque hay que buscar la
   posición correcta con un nodo auxiliar y conectar el nodo nuevo sin perder la cadena.
4. `ListaEnlazadaSimple` — recorridos con `while (nodoActual != null)`.
5. `Menu` — los tres menús con `do-while` y `switch`, más las validaciones.
6. `Main` — la rutina principal, corta y ordenada.

### Paso 4 — Pruebas
Se compiló con `javac` y se ejecutó el programa varias veces revisando estos casos:

| # | Caso probado | Resultado esperado |
|---|---|---|
| 1 | Crear tickets con prioridad Baja, Alta y Media | El frente de la cola es el de prioridad Alta |
| 2 | Crear dos tickets con la misma prioridad | Sale primero el que se creó primero (FIFO) |
| 3 | Resolver el ticket del frente | Desaparece de la cola y aparece en resueltos con su `fechaResolucion` |
| 4 | Buscar un id resuelto, uno pendiente y uno inexistente | Muestra el ticket / avisa "pendiente" / avisa "no existe" |
| 5 | Ver frente y resolver con la cola vacía | Muestra un mensaje y no se cae |
| 6 | Escribir letras o números fuera de rango en los menús | Muestra "Opción inválida" y vuelve a preguntar |

**Las 6 pruebas pasaron correctamente.** También se compiló con `javac -Xlint:all`, que no
reportó ni errores ni advertencias.

---

## 6. Cómo ejecutar el programa

El programa es Java estándar y **no trae ni necesita configuración de ningún editor**. Solo
hace falta tener instalado el **JDK 17 o superior**.

### Opción A — Por consola (la más simple)

1. Descargar o clonar el repositorio:
   ```
   git clone https://github.com/B0W3YYY/PracticaImplementacion1-Tickets.git
   ```
2. Entrar a la carpeta del proyecto (la que contiene `src` y este `README.md`).
3. Compilar y ejecutar con dos comandos:
   ```
   javac -encoding UTF-8 -d out src/*.java
   java -cp out Main
   ```
4. El menú aparece en la misma consola. Ahí se digitan las opciones y se presiona Enter.

### Opción B — Desde un editor

Cualquier editor con soporte de Java sirve (Visual Studio Code, IntelliJ IDEA, Eclipse,
NetBeans). Hay que abrir la **carpeta del proyecto**, no un archivo suelto, y ejecutar la
clase `Main`.

> **Importante si se ejecuta desde Visual Studio Code:** el programa lee del teclado con
> `Scanner`, y la *Debug Console* que VS Code abre por defecto no acepta texto escrito. Se
> ve el menú pero no se puede digitar. Hay que usar la **terminal integrada**: correrlo con
> los comandos de la Opción A, o poner `"console": "integratedTerminal"` en la
> configuración de ejecución.

En Windows, si las tildes y las ñ se ven raras, agregar:

```
java -Dstdout.encoding=UTF-8 -cp out Main
```

---

## 7. Ejemplo de uso por consola

```
==================================================
   SISTEMA DE GESTIÓN DE TICKETS EN LÍNEA
==================================================
1. Menú de usuario
2. Menú de administrador
0. Salir
Digite una opción: 1
==================================================
   MENÚ DE USUARIO
==================================================
1. Crear un ticket nuevo
2. Buscar un ticket por su id
0. Volver al menú principal
Digite una opción: 1
==================================================
   CREAR UN TICKET NUEVO
==================================================
Digite su nombre completo: Luis Mora
Describa el problema: El servidor de correo no funciona
Prioridades disponibles:
   1. Alta
   2. Media
   3. Baja
Digite la prioridad del ticket: 1

Ticket creado con id #1.
Su ticket quedó en la cola de pendientes con prioridad Alta.
```

Luego, desde el **menú de administrador**, se resuelve ese ticket:

```
==================================================
   RESOLVER EL TICKET DEL FRENTE
==================================================
El ticket #1 fue resuelto con éxito.

Ticket #1
   Usuario:             Luis Mora
   Descripción:         El servidor de correo no funciona
   Prioridad:           Alta (1)
   Fecha de creación:   26/09/2026 14:30:03
   Fecha de resolución: 26/09/2026 14:31:15
```

Y si el usuario busca un ticket que **todavía no ha sido resuelto**:

```
==================================================
   BUSCAR UN TICKET POR SU ID
==================================================
Digite el id del ticket: 3

El ticket #3 aún está pendiente de resolución.
```

---

## 8. Estructura de archivos

```
PracticaImplementacion1-Tickets/
├── src/
│   ├── Main.java                  Rutina principal del programa
│   ├── Menu.java                  Menús y validaciones (CLI)
│   ├── Ticket.java                Datos de un ticket + contador static
│   ├── Nodo.java                  Nodo con un ticket y su siguiente
│   ├── ColaPrioridad.java         Cola de prioridad: tickets pendientes
│   └── ListaEnlazadaSimple.java   Lista enlazada: tickets resueltos
├── .gitignore
└── README.md
```

No hay archivos de configuración de ningún editor: el proyecto es Java estándar y se compila
con `javac`.
