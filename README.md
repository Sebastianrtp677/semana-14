# ProyectoFireBase (Actividad 3.2 - Firestore)

App Android en **Java** (paquete `com.stomas.proyectofirebase`): formulario de ingreso de mascotas que envía los datos
a **Cloud Firestore** (colección `mascotas`) y los muestra en una lista.

## Pasos
1. Android Studio > File > Open > carpeta `ProyectoFireBase`. Sincroniza Gradle (icono del elefante).
2. https://console.firebase.google.com > Agregar proyecto (nombre libre) > continuar > aceptar condiciones.
3. Icono de Android: nombre del paquete `com.stomas.proyectofirebase` > Registrar app.
4. Descargar `google-services.json` y copiarlo en la carpeta **app/** (vista "Project"). Sincronizar de nuevo.
5. Compilación > **Firestore Database** > Crear base de datos > opciones por defecto > modo producción.
6. Pestaña **Reglas**, reemplazar y pulsar **Publicar** (solo para la clase; permite acceso abierto):

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /mascotas/{documento} {
      allow read, write: if true;
    }
  }
}
```
7. Ejecutar la app, llenar el formulario, **Enviar datos** y **Cargar datos**. Verifica el registro en Firestore.

## Si falla la compilación
- "no existe google-services.json": falta el paso 4.
- Error de compileSdk: ya viene en 34 (el cambio de 33 a 34 que pide la pauta).

## Git
```
git add .
git commit -m "Actividad 3.2: Firestore con formulario de mascotas"
git push
```
