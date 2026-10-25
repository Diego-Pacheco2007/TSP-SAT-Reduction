# Reducción Polinomial: TSP a SAT

Este proyecto demuestra experimentalmente el Teorema de Cook-Levin reduciendo el Problema del Viajante (TSP) en su versión de decisión al problema de Satisfacibilidad Booleana (SAT). El código está desarrollado en Java 11 y delega la resolución algorítmica exclusivamente al SAT-Solver externo.

## Requisitos y Dependencias
Para compilar y ejecutar este proyecto, el entorno debe contar con:
* **Java Development Kit (JDK):** Versión 11 o superior.
* **Librería externa:** `org.ow2.sat4j.core-2.3.6.jar` (Sat4j Core).

## Configuración de Dependencias en Apache NetBeans
1. Clonar este repositorio y abrir el proyecto `TSP_SAT` en Apache NetBeans.
2. En la pestaña **Projects**, hacer clic derecho en la carpeta **Libraries** > **Add JAR/Folder**.
3. Seleccionar el archivo `.jar` de Sat4j (ubicado en la carpeta `lib/` si se incluyó, o descargado previamente).
4. Verificar que el IDE reconozca la librería mediante un "Clean and Build".

## Estructura del Archivo de Entrada
El programa requiere un archivo de texto plano (ej. `grafo.txt`) en el directorio raíz. El formato estructurado debe ser:
* **Línea 1:** Número de nodos ($N$).
* **Siguientes líneas:** Matriz de adyacencia/distancias separada por espacios.

## Ejecución y Pruebas
Por diseño del motor de pruebas, las variables de ejecución están parametrizadas en la clase `Main.java` para su rápida evaluación en el IDE.

Para ejecutar una prueba:
1. Asegurarse de que el archivo `grafo.txt` contiene la matriz deseada.
2. Abrir `Main.java` y ajustar la variable `limiteK` al umbral de costo máximo deseado.
3. Ejecutar el archivo (`Shift + F6` en NetBeans).

**Salida esperada:**
El sistema procesará las cláusulas CNF DIMACS para "Visita Única" y "Ocupación Única". 
* Si existe un ciclo hamiltoniano, el solver imprimirá `SATISFIABLE (SAT)`, revelando el modelo booleano mapeado a la ruta.
* Si no existe, imprimirá `UNSATISFIABLE (UNSAT)`.

## Consideraciones del Modelado
Cumpliendo con los requerimientos técnicos de la práctica, **no se utiliza fuerza bruta, backtracking ni programación dinámica**. La lógica proposicional generada delega toda la inferencia computacional a la librería Sat4j.
