# Parqueadero App – Aplicación de Conductores (Android / Kotlin)

Guía oficial del equipo para crear, ordenar y mantener este repositorio.
**Léela completa antes de escribir la primera línea de código.** Si algo no está claro, se pregunta en el grupo antes de improvisar.

---

## 1. ¿Qué estamos construyendo?

Una aplicación móvil para **conductores** hecha en **Kotlin** con **Jetpack Compose** (la forma moderna de dibujar pantallas en Android).

| Pieza | Tecnología | Para qué sirve |
|---|---|---|
| App de conductores (**este repositorio**) | Kotlin + Jetpack Compose | Lo que ve y usa el conductor |
| App de administradores | React Native (proyecto aparte) | Lo que usa el administrador |
| Base de datos y roles | Supabase | Guardar datos y decidir quién puede ver qué |
| Inicio de sesión con Google | Firebase Authentication | Entrar con la cuenta de Google |

> Este repositorio contiene **solo la app Android de conductores**. La app de administradores se trabaja en su propio proyecto.

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
├── app/                        ← aquí vive todo el código de la aplicación
│   ├── build.gradle.kts        ← configuración y librerías de la app
│   └── src/
│       ├── main/               ← el código real y los recursos
│       ├── test/               ← pruebas que no necesitan celular
│       └── androidTest/        ← pruebas que corren en celular o emulador
├── gradle/
│   ├── libs.versions.toml      ← lista central de versiones de librerías
│   └── wrapper/                ← NO se toca
├── build.gradle.kts            ← configuración general del proyecto
├── settings.gradle.kts         ← nombre del proyecto y módulos
├── gradle.properties           ← ajustes de Gradle
├── gradlew / gradlew.bat       ← NO se tocan
├── local.properties            ← datos de TU computador, NO se sube
└── README.md                   ← este documento
```

### Datos base del proyecto

| Dato | Valor |
|---|---|
| Nombre del repositorio | `parqueaderoapp` |
| Lenguaje | Kotlin |
| Interfaz | Jetpack Compose |
| Versión mínima de Android | API 24 |
| Paquete base | El que aparece en la **primera línea** de `MainActivity.kt` (empieza con `package ...`) |

**Todos usamos exactamente el mismo paquete base.** No se cambia por cuenta propia. En el resto de este documento se le llama **[paquete base]**.

---

## 5. Estructura del código

Todo el código vive dentro de `app/src/main/java/[paquete base]/`.

```
[paquete base]/
│
├── MainActivity.kt                 ← puerta de entrada de la app
├── ParqueaderoApp.kt               ← clase principal de la aplicación
│
├── core/                           ← lo que usa toda la app
│   ├── di/                         ← aquí se "arman" las conexiones
│   ├── navigation/                 ← el mapa de pantallas
│   ├── ui/theme/                   ← colores, letras y estilos
│   └── util/                       ← ayudas pequeñas reutilizables
│
├── data/                           ← el trabajo real con el exterior
│   ├── remote/                     ← conexión con Supabase y Google
│   ├── model/                      ← datos tal como llegan del servidor
│   └── repository/                 ← código que cumple lo prometido en domain
│
├── domain/                         ← las reglas del negocio
│   ├── model/                      ← los objetos de la app (Usuario, etc.)
│   ├── repository/                 ← lista de acciones disponibles (solo promesas)
│   └── usecase/                    ← una acción concreta por archivo
│
└── feature/                        ← lo que el usuario ve, una carpeta por módulo
    ├── auth/                       ← login y registro
    │   └── components/             ← piezas del login (animación, botón de Google)
    ├── home/
    ├── parking/
    └── profile/
```

> No hace falta crear todo desde el primer día. Se empieza con `feature/auth`, `core/navigation` y `core/ui/theme`, y el resto se crea cuando se necesite.

### Qué va en cada carpeta (en palabras simples)

Piensa en un restaurante: cada carpeta es un área con un trabajo distinto.

| Carpeta | Analogía | Qué contiene |
|---|---|---|
| `feature` | El comedor | Pantallas y su lógica |
| `domain` | El menú | Qué se puede hacer, sin decir cómo |
| `data` | La cocina | El código que realmente habla con Supabase y Google |
| `core` | Los servicios generales | Navegación, estilos, conexiones compartidas |

### Reglas de dependencia (importante)

```
feature  →  domain  ←  data
```

- `feature` **puede** usar `domain`.
- `data` **puede** usar `domain`.
- `domain` **no usa** `feature` ni `data` (no sabe quién lo llama).
- `feature` **nunca** habla directo con Supabase ni con Firebase. Siempre pasa por `domain`.

Si rompes esta regla, el proyecto se vuelve un sancocho. Si dudas, pregunta.

---

## 6. Cómo crear la estructura en Android Studio

### 6.1 Crear los paquetes (carpetas de código)

1. En el panel izquierdo, abre la vista **Android** y despliega `kotlin+java`.
2. Clic derecho sobre el paquete base (el que **no** dice `androidTest` ni `test`).
3. Elige **New → Package**.
4. Escribe el nombre completo, por ejemplo `core.di`, y pulsa Enter.
5. Repite con cada uno de la lista, **siempre haciendo clic derecho sobre el paquete base** para que queden todos al mismo nivel:

```
core.di
core.navigation
core.util
data.remote
data.model
data.repository
domain.model
domain.repository
domain.usecase
feature.auth
feature.auth.components
```

> Android Studio puede mostrar las carpetas vacías juntas en una sola línea (por ejemplo `core.di`). Es solo visual; al meter archivos se separan solas.
>
> **Importante:** Git no guarda carpetas vacías. Una carpeta aparece en el repositorio solo cuando tiene al menos un archivo adentro.

### 6.2 Crear un archivo

1. Clic derecho sobre el paquete donde va el archivo.
2. **New → Kotlin Class/File**.
3. Escribe el nombre respetando las reglas de la sección 7.
4. Elige el tipo correcto:

| Tipo | Cuándo usarlo |
|---|---|
| **Class** | Pantallas con lógica, ViewModels, modelos de datos |
| **Interface** | Los contratos de `domain/repository` |
| **File** | Archivos que solo tienen funciones sueltas |
| **Object** | Cosas que existen una sola vez |

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
| **PascalCase** | Cada palabra empieza con mayúscula, sin espacios ni guiones | `LoginScreen`, `UserDto` |
| **camelCase** | La primera palabra en minúscula y las siguientes con mayúscula | `iniciarSesion`, `correoUsuario` |
| **snake_case** | Todo en minúscula separado por guion bajo | `ic_google`, `fondo_login` |
| **MAYÚSCULAS_CON_GUION_BAJO** | Para valores fijos que nunca cambian | `TIEMPO_ESPERA_SEGUNDOS` |
| **minúsculas pegadas** | Para paquetes: sin mayúsculas, sin guiones, sin guion bajo | `feature.auth` |

### Qué forma usar en cada caso

| Elemento | Forma | Ejemplo correcto | Ejemplo incorrecto |
|---|---|---|---|
| Archivo y clase | PascalCase | `LoginScreen.kt` | `loginscreen.kt`, `Login_Screen.kt` |
| Función | camelCase | `iniciarSesionConGoogle()` | `IniciarSesion()` |
| Variable | camelCase | `correoUsuario` | `correo_usuario`, `CorreoUsuario` |
| Constante | MAYÚSCULAS_CON_GUION_BAJO | `MAX_INTENTOS` | `maxIntentos` |
| Paquete | minúsculas pegadas | `feature.auth` | `Feature.Auth`, `feature_auth` |
| Recurso (imagen, XML) | snake_case | `ic_google.xml` | `IcGoogle.xml`, `ic-google.xml` |
| Función visual (Compose) | PascalCase | `BotonGoogle()` | `botonGoogle()` |

### Terminaciones obligatorias

Cada tipo de archivo termina con una palabra que dice qué es. Así cualquiera lo reconoce de un vistazo.

| Qué es | Termina en | Ejemplo | Carpeta |
|---|---|---|---|
| Pantalla completa | `Screen` | `LoginScreen.kt` | `feature/auth` |
| Lógica de una pantalla | `ViewModel` | `LoginViewModel.kt` | `feature/auth` |
| Estado de una pantalla | `UiState` | `LoginUiState.kt` | `feature/auth` |
| Pieza visual pequeña | nombre descriptivo | `GoogleButton.kt` | `feature/auth/components` |
| Una acción del negocio | `UseCase` | `SignOutUseCase.kt` | `domain/usecase` |
| Lista de acciones (promesa) | `Repository` | `AuthRepository.kt` | `domain/repository` |
| Código que cumple la promesa | `RepositoryImpl` | `AuthRepositoryImpl.kt` | `data/repository` |
| Datos tal como llegan del servidor | `Dto` | `UserDto.kt` | `data/model` |
| Armado de conexiones | `Module` | `SupabaseModule.kt` | `core/di` |

### Idioma del código

- **Comentarios, documentación, mensajes de commit y textos de la app: en español.**
- Las terminaciones técnicas de la tabla anterior (`Screen`, `ViewModel`, `UseCase`, etc.) se mantienen tal cual, porque son los términos que usa la propia herramienta y los reconoce cualquier programador de Android.
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

### Pantallas (Compose)
12. **La pantalla solo dibuja.** La lógica va en el `ViewModel`.
13. **Nada de llamadas a Supabase o Firebase dentro de una pantalla.**
14. **Las piezas que se repiten van en `components`** (botones, campos de texto, tarjetas).
15. **Todo color, tamaño de letra y forma sale del tema** (`core/ui/theme`). No se escribe un color suelto en la pantalla.

### Librerías y configuración
16. **Las versiones de librerías se declaran solo en `gradle/libs.versions.toml`.** No se escriben números de versión sueltos en `app/build.gradle.kts`.
17. **No se agrega una librería sin avisar.** Cada librería nueva afecta a todos.
18. **No se actualiza Gradle ni Android Studio a mitad de una tarea** aunque aparezca el aviso. Se acuerda y se hace en una rama aparte.

### Manejo de errores
19. **Toda llamada a internet puede fallar.** Siempre se contempla el caso de error y el de "sin conexión".
20. **El usuario nunca ve un mensaje técnico.** Se le muestra un texto claro en español.
21. **Siempre hay un estado de "cargando"** mientras se espera una respuesta.

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
orden: mover UserDto a data/model
```
Ejemplos malos: `cambios`, `listo`, `arreglé cosas`, `asdf`.

### Reglas
- **Un commit = un cambio con sentido.** No mezcles tres cosas en uno.
- **Actualiza tu rama antes de empezar a trabajar** (`git pull`).
- **Nunca subas código que no compila.** Prueba primero que la app abre.
- **Nunca uses `git push --force`** sin avisar al grupo.
- **Antes de hacer `git add .`, revisa `git status`.** Así ves qué se va a subir.

---

## 10. Seguridad: lo que NUNCA se sube al repositorio

| Qué | Por qué | Qué hacer |
|---|---|---|
| `google-services.json` | Contiene datos del proyecto de Firebase | Agregarlo al `.gitignore`; se comparte por un canal privado |
| `local.properties` | Datos de tu computador | Debe estar en el `.gitignore` (ya viene así por defecto) |
| Carpetas `build/` y `.gradle/` | Archivos generados, pesan y estorban | Ya vienen ignoradas |
| Clave `service_role` de Supabase | Da control total sobre la base de datos | **Jamás va en la app.** Solo en servidores |
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
| **Ley 1581 de 2012 (Colombia)** | Protección de datos personales (Habeas Data) | Pedir solo los datos necesarios, informar para qué se usan y protegerlos |

> Las normas ISO son estándares de referencia. No se "certifican" en un proyecto de formación, pero sí sirven como lista de buenas prácticas y se pueden citar en la documentación del proyecto.

---

## 12. Lista de revisión antes de subir tu trabajo

Revisa cada punto antes de hacer `push`:

- [ ] La app compila y abre sin errores.
- [ ] Cada archivo está en su carpeta correcta.
- [ ] Los nombres siguen las reglas de la sección 7.
- [ ] No hay código comentado ni imports sin usar.
- [ ] No hay textos escritos directo en las pantallas (están en `strings.xml`).
- [ ] La pantalla no llama directo a Supabase ni a Firebase.
- [ ] Se contemplan los casos de error y de carga.
- [ ] `git status` no muestra ningún archivo de la lista de la sección 10.
- [ ] El mensaje del commit sigue el formato en español.
- [ ] Trabajé en mi rama, no en `main`.

---

## 13. Orden de trabajo recomendado

1. Proyecto abierto y corriendo en el celular o emulador.
2. Paquetes iniciales de la sección 6.1 creados.
3. Pantalla de login con la animación (sin conectar nada todavía).
4. Proyecto de Firebase creado y `google-services.json` en `app/` (ignorado por Git).
5. Proyecto de Supabase creado y conexión lista.
6. Login con Google funcionando de principio a fin.
7. Después, las demás pantallas: `home`, `parking`, `profile`.

Cada paso se prueba antes de pasar al siguiente. Si algo falla, sabemos exactamente dónde mirar.

---

## 14. Preguntas frecuentes

**¿Dónde pongo una pantalla nueva?**
En `feature/nombre-del-modulo/`. Si el módulo no existe, se acuerda con el grupo.

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
