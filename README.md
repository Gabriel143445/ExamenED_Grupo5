# Sistema de Equipos de Redes y Routers â€“ Grupo 5

**Asignatura:** Estructura de Datos Â· **Lenguaje:** Java 17 Â· **Entorno:** Visual Studio Code
**Caso asignado:** Grupo 5 â€“ Sistema de equipos de redes y routers para prÃ¡cticas de networking.

## Integrantes y distribuciÃ³n del trabajo

| Integrante | Usuario GitHub | Responsabilidad | Estructura / mÃ³dulo | Estado |
|-----------|----------------|-----------------|---------------------|--------|
| Rommel | @gamboarommel580-rgb | CoordinaciÃ³n, repositorio e integraciÃ³n | `Main`, `util`, `DatosPrueba`, `PruebasSistema`, README | Terminado |
| Kerly | @kerlyespinoza298-dev | Inventario y mantenimiento | **Lista secuencial** + modelo `EquipoRed`, `ConfiguracionLogica` | Terminado |
| SebastiÃ¡n | @sr632252-crypto | PrÃ©stamos y devoluciones | **Lista simplemente enlazada** + `Nodo` | Terminado |
| Gabriel | @Gabriel143445 | Cola de espera y configuraciÃ³n lÃ³gica | **Cola** y **Pila** | Terminado |
| Esteban | @Esteban-EVIL | Historial y turnos del banco de pruebas | **Lista doblemente enlazada** y **Lista circular** | Terminado |

> Actualizar la columna *Estado* (Pendiente â†’ En progreso â†’ Terminado) a medida que se integran los mÃ³dulos.

## DescripciÃ³n del caso

El laboratorio de networking presta routers, switches y access points a estudiantes o grupos. Cuando un equipo estÃ¡ prestado, los interesados esperan en una cola por orden de llegada. El banco de pruebas compartido se usa por turnos rotativos. Cada equipo tiene una configuraciÃ³n lÃ³gica (hostname, IP/prefijo y VLAN) y el Ãºltimo cambio de configuraciÃ³n puede revertirse.

**Regla diferenciadora:** un equipo enviado a mantenimiento **no puede aparecer como disponible ni ingresar a la cola de asignaciÃ³n**. Se aplica en tres puntos: no se presta, no se puede encolar y, cuando un equipo entra a mantenimiento (manualmente o por devoluciÃ³n con falla), sus solicitudes pendientes se retiran de la cola.

## Estructuras de datos utilizadas

| Estructura | Uso en el sistema | Por quÃ© se eligiÃ³ | Complejidad principal |
|-----------|-------------------|-------------------|-----------------------|
| Lista secuencial (arreglo) | Inventario general | El inventario es estable y se recorre/lista con frecuencia; el arreglo da acceso por Ã­ndice O(1) y crece al duplicar su capacidad | Insertar O(1) amortizado Â· Buscar/Eliminar O(n) |
| Lista simplemente enlazada | PrÃ©stamos activos | Los prÃ©stamos se crean y eliminan constantemente; eliminar un nodo solo reenlaza punteros, sin desplazar elementos | Insertar al final O(1) Â· Buscar/Eliminar O(n) |
| Lista doblemente enlazada | Historial de movimientos | Permite ver el historial cronolÃ³gico (adelante) y lo mÃ¡s reciente primero (atrÃ¡s) sin invertir nada | Insertar al final O(1) Â· Recorridos O(n) |
| Cola (FIFO) | Solicitudes en espera | Es justo atender primero a quien llegÃ³ primero | Encolar/Desencolar O(1) |
| Pila (LIFO) | Cambios de configuraciÃ³n lÃ³gica | Deshacer siempre revierte el cambio mÃ¡s reciente | Apilar/Desapilar O(1) |
| Lista circular | Turnos del banco de pruebas | DespuÃ©s del Ãºltimo grupo el turno vuelve al primero sin reiniciar la lista | Insertar/Avanzar/Eliminar actual O(1) |

Todas las estructuras dinÃ¡micas usan nodos propios (`Nodo`, `NodoDoble`, `NodoCircular`). No se usan `LinkedList`, `Stack`, `Queue`, `ArrayDeque` ni `ArrayList`.

## Arquitectura

```
src/
â”œâ”€â”€ Main.java              MenÃº principal e integraciÃ³n
â”œâ”€â”€ estructuras/           Nodo, ListaSecuencial, ListaSimple, NodoDoble, ListaDoble,
â”‚                          Cola, Pila, NodoCircular, ListaCircular
â”œâ”€â”€ modelo/                EquipoRed, EstadoEquipo, TipoEquipo, ConfiguracionLogica,
â”‚                          Prestamo, Solicitud, CambioConfiguracion, Movimiento,
â”‚                          TipoMovimiento, Turno
â”œâ”€â”€ servicios/             LÃ³gica de negocio: Inventario, Prestamo, ColaEspera,
â”‚                          Configuracion, Historial, Turno
â”œâ”€â”€ menus/                 SubmenÃºs de consola de cada mÃ³dulo
â”œâ”€â”€ util/                  Consola (lectura validada), Resultado, Fechas
â””â”€â”€ datos/                 DatosPrueba y PruebasSistema (casos automÃ¡ticos)
```

Capas: **menÃºs** (entrada/salida) â†’ **servicios** (reglas de negocio y validaciones) â†’ **estructuras** (almacenamiento). Los servicios devuelven un `Resultado` (Ã©xito/error + mensaje) y no leen del teclado.

Flujo de integraciÃ³n: prÃ©stamo de equipo ocupado â†’ **cola** Â· devoluciÃ³n â†’ se atiende la **cola** automÃ¡ticamente Â· devoluciÃ³n con falla â†’ **mantenimiento** + depuraciÃ³n de la cola Â· toda operaciÃ³n â†’ **historial** Â· cambio de configuraciÃ³n â†’ **pila**.

## Datos de prueba y plan de direccionamiento

| CÃ³digo | Tipo | Capacidad | Estado inicial | ConfiguraciÃ³n lÃ³gica |
|-------|------|-----------|----------------|----------------------|
| RED001 | Router | 4 puertos | Disponible | R1-LAB Â· 192.168.10.1/24 Â· VLAN 10 |
| RED002 | Switch | 24 puertos | Prestado (Grupo Redes A) | SW1-LAB Â· 192.168.10.2/24 Â· VLAN 10 |
| RED003 | Access Point | 2 antenas | Mantenimiento | AP1-LAB Â· 192.168.20.1/24 Â· VLAN 20 |
| RED004 | Router | 2 puertos | Prestado (Ana Torres) | R2-LAB Â· 10.0.0.1/30 Â· VLAN 99 |
| RED005 | Switch | 48 puertos | Disponible | SW2-LAB Â· 192.168.10.3/24 Â· VLAN 10 |

Cola inicial: Luis Paredes y Grupo Redes B esperan RED002. Turnos: Grupo A, B y C.

## CompilaciÃ³n y ejecuciÃ³n

Requisitos: JDK 17 o superior y la extensiÃ³n *Extension Pack for Java* en VS Code.

**OpciÃ³n 1 â€“ VS Code:** abrir la carpeta del proyecto, abrir `src/Main.java` y pulsar **Run**.

**OpciÃ³n 2 â€“ Terminal integrada (Windows):**
```
javac -d bin src\*.java src\estructuras\*.java src\modelo\*.java src\servicios\*.java src\menus\*.java src\util\*.java src\datos\*.java
java -cp bin Main
```
o simplemente `compilar_y_ejecutar.bat`.

**Linux / macOS:** `./compilar_y_ejecutar.sh`

## Casos de prueba

La opciÃ³n **10** del menÃº ejecuta 10 casos automÃ¡ticos (29 verificaciones). El detalle y los pasos manuales estÃ¡n en [`docs/CASOS_PRUEBA.md`](docs/CASOS_PRUEBA.md). Las capturas de ejecuciÃ³n en VS Code deben incorporarse a `docs/capturas/` antes de la entrega.

Para verificar directamente el mÃ³dulo de Gabriel despuÃ©s de compilar todo el proyecto, ejecutar `java -cp bin datos.PruebasGabriel` (24 verificaciones de cola, pila, espera y deshacer).

## Evidencia de colaboraciÃ³n

Cada integrante debe trabajar desde su cuenta y aportar commits significativos en una rama propia. La integraciÃ³n de mÃ³dulos y la versiÃ³n final en `main` siguen en progreso. Ver *Insights â†’ Contributors* y el historial de commits del repositorio para comprobar la participaciÃ³n individual.

Gabriel presentÃ³ su mÃ³dulo desde `@Gabriel143445` en el [PR #3](https://github.com/gamboarommel580-rgb/ExamenED_Grupo5/pull/3), con commits de implementaciÃ³n, pruebas y documentaciÃ³n. La integraciÃ³n al proyecto del grupo estÃ¡ pendiente de revisiÃ³n.
