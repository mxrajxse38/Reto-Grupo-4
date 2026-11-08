package pruebas;

import app.Contexto;
import enums.TipoRecorrido;
import enums.TipoUnidad;
import java.util.List;
import modelo.Estudiante;
import modelo.UnidadAcademica;
import negocio.ArbolEstudiantes;

/**
 * [MISION 6] AGREGADO: ejecuta el reto completo (Misiones 1 a 5) en orden
 * y muestra si cada prueba salio bien. Al inicio reinicia los datos, por lo
 * que se puede ejecutar varias veces sin duplicar nada.
 */
public class RetoIntegrador {

    private static int total;
    private static int correctas;

    private static final String A = "EB-N1-A";
    private static final String B = "EB-N1-B";

    public static void ejecutar(Contexto c) {

        c.reiniciar(); // estado limpio: 9 matriculas en A, B vacio, sin SW
        total = 0;
        correctas = 0;

        mision1(c);
        mision2(c);
        mision3(c);
        mision4(c);
        mision5(c);

        System.out.println("\n===== RESULTADO FINAL =====");
        System.out.println("Pruebas correctas: " + correctas + " de " + total);
    }

    // ---------------- MISION 1: SW, ruta jerarquica ----------------
    private static void mision1(Contexto c) {
        titulo("MISION 1: unidades SW y ruta jerarquica");

        // Orden obligatorio: universidad > facultad > carrera > nivel > paralelo
        verificar("Agregar carrera SW bajo FCHE", c.organizacion.agregar("FCHE",
                new UnidadAcademica("SW", "Software", TipoUnidad.CARRERA)));
        verificar("Agregar nivel SW-N3 bajo SW", c.organizacion.agregar("SW",
                new UnidadAcademica("SW-N3", "Nivel 3", TipoUnidad.NIVEL)));
        verificar("Agregar paralelo SW-N3-A bajo SW-N3", c.organizacion.agregar("SW-N3",
                new UnidadAcademica("SW-N3-A", "Paralelo A", TipoUnidad.PARALELO)));
        verificar("Rechaza saltarse niveles (paralelo directo bajo FCHE)",
                !c.organizacion.agregar("FCHE",
                        new UnidadAcademica("SW-X", "Invalido", TipoUnidad.PARALELO)));

        verificar("Altura del arbol = 4", c.organizacion.altura() == 4);
        verificar("Hojas del arbol = 3", c.organizacion.contarHojas() == 3);

        String ruta = c.organizacion.obtenerRutaJerarquica(A);
        System.out.println("Ruta: " + ruta);
        verificar("Ruta de EB-N1-A", ruta.equals("UTA -> FCHE -> EB -> EB-N1 -> EB-N1-A"));
        verificar("Ruta de SW-N3-A",
                c.organizacion.obtenerRutaJerarquica("sw-n3-a")
                        .equals("UTA -> FCHE -> SW -> SW-N3 -> SW-N3-A"));
        verificar("Codigo inexistente -> No existe",
                c.organizacion.obtenerRutaJerarquica("XYZ").equals("No existe"));
    }

    // ---------------- MISION 2: eliminar unidades y binario ----------------
    private static void mision2(Contexto c) {
        titulo("MISION 2: eliminacion protegida y transformacion binaria");

        verificar("No elimina EB-N1-A (tiene estudiantes)", !c.organizacion.eliminar(A));
        verificar("No elimina SW-N3 (tiene un hijo)", !c.organizacion.eliminar("SW-N3"));

        String antes = c.organizacion.mostrarJerarquia();
        String binario = c.organizacion.transformarABinario();
        System.out.println(binario);
        verificar("SW queda como hermano derecho de EB",
                binario.contains("Hermano Der de EB: SW"));
        verificar("EB-N1-B queda como hermano derecho de EB-N1-A",
                binario.contains("Hermano Der de EB-N1-A: EB-N1-B"));
        verificar("La transformacion no altera el arbol original",
                antes.equals(c.organizacion.mostrarJerarquia()));

        verificar("Elimina SW-N3-A (hoja vacia)", c.organizacion.eliminar("SW-N3-A"));
        verificar("SW-N3-A ya no existe", c.organizacion.buscar("SW-N3-A") == null);

        // Se vuelve a agregar para dejar el arbol listo para las demas misiones
        c.organizacion.agregar("SW-N3",
                new UnidadAcademica("SW-N3-A", "Paralelo A", TipoUnidad.PARALELO));
    }

    // ---------------- MISION 3: validaciones y traslado ----------------
    private static void mision3(Contexto c) {
        titulo("MISION 3: validaciones de Estudiante y traslado");

        verificar("Matricula 0 rechazada", lanza(() -> new Estudiante(0, "0000000001", "X", 8)));
        verificar("Promedio NaN rechazado",
                lanza(() -> new Estudiante(1, "0000000001", "X", Double.NaN)));
        verificar("Promedio Infinity rechazado",
                lanza(() -> new Estudiante(1, "0000000001", "X", Double.POSITIVE_INFINITY)));
        verificar("Promedio 11 rechazado", lanza(() -> new Estudiante(1, "0000000001", "X", 11)));

        verificar("No traslada a una unidad que no es paralelo",
                !c.organizacion.trasladarEstudiante(A, "SW-N3", 10));
        verificar("No traslada si el estudiante no existe en el origen",
                !c.organizacion.trasladarEstudiante(A, B, 999));
        verificar("No traslada al mismo paralelo", !c.organizacion.trasladarEstudiante(A, A, 10));

        // Duplicado en destino: se coloca una copia de 15 en B y se intenta trasladar
        c.organizacion.buscar(B).getEstudiantes()
                .insertar(new Estudiante(15, "0000000015", "Copia 15", 8));
        verificar("No traslada si ya esta repetido en el destino",
                !c.organizacion.trasladarEstudiante(A, B, 15));
        c.organizacion.buscar(B).getEstudiantes().eliminar(15);
    }

    // ---------------- MISION 4: modificar y 4 casos de eliminacion ----------------
    private static void mision4(Contexto c) {
        titulo("MISION 4: modificacion y casos de eliminacion del ABB");

        verificar("Modifica promedio del 35 a 9.80",
                abbA(c).modificar(35, "0000000035", "Estudiante 35", 9.80));
        verificar("Promedio del 35 = 9.80", abbA(c).buscar(35).getPromedio() == 9.80);

        verificar("Cedula invalida rechazada", lanza(() ->
                abbA(c).modificar(35, "123", "Estudiante 35", 9.80)));
        verificar("Promedio 15 rechazado", lanza(() ->
                abbA(c).modificar(35, "0000000035", "Estudiante 35", 15)));
        verificar("Registro del 35 no se dano",
                abbA(c).buscar(35).getCedula().equals("0000000035")
                        && abbA(c).buscar(35).getPromedio() == 9.80);

        // Caso 1: hoja (10)  | Caso 2: un hijo (40) | Caso 3: dos hijos (45) | Raiz (30)
        trasladarMostrando(c, 10, "hoja");
        trasladarMostrando(c, 40, "un hijo");
        trasladarMostrando(c, 45, "dos hijos (sube el sucesor)");
        trasladarMostrando(c, 30, "raiz");
    }

    private static void trasladarMostrando(Contexto c, int matricula, String caso) {
        System.out.println("\nTraslado del " + matricula + " - caso: " + caso);
        System.out.println("  Inorden A antes:   " + inorden(abbA(c)));
        boolean ok = c.organizacion.trasladarEstudiante(A, B, matricula);
        System.out.println("  Inorden A despues: " + inorden(abbA(c)));
        System.out.println("  Inorden B:         " + inorden(abbB(c)));
        verificar("Traslado del " + matricula + " (" + caso + ") y ambos inorden ordenados",
                ok && ordenado(abbA(c)) && ordenado(abbB(c)));
    }

    // ---------------- MISION 5: aprobados y comparacion ----------------
    private static void mision5(Contexto c) {
        titulo("MISION 5: aprobados y comparacion de paralelos");

        ArbolEstudiantes a = abbA(c);
        ArbolEstudiantes b = abbB(c);

        System.out.println("EB-N1-A inorden: " + inorden(a));
        System.out.printf("EB-N1-A promedio: %.2f%n", a.promedioGeneral());
        System.out.println("EB-N1-B inorden: " + inorden(b));
        System.out.printf("EB-N1-B promedio: %.2f%n", b.promedioGeneral());

        verificar("EB-N1-A = 15, 20, 35, 50, 55", inorden(a).equals("15 20 35 50 55"));
        verificar("EB-N1-B = 10, 30, 40, 45", inorden(b).equals("10 30 40 45"));
        verificar("Promedio EB-N1-A = 8.36", String.format("%.2f", a.promedioGeneral()).equals("8.36"));
        verificar("Promedio EB-N1-B = 8.00", String.format("%.2f", b.promedioGeneral()).equals("8.00"));
        verificar("Aprobados A con nota >= 8 son 5", a.contarAprobados(8) == 5);
        verificar("Aprobados A con nota >= 9 es 1", a.contarAprobados(9) == 1);
        verificar("Aprobados B con nota >= 8 son 4", b.contarAprobados(8) == 4);
        verificar("Nota minima NaN rechazada", lanza(() -> a.contarAprobados(Double.NaN)));
    }

    // ---------------- utilidades ----------------
    private static ArbolEstudiantes abbA(Contexto c) {
        return c.organizacion.buscar(A).getEstudiantes();
    }

    private static ArbolEstudiantes abbB(Contexto c) {
        return c.organizacion.buscar(B).getEstudiantes();
    }

    private static String inorden(ArbolEstudiantes abb) {
        StringBuilder sb = new StringBuilder();
        for (Estudiante e : abb.recorrer(TipoRecorrido.INORDEN)) {
            sb.append(e.getMatricula()).append(' ');
        }
        return sb.toString().trim();
    }

    private static boolean ordenado(ArbolEstudiantes abb) {
        List<Estudiante> l = abb.recorrer(TipoRecorrido.INORDEN);
        for (int i = 1; i < l.size(); i++) {
            if (l.get(i - 1).getMatricula() >= l.get(i).getMatricula()) {
                return false;
            }
        }
        return true;
    }

    private static boolean lanza(Runnable r) {
        try {
            r.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void titulo(String t) {
        System.out.println("\n--- " + t + " ---");
    }

    private static void verificar(String descripcion, boolean ok) {
        total++;
        if (ok) {
            correctas++;
        }
        System.out.println((ok ? "[OK]    " : "[FALLO] ") + descripcion);
    }
}
