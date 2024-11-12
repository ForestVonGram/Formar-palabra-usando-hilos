import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MonitorBuffer monitor = new MonitorBuffer();
        List<Character> letrasSobrantes = new ArrayList<>();

        char[] letras = "abcdefghijklmnñopqrstuvwxyz".toCharArray();
        char[] numeros = "0123456789".toCharArray();
        char[] especiales = "@#/%+_:;".toCharArray();
        char[] vocales = "aeiou".toCharArray();

        Productor p1 = new Productor(monitor, especiales, 350);
        Productor p2 = new Productor(monitor, numeros, 250);
        Productor p3 = new Productor(monitor, letras, 150);
        Productor p4 = new Productor(monitor, vocales, 50);

        Consumidor consumidor = new Consumidor(monitor, "progr@macion_3#2023%".toCharArray(), letrasSobrantes);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        consumidor.start();
    }
}
