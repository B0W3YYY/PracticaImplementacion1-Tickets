/**
 * Clase Nodo.
 * Es la pieza básica de las dos estructuras de datos dinámicas del programa.
 * Cada nodo guarda un Ticket (el dato) y una referencia llamada "siguiente"
 * que apunta al nodo que va después. Cuando "siguiente" es null quiere decir
 * que ese nodo es el último de la estructura.
 * La misma clase Nodo se reutiliza en la ColaPrioridad y en la
 * ListaEnlazadaSimple, porque las dos guardan tickets.
 */
public class Nodo {

    //Atributos.
    private Ticket ticket;  //Dato que guarda el nodo.
    private Nodo siguiente; //Referencia al siguiente nodo (null si es el último).

    //Constructor.

    /**
     * Crea un nodo con el ticket que se recibe. El siguiente empieza en null
     * porque todavía no se ha conectado con ningún otro nodo.
     *
     * @param ticket ticket que se va a guardar dentro del nodo.
     */
    public Nodo(Ticket ticket) {
        this.ticket = ticket;
        this.siguiente = null;
    }

    //Métodos.

    //El ticket se asigna al crear el nodo y no cambia, por eso solo tiene
    //getter. El "siguiente" sí tiene setter, porque es justamente lo que las
    //estructuras van reconectando cuando insertan o sacan un nodo.

    /**
     * Retorna el ticket que guarda el nodo.
     *
     * @return ticket almacenado.
     */
    public Ticket getTicket() {
        return ticket;
    }

    /**
     * Retorna el nodo que sigue a este nodo.
     *
     * @return siguiente nodo, o null si este es el último.
     */
    public Nodo getSiguiente() {
        return siguiente;
    }

    /**
     * Conecta este nodo con el nodo que se recibe.
     *
     * @param siguiente nodo que quedará después de este.
     */
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * Muestra el contenido del nodo, es decir, los datos de su ticket.
     *
     * @return texto con los datos del ticket que guarda el nodo.
     */
    @Override
    public String toString() {
        return ticket.toString();
    }
}
