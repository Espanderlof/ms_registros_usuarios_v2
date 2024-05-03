# DESARROLLO FULLSTACK I_001A - Ejercicio A - ms_registros_usuarios_v2
Semana 9 - Microservicio a. - Registros usuarios.

Perfecto, veo que tienes tu proyecto en un repositorio de GitHub. Aquí te explico paso a paso cómo puedes usar Docker Playground para clonar tu repositorio, construir y ejecutar tus contenedores, y hacer pruebas:

## Accede a Docker Playground:
Visita la página web de Docker Playground en https://labs.play-with-docker.com/.
Haz clic en el botón "Start" para comenzar una nueva sesión.

## Crea una nueva instancia:
En la página principal de Docker Playground, haz clic en "Add New Instance" para crear una nueva instancia de Docker.
Espera unos segundos hasta que la instancia esté lista y se muestre el terminal en tu navegador.

## Clona tu repositorio de GitHub:
En el terminal de la instancia de Docker Playground, ejecuta el siguiente comando para clonar tu repositorio:

git clone https://github.com/Espanderlof/ms_registros_usuarios_v2.git

Espera a que se complete la clonación del repositorio.

## Navega al directorio del proyecto:
Después de clonar el repositorio, navega al directorio del proyecto ejecutando el siguiente comando:

cd ms_registros_usuarios_v2

## Cambia a la rama "sem9" (si es necesario):
Si tu proyecto se encuentra en una rama específica, en este caso "sem9", asegúrate de cambiar a esa rama ejecutando el siguiente comando:

git checkout sem9

## Construye y ejecuta tus contenedores:
Asegúrate de que tus archivos Dockerfile y docker-compose.yml se encuentren en el directorio actual.
Ejecuta el siguiente comando para construir y ejecutar tus contenedores en segundo plano:

docker-compose up

Espera a que Docker Compose descargue las imágenes necesarias, construya los contenedores y los ponga en marcha.