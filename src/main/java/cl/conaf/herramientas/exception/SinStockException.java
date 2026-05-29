package cl.conaf.herramientas.exception;

public class SinStockException extends RuntimeException {
    public SinStockException(String mensaje) {
        super(mensaje);
    }
}