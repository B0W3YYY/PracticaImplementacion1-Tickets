/**
 * Clase ListaEnlazadaSimple.
 * Lista enlazada simple hecha con nodos propios (no se usa ninguna colección
 * de Java). Aquí se guardan los tickets RESUELTOS.
 * Se escogió una lista enlazada porque los tickets resueltos ya no se atienden
 * en ningún orden especial: solamente se guardan como historial y se consultan
 * por su id, y la lista permite ir agregando tickets sin tener que definir un
 * tamaño máximo desde el inicio.
 * Solo se guarda la referencia al primer nodo; desde ahí se recorre toda la
 * lista con un nodo auxiliar hasta llegar a null.
 */
public class ListaEnlazadaSimple {

    //Atributos.
    private Nodo primero; //Primer nodo de la lista (null si la lista está vacía).

    //Constructor.

    /**
     * Crea una lista vacía, o sea, sin ningún nodo.
     */
    public ListaEnlazadaSimple() {
        this.primero = null;
    }

    //Métodos.

    /**
     * Revisa si la lista está vacía.
     *
     * @return true si no hay ningún ticket resuelto, false si hay al menos uno.
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Inserta un ticket al inicio de la lista. El nodo nuevo apunta al que era
     * el primero y luego pasa a ser el primero.
     * Es una de las dos formas de insertar que tiene toda lista enlazada
     * simple. En este programa el administrador usa insertarNodoFinal(), pero
     * se deja también esta para que la estructura quede completa y se pueda
     * reutilizar si más adelante se necesita el historial en orden inverso.
     *
     * @param ticket ticket que se va a guardar en la lista.
     */
    public void insertarNodoInicio(Ticket ticket) {
        Nodo nodoNuevo = new Nodo(ticket);
        nodoNuevo.setSiguiente(primero);
        primero = nodoNuevo;
    }

    /**
     * Inserta un ticket al final de la lista. Si la lista está vacía el nodo
     * nuevo queda de primero; si no, se recorre con un nodo auxiliar hasta el
     * último nodo (el que tiene siguiente en null) y ahí se conecta.
     * Este es el método que usa el administrador al resolver, para que los
     * tickets queden en el mismo orden en que se fueron resolviendo.
     *
     * @param ticket ticket que se va a guardar al final de la lista.
     */
    public void insertarNodoFinal(Ticket ticket) {
        Nodo nodoNuevo = new Nodo(ticket);

        if (estaVacia()) {
            primero = nodoNuevo;
        } else {
            Nodo nodoActual = primero;
            while (nodoActual.getSiguiente() != null) {
                nodoActual = nodoActual.getSiguiente();
            }
            nodoActual.setSiguiente(nodoNuevo);
        }
    }

    /**
     * Busca un ticket dentro de la lista usando su id.
     * Recorre la lista con un nodo auxiliar mientras no llegue a null y va
     * comparando el id de cada ticket con el id que se está buscando.
     *
     * @param idBuscar id del ticket que se quiere encontrar.
     * @return el ticket con ese id, o null si no está en la lista.
     */
    public Ticket buscarNodo(int idBuscar) {
        Nodo nodoActual = primero;
        while (nodoActual != null) {
            if (nodoActual.getTicket().getId() == idBuscar) {
                return nodoActual.getTicket();
            }
            nodoActual = nodoActual.getSiguiente();
        }
        return null;
    }

    /**
     * Recorre la lista con un nodo auxiliar e imprime todos los tickets
     * resueltos, desde el primero hasta el último.
     */
    public void mostrarLista() {
        if (estaVacia()) {
            System.out.println("La lista de tickets resueltos se encuentra vacía.\n");
            return;
        }

        Nodo nodoActual = primero;
        while (nodoActual != null) {
            System.out.println(nodoActual.getTicket() + "\n");
            nodoActual = nodoActual.getSiguiente();
        }
    }
}
