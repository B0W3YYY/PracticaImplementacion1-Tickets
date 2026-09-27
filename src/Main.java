/**
 * Clase Main.
 * Es el punto de entrada del programa. Aquí solamente se crean los objetos que
 * el sistema necesita y se arranca el menú; toda la lógica está en las otras
 * clases para que cada una tenga una sola responsabilidad.
 *
 * Curso: SOFT-10 Estructuras de Datos - Universidad CENFOTEC.
 * Estudiante: Braden Knuter Córdoba.
 * Primera práctica de implementación de estructuras de datos.
 */
public class Main {

    /**
     * Rutina principal del programa.
     * Crea las dos estructuras de datos dinámicas, se las entrega al menú y
     * muestra el menú principal.
     *
     * @param args argumentos de la línea de comandos (no se usan).
     */
    public static void main(String[] args) {

        //1. Cola de prioridad: guarda los tickets PENDIENTES ordenados por urgencia.
        ColaPrioridad colaPendientes = new ColaPrioridad();

        //2. Lista enlazada simple: guarda los tickets RESUELTOS como historial.
        ListaEnlazadaSimple listaResueltos = new ListaEnlazadaSimple();

        //3. Menú de línea de comandos que trabaja con las dos estructuras.
        Menu menu = new Menu(colaPendientes, listaResueltos);

        //4. Arranca el sistema mostrando el menú principal.
        menu.mostrarMenuPrincipal();
    }
}
