# Bug Report

Se reportan los siguientes incidentes en la aplicación. Por favor investigar y corregir.

La aplicación compila, instala y arranca con normalidad. La suite de tests unitarios
(`./gradlew test`) pasa completa: ninguno de estos incidentes está cubierto por ella, así que
no hay ningún test rojo que los delate. Todos los errores se reproducen en runtime.

---

### 1. El botón "Retry" no recupera la lista

Al abrir la app sin conexión, la lista muestra un mensaje de error y un botón "Retry".
Al recuperar la conexión y presionar el botón, la red responde bien y la app **sí recibe los
datos** (se puede ver en el log de OkHttp: `<-- 200 .../users`), pero la pantalla sigue
mostrando el mismo mensaje de error. La lista nunca aparece.

La única forma de volver a ver la lista es cerrar la app por completo y abrirla de nuevo.

---

### 2. El email y el nombre de la compañía aparecen intercambiados

Tanto en la lista como en la pantalla de detalle, el campo de email muestra el nombre de la
compañía y el campo de nombre de compañía muestra el email. Por ejemplo, en la ficha de
"Leanne Graham" el email aparece como "Romaguera-Crona" y la compañía como "Sincere@april.biz".

El resto de los datos (nombre, username, teléfono, website, dirección, catch phrase) se ven
correctos. Ocurre con los 10 usuarios.

---

### 3. El detalle siempre muestra el usuario "Leanne Graham"

Al tocar cualquier usuario de la lista se abre la pantalla de detalle, pero el contenido
corresponde siempre al usuario con id 1 ("Leanne Graham"), sin importar cuál se haya tocado.
Se reproduce con los 10 usuarios, incluso después de reiniciar la app.

---

### 4. El botón "atrás" no regresa a la lista

Al entrar al detalle y presionar el botón de atrás (el de la barra superior o el gesto del
sistema), la lista aparece por una fracción de segundo y la app vuelve a abrir el detalle del
mismo usuario automáticamente. Es prácticamente imposible quedarse en la lista: cada intento de
volver termina otra vez en el detalle.
