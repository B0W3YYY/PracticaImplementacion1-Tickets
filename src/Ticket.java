import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase Ticket.
 * Representa un ticket de soporte dentro del sistema de gestión de tickets.
 * Guarda los datos que se piden al crearlo (descripción, nombre completo del
 * usuario, prioridad y fecha de creación) y la fecha de resolución, que
 * comienza en null porque el ticket todavía no ha sido atendido.
 * Además lleva un contador static llamado "cantidad" que sirve para darle a
 * cada ticket un id único e irrepetible.
 */
public class Ticket {

    //Atributos.

    //Constantes que representan las tres prioridades permitidas.
    public static final int PRIORIDAD_ALTA = 1;
    public static final int PRIORIDAD_MEDIA = 2;
    public static final int PRIORIDAD_BAJA = 3;

    //Formato con el que se muestran las fechas en pantalla.
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    //Contador consecutivo compartido por todos los tickets. De aquí sale el id.
    private static int cantidad = 0;

    private int id;                        //Número único del ticket.
    private String descripcion;            //Problema que reporta el usuario.
    private String nombreCompleto;         //Nombre del usuario que creó el ticket.
    private int prioridad;                 //1 = Alta, 2 = Media, 3 = Baja.
    private LocalDateTime fechaCreacion;   //Momento en que se creó el ticket.
    private LocalDateTime fechaResolucion; //Momento en que se resolvió (null si está pendiente).

    //Constructor.

    /**
     * Crea un ticket nuevo.
     * Aumenta el contador static "cantidad" y usa ese valor como id, de modo
     * que ningún ticket repite su número. La fecha de creación se toma del
     * reloj de la computadora y la fecha de resolución queda en null.
     *
     * @param descripcion    problema reportado por el usuario.
     * @param nombreCompleto nombre completo del usuario que crea el ticket.
     * @param prioridad      1 = Alta, 2 = Media, 3 = Baja.
     */
    public Ticket(String descripcion, String nombreCompleto, int prioridad) {
        cantidad++;
        this.id = cantidad;
        this.descripcion = descripcion;
        this.nombreCompleto = nombreCompleto;
        this.prioridad = prioridad;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaResolucion = null;
    }

    //Métodos.

    //Todos los atributos tienen su getter, porque son privados y el resto del
    //programa necesita leerlos. En cambio el único setter es el de la
    //fechaResolucion: es el único dato de un ticket que cambia durante su
    //ciclo de vida, cuando el administrador lo resuelve. El id, la
    //descripción, el nombre del usuario, la prioridad y la fechaCreacion se
    //asignan una sola vez al crear el ticket y no deben poder modificarse
    //después, porque eso alteraría el reporte original del usuario.

    /**
     * Retorna cuántos tickets se han creado en total.
     * Sirve para saber si un id existe o no.
     *
     * @return valor actual del contador static.
     */
    public static int getCantidad() {
        return cantidad;
    }

    /**
     * Retorna el id del ticket. El menú lo usa para mostrarlo y la lista
     * enlazada lo usa para buscar el ticket.
     *
     * @return número único del ticket.
     */
    public int getId() {
        return id;
    }

    /**
     * Retorna la descripción del problema que reportó el usuario.
     *
     * @return descripción del ticket.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna el nombre completo del usuario que creó el ticket.
     *
     * @return nombre completo del usuario.
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Retorna la prioridad en número (1, 2 o 3).
     * La cola de prioridad usa este valor para ordenar los tickets.
     *
     * @return prioridad del ticket.
     */
    public int getPrioridad() {
        return prioridad;
    }

    /**
     * Retorna la fecha de creación del ticket.
     *
     * @return fecha y hora en que se creó el ticket.
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Retorna la fecha de resolución del ticket.
     *
     * @return fecha y hora de resolución, o null si el ticket está pendiente.
     */
    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    /**
     * Asigna la fecha de resolución. El administrador lo usa cuando resuelve
     * el ticket que está al frente de la cola.
     *
     * @param fechaResolucion fecha y hora en que se resolvió el ticket.
     */
    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    /**
     * Traduce el número de la prioridad a una palabra, para que el usuario
     * entienda mejor lo que ve en pantalla.
     *
     * @return "Alta", "Media" o "Baja".
     */
    public String getPrioridadTexto() {
        if (prioridad == PRIORIDAD_ALTA) {
            return "Alta";
        } else if (prioridad == PRIORIDAD_MEDIA) {
            return "Media";
        } else {
            return "Baja";
        }
    }

    /**
     * Arma un texto legible con todos los datos del ticket.
     * Si la fecha de resolución es null muestra la palabra "Pendiente".
     * Lee los atributos por medio de los getters, así toda la lectura de los
     * datos del ticket pasa siempre por el mismo lugar.
     *
     * @return todos los datos del ticket en varias líneas.
     */
    @Override
    public String toString() {
        String textoResolucion;
        if (getFechaResolucion() == null) {
            textoResolucion = "Pendiente";
        } else {
            textoResolucion = getFechaResolucion().format(FORMATO_FECHA);
        }

        return "Ticket #" + getId() + "\n"
                + "   Usuario:             " + getNombreCompleto() + "\n"
                + "   Descripción:         " + getDescripcion() + "\n"
                + "   Prioridad:           " + getPrioridadTexto() + " (" + getPrioridad() + ")\n"
                + "   Fecha de creación:   " + getFechaCreacion().format(FORMATO_FECHA) + "\n"
                + "   Fecha de resolución: " + textoResolucion;
    }
}
