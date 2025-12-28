# RMViewer

## Introducción

Esta aplicación permite al usuario gestionar y hacer seguimiento de episodios de la serie Rick y Morty obtenidos desde su API pública,
ofreciendo una experiencia sencilla y organizada.  
El propósito principal es proporcionar un sistema centralizado donde el usuario pueda:
- Autenticarse de forma segura.
- Consultar episodios obtenidos desde una API externa.
- Filtrar, marcar episodios como vistos y llevar un control estadístico.
- Personalizar ajustes locales de la app.

---

## Características principales

- Autenticación de usuarios
    - Registro e inicio de sesión mediante Firebase Authentication (Email/Password).

- Listado de episodios
    - Obtención de episodios desde la API REST usando Retrofit.
    - Visualización en RecyclerView.

- Detalle de episodio
    - Información del episodio y listado de personajes asociados.

- Marcado de episodios como vistos
    - Persistencia por usuario usando Firebase Firestore.
    - Indicador visual de episodios vistos.

- Filtros
    - Mostrar todos los episodios o solo los marcados como vistos.

- Estadísticas
    - Total de episodios vistos.
    - Porcentaje de progreso.

- Ajustes
    - Cambio de idioma.
    - Cambio de tema (claro / oscuro).

- Persistencia local
    - Uso de SharedPreferences para ajustes de usuario.

---

## Tecnologías utilizadas

- Lenguaje: Kotlin
- Arquitectura: Activities + Fragments
- UI:
    - RecyclerView
    - Material Components
    - ViewBinding
- Red:
    - Retrofit
    - Gson
- Backend:
    - Firebase Authentication
    - Firebase Firestore
- Persistencia local:
    - SharedPreferences
- API externa:
    - Rick and Morty API  
      https://rickandmortyapi.com/api

---

## Instrucciones de uso

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/RMViewer.git
2. Abrir el proyecto
Abrir Android Studio.

Seleccionar Open an existing project.

Elegir la carpeta del proyecto.

3. Configurar Firebase
Acceder a https://console.firebase.google.com

Crear un nuevo proyecto.

Añadir una aplicación Android.

Descargar el archivo google-services.json.

Colocar el archivo en el proyecto en la ruta:


app/google-services.json
Activar el proveedor Email/Password en Firebase Console.

Conclusiones del desarrollador
En el desarrollo de esta tarea he aprendido distintos conceptos del desarrollo Android:

La autenticación de usuarios.

Peticiones HTTP para el consumo de APIs REST usando Retrofit.

La persistencia de datos en Firestore.

He reforzado conocimientos sobre RecyclerView, adapter, fragmentos, views y Kotlin.

Dificultades
Esta ha sido la tarea a la que he tenido que dedicar más tiempo, con bastante diferencia.
Enfrentarme a tantos conceptos nuevos no ha sido fácil. He tenido que dedicar tiempo a practicar código Kotlin para poder ver las cosas con más claridad.

No ha sido fácil encontrar información de calidad que ayudara a completar la tarea. Por eso, en alguna ocasión, he tenido que recurrir a la IA en modo “Estudiar y aprender” para comprender mejor algunos conceptos.

Para mí, lo más difícil ha sido combinarlo todo y saber dónde y cómo aplicar cada elemento.