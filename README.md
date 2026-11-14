# Heavy Tools (Fabric 1.21.1)

Martillos (minan 3x3), grandes espadas y hachas pesadas en madera, piedra, hierro, diamante y netherita.

## Cómo compilar
1. Instala **JDK 21** y abre la carpeta en IntelliJ IDEA (importa `build.gradle`).
2. Si no tienes el wrapper de Gradle, desde la carpeta ejecuta: `gradle wrapper --gradle-version 8.10`
   (o genera una plantilla en https://fabricmc.net/develop/template/ y copia `src/` encima).
3. `./gradlew build` -> el .jar queda en `build/libs/heavytools-1.0.0.jar`
4. `./gradlew runClient` para probarlo en el juego.

## Cambiar el nombre/paquete
- `fabric.mod.json`: `id`, `name`, `authors` y el entrypoint.
- Si cambias el id `heavytools`, renómbralo también en: carpetas `assets/heavytools` y `data/heavytools`,
  `HeavyTools.MOD_ID`, los JSON de recetas/modelos/tags/lang.

## Notas
- Las texturas son marcadores de posición 16x16 generados por código: reemplázalos por tu arte.
- Agacharse con el martillo = minar un solo bloque.
