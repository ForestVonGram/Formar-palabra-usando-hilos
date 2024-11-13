import java.util.ArrayList;
import java.util.List;

public class MonitorBuffer {

    private final char[] buffer = new char[10];
    private int siguiente = 0;
    private boolean estaLlena = false;
    private boolean estaVacia = true;

    //Recoge letras del buffer por el consumidor
    public synchronized List<Character> recoger(int cantidad) {
        List<Character> caracteresRecogidos = new ArrayList<>();

        while (estaVacia) {
            try {
                wait(); //Espera hasta que el buffer tenga caracteres
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } 

        //Recoge el número de caracteres indicados o hasta que esté vacío
        for (int i = 0; i < cantidad && siguiente > 0; i++) {
            caracteresRecogidos.add(buffer[--siguiente]);
        }

        //Comprueba si el buffer se ha vaciado después de recoger caracteres
        if (siguiente == 0) {
            estaVacia = true;
        }
        estaLlena = false; //Si se recogieron caracteres, el buffer ya no está lleno
        notifyAll(); //Notifica a los productores que pueden insertar más caracteres

        return caracteresRecogidos;
    }

    //Añadir letras al buffer por los productores
    public synchronized void lanzar(char c) {
        while (estaLlena) {
            try {
                wait(); //Espera hasta que haya espacio disponible en el buffer
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        //Añade un carácter en la posición siguiente
        buffer[siguiente] = c;
        siguiente++;

        //Comprueba si el buffer está lleno después de añadir el carácter
        if (siguiente == buffer.length) {
            estaLlena = true;
        }
        estaVacia = false; //Si se añadió un carácter, el buffer ya no está vacío
        System.out.println("Producido: " + c);

        notifyAll(); //Notifica al consumidor que hay caracteres disponibles
    }

}



//private char buffer[] = new char[6];
//private int siguiente = 0;
////flags para saber el estado del buffer
//private boolean estaLlena = false;
//private boolean estaVacia = true;
//
////Metodo para retirar letras del buffer
//public synchronized char recoger() {
//    //No se puede consumir si el buffer estaVacio cambia a false
//    while (estaVacia) {
//        try {
//            wait(); //Se sale cuando estaVacia cambia a false
//        } catch (InterruptedException e) {
//            ;
//        }
//    }
//    //Decrementa la cuenta, ya que se va a consumir una letra
//    siguiente--;
//    //Comprueba si se retiró la última letra
//    if (siguiente == 0)
//        estaVacia = true;
//    //El buffer no puede estar lleno, porque acabamos de consumir
//    estaLlena = false;
//    notify();
//
//    //Devuelve la letra al thread consumidor
//    return (buffer[siguiente]);
//}
//
////Metodo para añadir letras al buffer
//public synchronized void lanzar(char c) {
//    //Espera hasta que haya sitio para otra letra
//    while (estaLlena) {
//        try {
//            wait(); //Se sale cuando estaLlena cambia a false
//        } catch (InterruptedException e) {
//            ;
//        }
//    }
//    //Añade una letra en el primer lugar disponible
//    buffer[siguiente] = c;
//    //Cambia al siguiente lugar disponible
//    siguiente++;
//    //Comprueba si el buffer está lleno
//    if (siguiente == 6)
//        estaLlena = true;
//    estaVacia = false;
//    notify();
//}