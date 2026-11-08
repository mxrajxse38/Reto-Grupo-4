RETO PRÁCTICO INTEGRADOR SJA-UTA
APE 3 · Árbol general y árbol binario de búsqueda · Grupo 4
1. Enunciado
La Universidad Técnica de Ambato requiere completar y comprobar el Sistema Jerárquico Académico UTA (SJA-UTA), desarrollado como aplicación de consola en Java. El sistema representa la organización académica mediante un árbol general y administra a los estudiantes de cada paralelo mediante un árbol binario de búsqueda (ABB), ordenado por número de matrícula.
A partir del código base entregado, cada equipo deberá implementar las operaciones solicitadas, integrar los cambios en el menú y demostrar que la estructura académica, los registros estudiantiles y las reglas de negocio mantienen su consistencia después de realizar modificaciones, eliminaciones y traslados.
2. Punto de partida y condiciones
•	Utilizar el proyecto Java SJA-UTA proporcionado, conservando los paquetes enums, modelo, negocio y app, así como las clases y firmas existentes.
•	La jerarquía inicial contiene UTA → FCHE → EB → EB-N1, con los paralelos EB-N1-A y EB-N1-B.
•	EB-N1-A tiene inicialmente nueve matrículas, insertadas en este orden: 30, 15, 45, 10, 20, 40, 50, 35 y 55. EB-N1-B comienza vacío.
•	Los estudiantes iniciales tienen promedio 8.00. Para las pruebas de actualización se modificará únicamente el promedio indicado en el reto.
•	No utilizar TreeMap ni TreeSet para sustituir los árboles. Mantener nodos propios, operaciones recursivas cuando corresponda y validaciones en la capa de negocio.
•	La altura se expresa en aristas: un árbol con un único nodo tiene altura 0 y un árbol vacío, −1.
3. Actividades que deben desarrollar
Misión 1. Jerarquía y ruta institucional
•	En ArbolOrganizacional.java, registrar bajo la facultad FCHE la carrera SW (Software), después el nivel SW-N3 y finalmente el paralelo SW-N3-A. Respetar la relación Universidad → Facultad → Carrera → Nivel → Paralelo.
•	Comprobar las consultas de nivel, altura y cantidad de hojas después de agregar las nuevas unidades.
•	Implementar el método recursivo public String obtenerRutaJerarquica(String codigo), que devuelva la ruta desde UTA hasta la unidad solicitada, separando los códigos con « -> ». Si no existe, devolver «No existe».
Misión 2. Protección de unidades y transformación binaria
•	Verificar y, si hace falta, reforzar ArbolOrganizacional.eliminar(String codigo), de modo que no permita eliminar unidades que tengan subunidades ni paralelos que contengan estudiantes.
•	Comprobar que sí puede eliminarse una unidad hoja vacía y que la operación devuelve false cuando el código no existe.
•	Ejecutar transformarABinario() y comprobar que represente correctamente la relación hijo izquierdo / hermano derecho sin alterar el árbol general original. Debe identificar la relación entre EB y SW, y entre EB-N1-A y EB-N1-B.
Misión 3. Validación de datos y traslado entre paralelos
•	En Estudiante.java, comprobar que la matrícula sea positiva y que setPromedio rechace valores fuera de 0 a 10 y valores no finitos, como NaN e Infinity.
•	En ArbolOrganizacional.java, implementar public boolean trasladarEstudiante(String codOrigen, String codDestino, int matricula).
•	Aceptar el traslado únicamente si origen y destino existen, son de tipo PARALELO, el estudiante existe en el origen y su matrícula no está duplicada en el destino.
•	Evitar pérdida de información: insertar primero en el destino y eliminar del origen solamente si la inserción fue satisfactoria. Contemplar cómo recuperar el estado anterior si la eliminación de origen fallara.
Misión 4. Modificación segura y casos de eliminación del ABB
•	En EB-N1-A, modificar el promedio de la matrícula 35 a 9.80. Verificar que un intento posterior de actualización con cédula inválida o promedio 15 no altere el registro válido.
•	Trasladar, en este orden, las matrículas 10, 40, 45 y 30 desde EB-N1-A hacia EB-N1-B mediante trasladarEstudiante(...).
•	Comprobar que en el ABB de origen estas eliminaciones cubren los casos de nodo hoja, nodo con un hijo, nodo con dos hijos y eliminación de la raíz. Registrar el inorden antes y después para comprobar la conservación del orden.
Misión 5. Recorridos y estadísticas comparadas
•	En ArbolEstudiantes.java, implementar recursivamente public int contarAprobados(double notaMinima), que cuente los estudiantes cuyo promedio sea mayor o igual al umbral indicado.
•	Después de los traslados, obtener para EB-N1-A y EB-N1-B: recorrido inorden, cantidad de estudiantes, altura, matrícula menor, matrícula mayor, promedio general y estudiantes aprobados con nota mínima 8.00.
•	Comparar los datos finales entre ambos paralelos y explicar brevemente por qué el recorrido inorden conserva el orden de matrículas.
Misión 6. Integración y reinicio
•	En Main.java, incorporar una opción 8 denominada «Ejecutar reto integrador», que ejecute o permita comprobar en secuencia las operaciones de las misiones anteriores, informando claramente los resultados.
•	Evitar que la ejecución repetida de la opción 8 genere unidades o estudiantes duplicados o un estado inconsistente.
•	Comprobar que la opción 7 reconstruya los datos iniciales: las nueve matrículas vuelven a EB-N1-A, EB-N1-B queda vacío y las unidades incorporadas durante el reto dejan de existir.
4. Verificaciones obligatorias
•	La ruta de EB-N1-A debe corresponder a UTA -> FCHE -> EB -> EB-N1 -> EB-N1-A.
•	La eliminación de EB-N1-A debe rechazarse mientras tenga estudiantes. La eliminación de SW-N3-A debe ser posible cuando sea una hoja vacía.
•	Tras los cuatro traslados, EB-N1-A debe contener las matrículas 15, 20, 35, 50 y 55; EB-N1-B debe contener 10, 30, 40 y 45.
•	Con los promedios iniciales indicados, el promedio de EB-N1-A debe ser 8.36 y el de EB-N1-B, 8.00.
•	El sistema debe compilar, ejecutar el menú y permitir recuperar íntegramente los datos originales mediante el reinicio.
5. Entrega del reto
Entregar el proyecto Java con las clases modificadas, sin errores de compilación, y evidencias claras de los casos solicitados. Incluir una explicación breve de las decisiones de implementación y de las validaciones realizadas. Los resultados deben corresponder a una ejecución real del proyecto y no solamente a salidas esperadas transcritas.
