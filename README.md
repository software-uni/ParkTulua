# Parqueadero App – Aplicación de Conductores (Android / Kotlin)

Guía oficial del equipo para crear, ordenar y mantener este repositorio.
**Léela completa antes de escribir la primera línea de código.** Si algo no está claro, se pregunta en el grupo antes de improvisar.

---

## 1. ¿Qué estamos construyendo?

Una aplicación móvil para **conductores** hecha en **Kotlin** con **Jetpack Compose** (la forma moderna de dibujar pantallas en Android), siguiendo la arquitectura **MVVM**.

| Pieza | Tecnología | Para qué sirve |
|---|---|---|
| App de conductores (**este repositorio**) | Kotlin + Jetpack Compose, MVVM | Lo que ve y usa el conductor |
| App web de administración | React + TypeScript + Vite (repositorio `parkTulua-web`) | Lo que usa el personal del parqueadero |
| Base de datos, reglas de acceso y funciones de servidor | Supabase (PostgreSQL, RLS, funciones) | Guardar datos y decidir quién puede ver qué |
| Inicio de sesión | Firebase Authentication | Entrar con correo, con Google o como invitado |
| Mapas | MapLibre con teselas de OpenFreeMap | Mostrar el mapa y los parqueaderos |

> Este repositorio contiene **solo la app Android de conductores**. La app web de administración se trabaja en su propio repositorio.

### Principio de ParkTuluá que también afecta a esta app

Un conductor **no necesita cuenta** para usar el servicio. La cuenta es opcional. "Ingresar como invitado" abre una sesión anónima de Firebase, que **no** es un usuario de ParkTuluá. Por eso ninguna pantalla del flujo principal (consultar, reservar, ingresar, retirar) puede exigir una cuenta.

---

## 2. Herramientas que debe tener cada integrante

- **Android Studio** (versión estable más reciente).
- **JDK 17** (viene incluido con Android Studio).
- **Git** instalado y configurado con tu nombre y correo.
- Un celular Android con **Depuración USB** activada, o un emulador creado desde *Device Manager*.
- Acceso al repositorio y a los proyectos de Firebase y Supabase (te lo da el líder del grupo).

**Primera vez que abres el proyecto:** la sincronización de Gradle puede tardar varios minutos y fallar por red lenta. Si aparecen avisos de "Failed to resolve", ve a *File → Sync Project with Gradle Files* y repite hasta que termine sin errores.

---

## 3. Cómo empezar (clonar el repositorio)

1. Clona el repositorio:
   ```
   git clone https://github.com/USUARIO/parqueaderoapp.git
   ```
2. Abre **Android Studio → Open** y elige la carpeta `parqueaderoapp` (la que contiene `build.gradle.kts`).
3. Espera a que termine la sincronización de Gradle.
4. Cambia a la rama de trabajo:
   ```
   git checkout develop
   ```
5. Crea tu propia rama para tu tarea (ver sección 9).

---

## 4. Estructura del repositorio (raíz)

El proyecto de Android está **directamente en la raíz**. No existe una carpeta intermedia.

```
parqueaderoapp/
├── app/                        todo el código de la aplicación
│   ├── build.gradle.kts        configuración y librerías de la app
│   └── src/
│       ├── main/               el código real y los recursos
│       ├── test/               pruebas que no necesitan celular
│       └── androidTest/        pruebas que corren en celular o emulador
├── gradle/
│   ├── libs.versions.toml      lista central de versiones de librerías
│   └── wrapper/                NO se toca
├── build.gradle.kts            configuración general del proyecto
├── settings.gradle.kts         nombre del proyecto y módulos
├── gradle.properties           ajustes de Gradle
├── gradlew / gradlew.bat       NO se tocan
├── local.properties            datos de TU computador, NO se sube
└── README.md                   este documento
```

### Datos base del proyecto

| Dato | Valor |
|---|---|
| Nombre del repositorio | `parqueaderoapp` |
| Lenguaje | Kotlin |
| Interfaz | Jetpack Compose |
| Arquitectura | MVVM |
| Versión mínima de Android | API 24 (Android 8.0), según la ERS (RNF-15) |
| Paquete base | `com.univalle.parqueaderoapp` |

**Todos usamos exactamente el mismo paquete base.** No se cambia por cuenta propia.

---

## 5. Estructura del código y arquitectura MVVM

Todo el código vive dentro de `app/src/main/java/com/univalle/parqueaderoapp/`.

```
com.univalle.parqueaderoapp/
│
├── MainActivity.kt                 puerta de entrada de la app
│
├── core/                           lo que usa toda la app
│   └── network/                    observador de conexión a internet
│
├── data/                           el trabajo real con el exterior, una carpeta por módulo
│   ├── auth/                       AuthRepository y su implementación con Firebase
│   ├── parqueaderos/
│   ├── reserva/
│   ├── usuario/                    UsuarioRepository, configuración de Supabase, foto local
│   └── vehiculo/
│
└── ui/                             lo que el usuario ve, una carpeta por módulo
    ├── auth/                       login, registro y verificación (pantallas y AuthViewModel en `screen`)
    ├── mapa/                       pantalla del mapa
    ├── navigation/                 el mapa de pantallas (AppNavGraph)
    ├── reserva/
    ├── shared/                     piezas reutilizables: botones, inputs, logo, screens
    └── theme/                      colores, letras y estilos
```

> No hace falta crear todo desde el primer día. Cada módulo se crea cuando se necesita.

### Las tres piezas de MVVM

| Pieza | Qué es | Dónde vive | Qué hace |
|---|---|---|---|
| **Vista** (View) | Pantalla en Compose, por ejemplo `LoginScreen` | `ui/<módulo>` | Solo dibuja y avisa al ViewModel qué hizo el usuario |
| **ViewModel** | Por ejemplo `AuthViewModel` | `ui/<módulo>` | Guarda el estado de la pantalla y decide qué hacer. No conoce Compose ni Android visual |
| **Modelo** (Model) | Repositorios y datos | `data/<módulo>` | Habla con Supabase, Firebase o el celular |

### Qué va en cada carpeta (en palabras simples)

Piensa en un restaurante: cada carpeta es un área con un trabajo distinto.

| Carpeta | Analogía | Qué contiene |
|---|---|---|
| `ui` | El comedor | Pantallas y la lógica de cada pantalla (ViewModel) |
| `data` | La cocina | El código que realmente habla con Supabase, Firebase y el celular |
| `core` | Los servicios generales | Ayudas compartidas por toda la app |

### Reglas de dependencia (importante)

```
Screen  ->  ViewModel  ->  Repository (interfaz)  <-  implementación  ->  Supabase / Firebase
```

- La **pantalla** solo conoce a su `ViewModel`.
- El **ViewModel** solo conoce la **interfaz** del repositorio (por ejemplo `AuthRepository`), nunca a Supabase ni a Firebase directamente.
- La **interfaz** y su **implementación** viven juntas en `data/<módulo>` (por ejemplo `AuthRepository` y `FirebaseAuthRepository`).
- Hoy **no existe una capa `domain`**. Si en el futuro la lógica de negocio crece, se acuerda en el grupo antes de crearla.
- El ViewModel **no** llama a `Context` ni a clases visuales de Compose.

Si rompes estas reglas, el proyecto se vuelve un sancocho. Si dudas, pregunta.

### Cómo se crean las dependencias

Las dependencias se pasan **por el constructor** (por ejemplo, `AuthViewModel` recibe sus repositorios) y hoy se arman **a mano** con una fábrica (`AuthViewModelFactory`). No se crean repositorios dentro de la pantalla ni del ViewModel.

> **Decisión abierta:** el grupo evalúa adoptar Hilt para la inyección de dependencias. Hasta que se decida, no se mezclan frameworks y se sigue el esquema actual.

### Conexión con Firebase y Supabase

La persona inicia sesión con **Firebase**. Cuando la app necesita hablar con Supabase, envía el token de Firebase en la cabecera `Authorization` de la petición. La app usa **solo** la clave pública (`anon key`) de Supabase. La seguridad real la ponen las reglas **RLS** y las funciones de servidor.

### Mapas

- El mapa se dibuja con **MapLibre**. La URL del estilo del mapa no se escribe en las pantallas: va en una constante de configuración.
- MapLibre se usa **solo dentro del componente del mapa**, en `ui/mapa/components`. Ninguna otra pantalla importa clases de MapLibre.
- El ViewModel del mapa maneja coordenadas e identificadores, no objetos de MapLibre.
- La versión de MapLibre se fija exacta en `gradle/libs.versions.toml`.

---

## 6. Cómo crear la estructura en Android Studio

### 6.1 Crear los paquetes (carpetas de código)

1. En el panel izquierdo, abre la vista **Android** y despliega `kotlin+java`.
2. Clic derecho sobre `com.univalle.parqueaderoapp` (el que **no** dice `androidTest` ni `test`).
3. Elige **New → Package**.
4. Escribe el nombre completo, por ejemplo `ui.mapa`, y pulsa Enter.
5. Repite con cada paquete, **siempre haciendo clic derecho sobre `com.univalle.parqueaderoapp`** para que queden todos al mismo nivel.

Ejemplo para un módulo nuevo llamado `mapa`:

```
ui.mapa
ui.mapa.components
data.parqueaderos
```

> Android Studio puede mostrar las carpetas vacías juntas en una sola línea (por ejemplo `ui.mapa`). Es solo visual; al meter archivos se separan solas.
>
> **Importante:** Git no guarda carpetas vacías. Una carpeta aparece en el repositorio solo cuando tiene al menos un archivo adentro.

### 6.2 Crear un archivo

1. Clic derecho sobre el paquete donde va el archivo.
2. **New → Kotlin Class/File**.
3. Escribe el nombre respetando las reglas de la sección 7.
4. Elige el tipo correcto:

| Tipo | Cuándo usarlo |
|---|---|
| **File** | Pantallas y piezas visuales de Compose (son funciones `@Composable`) y archivos con funciones sueltas |
| **Class** | ViewModels, UiState, fábricas de ViewModel, modelos de datos |
| **Interface** | Los contratos de los repositorios (por ejemplo `AuthRepository`) |
| **Object** | Cosas que existen una sola vez (por ejemplo la configuración de Supabase) |

### 6.3 Carpetas que no van en `java`

- **Animación (Lottie, .json):** clic derecho sobre `res` → **New → Android Resource Directory** → tipo `raw`. Ahí se guarda el archivo.
- **Imágenes e íconos:** dentro de `res/drawable`.
- **Textos de la app:** en `res/values/strings.xml`. Ningún texto visible se escribe directo en el código.
- **`google-services.json`:** va dentro de la carpeta `app/`. **No se sube al repositorio** (ver sección 10).

---

## 7. Cómo se escriben los nombres

Estas son las únicas formas permitidas. Se llaman por su nombre técnico, pero aquí se explican en español.

| Forma | Cómo se escribe | Ejemplo |
|---|---|---|
| **PascalCase** | Cada palabra empieza con mayúscula, sin espacios ni guiones | `LoginScreen`, `UsuarioDto` |
| **camelCase** | La primera palabra en minúscula y las siguientes con mayúscula | `iniciarSesion`, `correoUsuario` |
| **snake_case** | Todo en minúscula separado por guion bajo | `ic_google`, `fondo_login` |
| **MAYÚSCULAS_CON_GUION_BAJO** | Para valores fijos que nunca cambian | `TIEMPO_ESPERA_SEGUNDOS` |
| **minúsculas pegadas** | Para paquetes: sin mayúsculas, sin guiones, sin guion bajo | `ui.auth` |

### Qué forma usar en cada caso

| Elemento | Forma | Ejemplo correcto | Ejemplo incorrecto |
|---|---|---|---|
| Archivo y clase | PascalCase | `LoginScreen.kt` | `loginscreen.kt`, `Login_Screen.kt` |
| Función | camelCase | `iniciarSesionConGoogle()` | `IniciarSesion()` |
| Variable | camelCase | `correoUsuario` | `correo_usuario`, `CorreoUsuario` |
| Constante | MAYÚSCULAS_CON_GUION_BAJO | `MAX_INTENTOS` | `maxIntentos` |
| Paquete | minúsculas pegadas | `ui.auth` | `Ui.Auth`, `ui_auth` |
| Recurso (imagen, XML) | snake_case | `ic_google.xml` | `IcGoogle.xml`, `ic-google.xml` |
| Función visual (Compose) | PascalCase | `BotonGoogle()` | `botonGoogle()` |

### Terminaciones obligatorias

Cada tipo de archivo termina con una palabra que dice qué es. Así cualquiera lo reconoce de un vistazo.

| Qué es | Termina en | Ejemplo | Carpeta |
|---|---|---|---|
| Pantalla completa | `Screen` | `LoginScreen.kt` | `ui/<módulo>` |
| Lógica de una pantalla | `ViewModel` | `AuthViewModel.kt` | `ui/<módulo>` |
| Estado de una pantalla | `UiState` | `MapaUiState.kt` | `ui/<módulo>` |
| Pieza visual pequeña | nombre descriptivo | `FiltroTipoChips.kt` | `ui/<módulo>/components` o `ui/shared` |
| Lista de acciones (contrato) | `Repository` | `AuthRepository.kt` | `data/<módulo>` |
| Código que cumple el contrato | tecnología + `Repository` | `FirebaseAuthRepository.kt` | `data/<módulo>` |
| Datos tal como llegan del servidor | `Dto` | `ParqueaderoDto.kt` | `data/<módulo>` |
| Creador de un ViewModel con dependencias | `ViewModelFactory` | `AuthViewModelFactory.kt` | junto a su ViewModel |
| Vigilante de un estado del sistema | `Observer` | `NetworkConnectivityObserver.kt` | `core/<tema>` |

### Idioma del código

- **Comentarios, documentación, mensajes de commit y textos de la app: en español.**
- Las terminaciones técnicas de la tabla anterior (`Screen`, `ViewModel`, `Repository`, etc.) se mantienen tal cual, porque son los términos que usa la propia herramienta y los reconoce cualquier programador de Android.
- Los nombres de variables y funciones propias del proyecto **deben ser claros y se escriben en un solo idioma por archivo**. No se mezclan.

---

## 8. Buenas prácticas

### Orden y claridad
1. **Una clase por archivo.** El nombre del archivo es el nombre de la clase.
2. **Cada cosa en su carpeta.** Si no sabes dónde va, pregunta antes de ponerlo "donde cabe".
3. **Nada de carpetas nuevas por gusto.** Si necesitas una, se acuerda con el grupo.
4. **Archivos cortos.** Si un archivo pasa de unas 200 líneas, probablemente hace demasiado: divídelo.
5. **Nombres que se entiendan solos.** `usuarioAutenticado` sí; `x`, `dato2`, `temp` no.

### Código limpio
6. **Un trabajo por función.** Si necesitas la palabra "y" para describirla, son dos funciones.
7. **Comenta el porqué, no el qué.** El código ya dice qué hace; el comentario explica por qué lo hiciste así.
8. **Cero código muerto.** No se deja código comentado "por si acaso". Para eso existe Git.
9. **Sin números ni textos "sueltos".** Los textos van en `strings.xml` y los valores fijos en constantes.
10. **Borra los imports que no uses** (*Code → Optimize Imports* o `Ctrl + Alt + O`).
11. **Formatea antes de subir** con `Ctrl + Alt + L`. Todos usamos el formato por defecto de Android Studio.

### Pantallas (Compose) y ViewModels
12. **La pantalla solo dibuja.** La lógica va en el `ViewModel`.
13. **Nada de llamadas a Supabase o Firebase dentro de una pantalla ni de un ViewModel.** Se hacen en `data`, a través de un repositorio.
14. **El ViewModel recibe sus repositorios por el constructor.** No los crea él mismo.
15. **Las piezas que se repiten van en `components`** (botones, campos de texto, tarjetas) o en `ui/shared` si las usan varios módulos.
16. **Todo color, tamaño de letra y forma sale del tema** (`ui/theme`). No se escribe un color suelto en la pantalla.
17. **Una pantalla no cambia su propio estado.** Avisa al ViewModel y observa el estado que este le entrega.

### Librerías y configuración
18. **Las versiones de librerías se declaran solo en `gradle/libs.versions.toml`.** No se escriben números de versión sueltos en `app/build.gradle.kts`.
19. **No se agrega una librería sin avisar.** Cada librería nueva afecta a todos.
20. **No se actualiza Gradle ni Android Studio a mitad de una tarea** aunque aparezca el aviso. Se acuerda y se hace en una rama aparte.

### Manejo de errores
21. **Toda llamada a internet puede fallar.** Siempre se contempla el caso de error y el de "sin conexión".
22. **El usuario nunca ve un mensaje técnico.** Se le muestra un texto claro en español.
23. **Siempre hay un estado de "cargando"** mientras se espera una respuesta.
24. **Toda pantalla que carga datos contempla tres estados:** cargando, error y vacío.

---

## 9. Trabajo con Git (para no pisarnos)

### Ramas

| Rama | Para qué |
|---|---|
| `main` | Código estable. **Nadie sube directo aquí.** |
| `develop` | Donde se junta el trabajo del equipo |
| `feature/nombre-corto` | Una por tarea. Ej.: `feature/login-google` |
| `fix/nombre-corto` | Para corregir errores. Ej.: `fix/animacion-login` |

Flujo: se crea la rama desde `develop` → se trabaja → se abre una solicitud de integración (*Pull Request*) → otro compañero la revisa → se une.

Para crear tu rama:
```
git checkout develop
git pull
git checkout -b feature/nombre-corto
```

### Mensajes de commit (en español)

Formato: `tipo: descripción corta en presente`

| Tipo | Cuándo |
|---|---|
| `nuevo` | Se agrega algo nuevo |
| `arreglo` | Se corrige un error |
| `mejora` | Se mejora algo que ya existía |
| `docs` | Cambios solo de documentación |
| `orden` | Se reorganizan archivos sin cambiar lo que hace |

Ejemplos buenos:
```
nuevo: pantalla de login con animación
arreglo: el botón de Google no respondía
orden: mover ParqueaderoDto a data/parqueaderos
```
Ejemplos malos: `cambios`, `listo`, `arreglé cosas`, `asdf`.

### Reglas
- **Un commit = un cambio con sentido.** No mezcles tres cosas en uno.
- **Actualiza tu rama antes de empezar a trabajar** (`git pull`).
- **Nunca subas código que no compila.** Prueba primero que la app abre.
- **Nunca uses `git push --force`** sin avisar al grupo.
- **Antes de hacer `git add .`, revisa `git status`.** Así ves qué se va a subir.
- **Cada tarea se enlaza con su issue de Linear** en la descripción del Pull Request.

---

## 10. Seguridad: lo que NUNCA se sube al repositorio

| Qué | Por qué | Qué hacer |
|---|---|---|
| `google-services.json` | Contiene datos del proyecto de Firebase | Agregarlo al `.gitignore`; se comparte por un canal privado |
| `local.properties` | Datos de tu computador | Debe estar en el `.gitignore` (ya viene así por defecto) |
| Carpetas `build/` y `.gradle/` | Archivos generados, pesan y estorban | Ya vienen ignoradas |
| Clave `service_role` de Supabase | Da control total sobre la base de datos | **Jamás va en la app.** Solo en servidores |
| Claves de Stripe y de otros servicios de pago o mapas | Permiten cobrar o consumir cupos a tu nombre | Solo en el servidor, nunca dentro de la app |
| Contraseñas, tokens, claves de API | Cualquiera podría usarlas | Se guardan en `local.properties` y se leen desde el código |
| Archivos `.jks` o `.keystore` | Son la firma de la app | Los guarda solo el líder del grupo |

En la app **solo** se usa la clave pública (`anon key`) de Supabase, y la seguridad real la ponen las reglas **RLS** en la base de datos.

Para comprobar que `local.properties` está ignorado:
```
git check-ignore -v local.properties
```
Si no responde nada, **no lo subas** y avisa al grupo.

---

## 11. Normas y estándares que debemos tener en cuenta

Estas normas no se aplican "de memoria": son la guía de por qué el proyecto se hace con orden.

| Norma | De qué trata | Cómo la aplicamos |
|---|---|---|
| **ISO/IEC 25010** | Calidad del software (funcionalidad, rendimiento, usabilidad, fiabilidad, seguridad, mantenibilidad, portabilidad) | Estructura por capas, nombres claros, manejo de errores y pantallas fáciles de usar |
| **ISO/IEC 27001** | Seguridad de la información | No subir claves, usar RLS, mínimos permisos por rol, no guardar datos sensibles sin necesidad |
| **ISO/IEC 12207** | Ciclo de vida del software (planear, desarrollar, probar, mantener) | Trabajo por ramas, revisión entre compañeros y documentación al día |
| **Ley 1581 de 2012 (Colombia)** | Protección de datos personales (Habeas Data) | Pedir solo los datos necesarios, informar para qué se usan y protegerlos. No se guarda la ubicación del usuario |

> Las normas ISO son estándares de referencia. No se "certifican" en un proyecto de formación, pero sí sirven como lista de buenas prácticas y se pueden citar en la documentación del proyecto.

---

## 12. Lista de revisión antes de subir tu trabajo

Revisa cada punto antes de hacer `push`:

- [ ] La app compila y abre sin errores.
- [ ] Cada archivo está en su carpeta correcta.
- [ ] Los nombres siguen las reglas de la sección 7.
- [ ] No hay código comentado ni imports sin usar.
- [ ] No hay textos escritos directo en las pantallas (están en `strings.xml`).
- [ ] Ni la pantalla ni el ViewModel llaman directo a Supabase ni a Firebase.
- [ ] El ViewModel recibe sus repositorios por el constructor.
- [ ] Se contemplan los estados de cargando, error y vacío.
- [ ] `git status` no muestra ningún archivo de la lista de la sección 10.
- [ ] El mensaje del commit sigue el formato en español.
- [ ] Trabajé en mi rama, no en `main`.
- [ ] El Pull Request enlaza su issue de Linear.

---

## 13. Estado actual y orden de trabajo

### Estado de los módulos

| Módulo | Estado |
|---|---|
| `auth` (correo, Google, invitado, registro y verificación) | Implementado |
| `mapa` | En desarrollo |
| `reserva` | En desarrollo |
| Los demás módulos | Se crean cuando se necesiten |

### Cómo agregar un módulo nuevo (receta MVVM)

1. Crear el paquete `ui.<módulo>` (y `ui.<módulo>.components` si lo necesita).
2. Crear el `UiState` del módulo (tipo Class).
3. Crear el `ViewModel` (tipo Class), que recibe sus repositorios por el constructor.
4. Crear la pantalla `Screen` (tipo File), que solo dibuja y observa el estado.
5. Si el módulo necesita datos externos, crear en `data.<módulo>` la interfaz del repositorio y su implementación.
6. Registrar la ruta de la pantalla en `AppNavGraph`.
7. Agregar los textos a `strings.xml`.
8. Probar en un celular y revisar la lista de la sección 12.

Cada paso se prueba antes de pasar al siguiente. Si algo falla, sabemos exactamente dónde mirar.

---

## 14. Preguntas frecuentes

**¿Dónde pongo una pantalla nueva?**
En `ui/nombre-del-módulo/`. Si el módulo no existe, se acuerda con el grupo.

**¿Dónde pongo el código que habla con Supabase?**
En `data/nombre-del-módulo/`, como un repositorio. Nunca dentro de una pantalla ni de un ViewModel.

**¿Puedo meter una librería nueva?**
No sin avisar. Se propone en el grupo y se agrega entre todos en `libs.versions.toml`.

**¿El código se ve en rojo después de abrir el proyecto?**
Casi siempre es que Gradle no terminó de descargar las librerías. Sincroniza de nuevo (*File → Sync Project with Gradle Files*).

**¿Puedo cambiar el nombre del paquete?**
No. Todos usamos el mismo paquete base.

**Creé una carpeta pero no aparece en GitHub.**
Git no sube carpetas vacías. Aparecerá cuando tenga un archivo adentro.

---

*Este documento se actualiza cuando el grupo acuerda un cambio. Si propones una regla nueva, se discute y se agrega aquí.*
