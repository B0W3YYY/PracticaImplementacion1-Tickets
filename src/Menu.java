import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Clase Menu.
 * Se encarga de TODA la interacción con la persona que usa el programa por la
 * línea de comandos (CLI). Muestra el menú principal, el menú de usuario y el
 * menú de administrador, lee lo que la persona digita, valida los datos y le
 * pide a las estructuras de datos que hagan el trabajo.
 * Usa un solo objeto Scanner para leer todo el teclado.
 */
public class Menu {

    //Atributos.

    //Valor que se retorna cuando la persona no digita un número válido.
    private static final int OPCION_INVALIDA = -1;

    //Línea que se usa para decorar los títulos de los menús.
    private static final String LINEA = "==================================================";

    private Scanner scanner;                     //Lee lo que la persona digita.
    private ColaPrioridad colaPendientes;        //Cola con los tickets pendientes.
    private ListaEnlazadaSimple listaResueltos;  //Lista con los tickets resueltos.

    //Constructor.

    /**
     * Crea el menú y guarda las dos estructuras de datos con las que va a
     * trabajar. También crea el único Scanner del programa.
     *
     * @param colaPendientes  cola de prioridad con los tickets pendientes.
     * @param listaResueltos  lista enlazada simple con los tickets resueltos.
     */
    public Menu(ColaPrioridad colaPendientes, ListaEnlazadaSimple listaResueltos) {
        this.colaPendientes = colaPendientes;
        this.listaResueltos = listaResueltos;
        this.scanner = new Scanner(System.in);
    }

    //Métodos.

    /**
     * Muestra el menú principal y se repite hasta que la persona digita 0.
     * Desde aquí se entra al menú de usuario o al menú de administrador.
     */
    public void mostrarMenuPrincipal() {
        int opcion;

        do {
            mostrarTitulo("SISTEMA DE GESTIÓN DE TICKETS EN LÍNEA");
            System.out.println("1. Menú de usuario");
            System.out.println("2. Menú de administrador");
            System.out.println("0. Salir");
            System.out.print("Digite una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    mostrarMenuUsuario();
                    break;
                case 2:
                    mostrarMenuAdministrador();
                    break;
                case 0:
                    System.out.println("\nGracias por usar el sistema de tickets. ¡Hasta luego!\n");
                    break;
                default:
                    System.out.println("\nOpción inválida. Digite 1, 2 o 0.\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú del usuario y se repite hasta que digita 0.
     * Desde aquí el usuario crea tickets y busca tickets ya resueltos.
     */
    private void mostrarMenuUsuario() {
        int opcion;

        do {
            mostrarTitulo("MENÚ DE USUARIO");
            System.out.println("1. Crear un ticket nuevo");
            System.out.println("2. Buscar un ticket por su id");
            System.out.println("0. Volver al menú principal");
            System.out.print("Digite una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    crearTicket();
                    break;
                case 2:
                    buscarTicket();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.out.println("\nOpción inválida. Digite 1, 2 o 0.\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Muestra el menú del administrador y se repite hasta que digita 0.
     * Desde aquí el administrador revisa la cola y resuelve tickets.
     */
    private void mostrarMenuAdministrador() {
        int opcion;

        do {
            mostrarTitulo("MENÚ DE ADMINISTRADOR");
            System.out.println("1. Ver el ticket que está al frente de la cola");
            System.out.println("2. Resolver el ticket que está al frente de la cola");
            System.out.println("3. Ver todos los tickets pendientes");
            System.out.println("4. Ver todos los tickets resueltos");
            System.out.println("0. Volver al menú principal");
            System.out.print("Digite una opción: ");
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    verTicketDelFrente();
                    break;
                case 2:
                    resolverTicketDelFrente();
                    break;
                case 3:
                    verTicketsPendientes();
                    break;
                case 4:
                    verTicketsResueltos();
                    break;
                case 0:
                    System.out.println("\nVolviendo al menú principal...\n");
                    break;
                default:
                    System.out.println("\nOpción inválida. Digite 1, 2, 3, 4 o 0.\n");
                    break;
            }
        } while (opcion != 0);
    }

    /**
     * Le pide al usuario los datos del ticket, crea el objeto Ticket y lo
     * inserta en la cola de prioridad según su nivel de urgencia.
     */
    private void crearTicket() {
        mostrarTitulo("CREAR UN TICKET NUEVO");

        String nombreCompleto = leerTextoObligatorio("Digite su nombre completo: ");
        String descripcion = leerTextoObligatorio("Describa el problema: ");
        int prioridad = leerPrioridad();

        Ticket ticketNuevo = new Ticket(descripcion, nombreCompleto, prioridad);
        colaPendientes.insertar(ticketNuevo);

        System.out.println("\nTicket creado con id #" + ticketNuevo.getId() + ".");
        System.out.println("Su ticket quedó en la cola de pendientes con prioridad "
                + ticketNuevo.getPrioridadTexto() + ".\n");
    }

    /**
     * Le pide al usuario un id y lo busca en la lista de tickets resueltos.
     * Si lo encuentra muestra todos sus datos. Si no está en la lista pero el
     * id sí existe, avisa que el ticket todavía está pendiente. Si el id nunca
     * fue creado, avisa que no existe.
     */
    private void buscarTicket() {
        mostrarTitulo("BUSCAR UN TICKET POR SU ID");
        System.out.print("Digite el id del ticket: ");
        int idBuscar = leerOpcion();

        if (idBuscar < 1) {
            System.out.println("\nEl id debe ser un número entero mayor que cero.\n");
            return;
        }

        Ticket ticketEncontrado = listaResueltos.buscarNodo(idBuscar);

        if (ticketEncontrado != null) {
            System.out.println("\nEl ticket #" + idBuscar + " ya fue resuelto:\n");
            System.out.println(ticketEncontrado + "\n");
        } else if (idBuscar <= Ticket.getCantidad()) {
            System.out.println("\nEl ticket #" + idBuscar + " aún está pendiente de resolución.\n");
        } else {
            System.out.println("\nNo existe ningún ticket con el id " + idBuscar + ".\n");
        }
    }

    /**
     * Muestra el ticket que está al frente de la cola, sin sacarlo de ahí.
     * Ese es el próximo ticket que el administrador debería resolver.
     */
    private void verTicketDelFrente() {
        mostrarTitulo("TICKET AL FRENTE DE LA COLA");
        Ticket ticketDelFrente = colaPendientes.verFrente();

        if (ticketDelFrente != null) {
            System.out.println("Próximo ticket por resolver:\n");
            System.out.println(ticketDelFrente + "\n");
        }
    }

    /**
     * Resuelve el ticket que está al frente de la cola: lo saca de la cola,
     * le asigna la fecha de resolución y lo inserta al final de la lista
     * enlazada de tickets resueltos.
     */
    private void resolverTicketDelFrente() {
        mostrarTitulo("RESOLVER EL TICKET DEL FRENTE");
        Ticket ticketResuelto = colaPendientes.remover();

        if (ticketResuelto == null) {
            System.out.println("No hay tickets pendientes por resolver.\n");
            return;
        }

        ticketResuelto.setFechaResolucion(LocalDateTime.now());
        listaResueltos.insertarNodoFinal(ticketResuelto);

        System.out.println("El ticket #" + ticketResuelto.getId() + " fue resuelto con éxito.\n");
        System.out.println(ticketResuelto + "\n");
    }

    /**
     * Muestra todos los tickets que siguen pendientes, en el orden en que van
     * a ser atendidos.
     */
    private void verTicketsPendientes() {
        mostrarTitulo("TICKETS PENDIENTES");
        colaPendientes.mostrarCola();
    }

    /**
     * Muestra todos los tickets que ya fueron resueltos, en el orden en que se
     * fueron resolviendo.
     */
    private void verTicketsResueltos() {
        mostrarTitulo("TICKETS RESUELTOS");
        listaResueltos.mostrarLista();
    }

    /**
     * Lee una línea del teclado y la convierte a número entero.
     * Si la persona digita letras o símbolos no deja que el programa se caiga:
     * atrapa la excepción y retorna un valor inválido para que el menú vuelva
     * a preguntar.
     *
     * @return número digitado, o OPCION_INVALIDA si no era un número.
     */
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException error) {
            return OPCION_INVALIDA;
        }
    }

    /**
     * Le pide un texto a la persona y no la deja seguir hasta que escriba algo,
     * porque el nombre y la descripción no pueden quedar vacíos.
     *
     * @param mensaje texto que se le muestra a la persona.
     * @return texto digitado, sin espacios de más y nunca vacío.
     */
    private String leerTextoObligatorio(String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("Este dato no puede quedar vacío. Intente de nuevo.\n");
            }
        } while (texto.isEmpty());

        return texto;
    }

    /**
     * Muestra las tres prioridades posibles y valida que la persona escoja 1,
     * 2 o 3. Se repite hasta que digite una prioridad válida.
     *
     * @return prioridad escogida (1 = Alta, 2 = Media, 3 = Baja).
     */
    private int leerPrioridad() {
        int prioridad;
        boolean prioridadValida;

        do {
            System.out.println("Prioridades disponibles:");
            System.out.println("   1. Alta");
            System.out.println("   2. Media");
            System.out.println("   3. Baja");
            System.out.print("Digite la prioridad del ticket: ");
            prioridad = leerOpcion();

            prioridadValida = prioridad >= Ticket.PRIORIDAD_ALTA && prioridad <= Ticket.PRIORIDAD_BAJA;
            if (!prioridadValida) {
                System.out.println("Prioridad inválida. Solo se acepta 1, 2 o 3.\n");
            }
        } while (!prioridadValida);

        return prioridad;
    }

    /**
     * Imprime el título de un menú entre dos líneas, para que la pantalla se
     * vea ordenada y sea fácil saber dónde está la persona.
     *
     * @param titulo nombre del menú o de la acción que se va a realizar.
     */
    private void mostrarTitulo(String titulo) {
        System.out.println(LINEA);
        System.out.println("   " + titulo);
        System.out.println(LINEA);
    }
}
