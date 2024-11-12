public class Productor extends Thread {
    private final MonitorBuffer monitor;
    private final char[] caracteres;
    private final int tiempoDeEspera;

    public Productor(MonitorBuffer monitor, char[] caracteres, int tiempoDeEspera) {
        this.monitor = monitor;
        this.caracteres = caracteres;
        this.tiempoDeEspera = tiempoDeEspera;
    }

    @Override
    public void run() {
        try {
            for (char c : caracteres) {
                monitor.lanzar(c);
                Thread.sleep(tiempoDeEspera);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
