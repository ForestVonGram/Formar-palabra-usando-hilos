import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.IntToDoubleFunction;

public class Consumidor extends Thread {
    private final MonitorBuffer monitor;
    private final char[] palabraObjeto;
    private final List<Character> letrasSobrantes;

    public Consumidor(MonitorBuffer monitor, char[] palabraObjeto, List<Character> letrasSobrantes) {
        this.monitor = monitor;
        this.palabraObjeto = palabraObjeto;
        this.letrasSobrantes = letrasSobrantes;
    }

    @Override
    public void run() {
        StringBuilder palabraFormada = new StringBuilder();
        for (int i = 0; i < palabraObjeto.length; i++) {
            palabraFormada.append(' '); //inicializa con espacios
        }
        try {
            while (!palabraFormada.toString().trim().equals(new String(palabraObjeto).trim())) {
                List<Character> caracteresConsumidos = monitor.recoger(3);
                Thread.sleep(200);

                for (Character c : caracteresConsumidos) {
                    boolean agregado = false;
                    //verifica si el carácter coincide con el siguiente carácter esperado en la palabra objeto
                    for (int i = 0; i < palabraObjeto.length; i++) {
                        if (palabraFormada.charAt(i) == ' ' && palabraObjeto[i] == c) {
                            palabraFormada.setCharAt(i, c);
                            agregado = true;
                        }
                    }
                    if (!agregado) {

                        if (palabraFormada.toString().contains(String.valueOf(c))) {
                            continue;
                        }
                        letrasSobrantes.add(c);
                    }
                }
                System.out.println("Palabra actual: " + palabraFormada.toString());
            }
            registrarLog("Palabra formada correctamente: " + palabraFormada.toString());
            guardarLetrasSobrantes();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void registrarLog(String mensaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true))) {
            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(fecha + " - " + mensaje);
            writer.newLine();
            System.out.println("Log registrado: " + mensaje);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al escribir log");
        }
    }

    private void guardarLetrasSobrantes() {
        try (BufferedWriter escritura = new BufferedWriter(new FileWriter("letrasSobrantes.txt"))) {
            for (Character c : letrasSobrantes) {
                escritura.write(c + "\n");
            }
            System.out.println("Archivo 'letrasSobrantes.txt' creado y escrito correctamente.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al escribir el archivo 'letrasSobrantes.txt'.");
        }
    }
}
