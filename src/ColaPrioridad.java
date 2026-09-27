/**
 * Clase ColaPrioridad.
 * Cola de prioridad hecha con nodos propios (no se usa ninguna colección de
 * Java). Aquí se guardan los tickets PENDIENTES, o sea, los que todavía no ha
 * resuelto el administrador.
 *
 * CRITERIO DE PRIORIDAD USADO:
 * Cada ticket tiene un número de prioridad: 1 = Alta, 2 = Media y 3 = Baja.
 * Mientras más pequeño es el número, más urgente es el ticket, así que los
 * tickets de prioridad Alta se colocan más cerca del frente de la cola.
 * Cuando dos tickets tienen la MISMA prioridad se respeta el orden de llegada
 * (FIFO): el que se creó primero sale primero. Eso se logra insertando el
 * ticket nuevo DESPUÉS de todos los que ya tienen una prioridad igual o mayor
 * (número menor o igual), nunca antes de ellos.
 *
 * Solo se atiende y se saca el ticket que está al frente, que siempre es el
 * más urgente y el más antiguo dentro de su prioridad.
 */
public class ColaPrioridad {

    //Atributos.
    private Nodo frente; //Primer nodo de la cola, el ticket que se atiende de una.

    //Constructor.

    /**
     * Crea una cola vacía, es decir, sin ningún nodo al frente.
     */
    public ColaPrioridad() {
        this.frente = null;
    }

    //Métodos.

    /**
     * Revisa si la cola está vacía.
     *
     * @return true si no hay ningún ticket pendiente, false si hay al menos uno.
     */
    public boolean estaVacia() {
        return frente == null;
    }

    /**
     * Inserta un ticket en la cola respetando su prioridad.
     * Primero arma el nodo nuevo. Si la cola está vacía, o si el ticket nuevo
     * es más urgente que el que está al frente, el nodo nuevo pasa a ser el
     * frente. Si no, recorre la cola con un nodo auxiliar (nodoActual) mientras
     * el siguiente nodo tenga una prioridad igual o más urgente, y lo inserta
     * justo en ese punto.
     *
     * @param ticket ticket pendiente que se va a encolar.
     */
    public void insertar(Ticket ticket) {
        Nodo nodoNuevo = new Nodo(ticket);

        if (estaVacia() || ticket.getPrioridad() < frente.getTicket().getPrioridad()) {
            //El ticket nuevo queda al frente de la cola.
            nodoNuevo.setSiguiente(frente);
            frente = nodoNuevo;
        } else {
            //Se busca la posición correcta usando un nodo auxiliar.
            Nodo nodoActual = frente;
            while (nodoActual.getSiguiente() != null
                    && nodoActual.getSiguiente().getTicket().getPrioridad() <= ticket.getPrioridad()) {
                nodoActual = nodoActual.getSiguiente();
            }
            //Se conecta el nodo nuevo en medio de nodoActual y su siguiente.
            nodoNuevo.setSiguiente(nodoActual.getSiguiente());
            nodoActual.setSiguiente(nodoNuevo);
        }
    }

    /**
     * Muestra cuál es el ticket que está al frente de la cola, sin sacarlo.
     *
     * @return ticket del frente, o null si la cola está vacía.
     */
    public Ticket verFrente() {
        if (estaVacia()) {
            System.out.println("La cola se encuentra vacía.\n");
            return null;
        }
        return frente.getTicket();
    }

    /**
     * Saca de la cola el ticket que está al frente y lo retorna.
     * El frente pasa a ser el siguiente nodo. Se desconecta el nodo que sale
     * para que no quede apuntando a la cola.
     * No imprime nada: si la cola está vacía retorna null y es el menú el que
     * le avisa a la persona, para no repetir dos mensajes en pantalla.
     *
     * @return ticket que se sacó de la cola, o null si la cola está vacía.
     */
    public Ticket remover() {
        if (estaVacia()) {
            return null;
        }

        Nodo nodoTemp = frente;
        frente = frente.getSiguiente();
        nodoTemp.setSiguiente(null);
        return nodoTemp.getTicket();
    }

    /**
     * Recorre la cola con un nodo auxiliar e imprime todos los tickets
     * pendientes en el orden en que van a ser atendidos.
     */
    public void mostrarCola() {
        if (estaVacia()) {
            System.out.println("La cola se encuentra vacía.\n");
            return;
        }

        Nodo nodoActual = frente;
        int posicion = 1;
        while (nodoActual != null) {
            System.out.println("Posición " + posicion + " de la cola:");
            System.out.println(nodoActual.getTicket() + "\n");
            nodoActual = nodoActual.getSiguiente();
            posicion++;
        }
    }
}
