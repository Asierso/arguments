
#  <img src="/resources/mono-logo.png" width="25" alt="logo"> Arguments
Imagina una red social donde los debates no quedan en el aire, donde cada argumento cuenta y siempre hay un ganador. Arguments no es solo un espacio para discutir, es la arena definitiva para los que aman el debate y la lógica.

## 🧐 ¿En que consiste?
Un usuario lanza un tema, expone su postura y el debate comienza. Otros entran en la discusión, ya sea para reforzar su argumento o desafiarlo con lógica y hechos. Pero aquí no se trata solo de hablar… se trata de ganar.

- 📢 Abre un hilo con un tema y desarrolla tu postura.
- ⚖️ Otros usuarios entran: ¿te apoyan o te desafían?
- 🗳️ La comunidad vota: ¿quién argumentó mejor?
- 🏆 El ganador gana puntos de experiencia y sube de rango, ganando estatus por el camino

Cada interacción cuenta, cada argumento pesa. Al final, solo una postura prevalecerá. 
¿Tienes lo necesario para convencer y salir victorioso? 

## 🛠️ Configuración del .env
El archivo base de configuración del orquestador ".env" debe de tener la siguiente estructura:
```properties
#Spring configuration
SPRING_PORT=24833
SPRING_MONGODB_URI=<URI mongo>

#MongoDB configuration
MONGO_INITDB_ROOT_USERNAME=<usuario mongo>
MONGO_INITDB_ROOT_PASSWORD=<password mongo>
MONGO_INITDB_DATABASE=arguments

#Ollama port
OLLAMA_PORT=11434
```

## 🚀 Despliegue del servidor
1. Clonar el repositorio
2. Lanzar orquestador de contenedores ejecutando el comando `docker-compose up -d` en la raíz del proyecto
3. Espere a que los contenedores estén levantados. Esta acción podría tardar hasta 10 minutos
4. Para ver el token de acceso, ejecute `docker exec -it arguments-backend /bin/bash` y luego `cat token.txt`. Copie el token ya que será obligatorio para realizar cualquier petición con el servicio. Tambien puede revisarlo desde los logs, en la línea que empiece por "Client token: [tu token]".

>[!TIP]
> Tenga en cuenta que si quiere que la aplicación funcione con su versión del back-ennd de Arguments, debe de cambiar la URL a la cual la aplicación intentará conectarse así como el token. Esta configuración puede encontrarla en `android/app/src/main/java/com/asier/arguments/utils/Globals.kt`

### 🔧 Comprobaciones
1. Para comprobar que el servicio de Spring Boot está funcionando correctamente, revise que el Swagger esté operativo. Para esto entre en ["http://host:puerto/swagger-ui.html"]()
2. En caso de error puede acceder al logging del contenedor de Arguments usando `docker logs arguments-backend`
3. Compruebe que Ollama tiene los modelos pulleados. Para el funcionamiento del procesador de mensajes (Paimon), se requiere que al menos un modelo esté pulleado en el contenedor de Ollama. Por defecto se pullea la versión `llama3.2:3b`. Para poder comprobarlo, ejecute `docker exec -it ollama /bin/bash`y a continuación `ollama list`. Si no apareciera el modelo en la lista puede forzar su instalación manual con `ollama pull llama3.2:3b`

## 🫆 Entorno de pruebas
Para levantar un entorno de pruebas, el cual permitirá ejecutar la aplicación de Spring Boot sin tener que contenerla en un contenedor de Docker, siga los siguientes pasos
1. En `arguments.properties` y `application.properties`asegúrese de que las líneas de local tests están descomentadas y las líneas de deploy comentadas. Las líneas de configuración para despliegue se pueden identificar porque hacen referencia al nombre de la red creada por compose en su correspondiente YAML
2. Ejecutar el fichero `fastenv.sh`. Asegurese de que tenga permisos para ejecutarse
3. Ya tiene el entorno de pruebas preparado. Puede ejecutar el proyecto Spring Boot sin problema. Recuerde que en el entorno de pruebas, el servicio se lanza en el puerto 8088. Siga la guía de comprobaciones por si existiera algún fallo

## ✍️ Cierre del Proyecto – Versión 1.0 
Después de meses de trabajo, esfuerzo, insomnio y alma puesta en cada línea, la RC ya está lista.
Y aquí está, el final.
Un proyecto que no solo fue un desafío, sino una experiencia.
Aquí no solo se integran ramas.
Aquí se integra todo lo que fui para lograr esto.
Un backend blindado, un frontend que aguantó, y yo... que resistí hasta el final.
Gracias por cada error que me enseñó.
Gracias por cada test que me templó.
Y gracias a mí mismo por no soltar.
Este proyecto ya no es solo código.
Es mi historia compilada.