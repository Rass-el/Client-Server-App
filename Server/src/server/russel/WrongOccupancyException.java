package server.russel;

public class WrongOccupancyException extends Exception {
    public WrongOccupancyException() {
        super();
    }

    public WrongOccupancyException(String message) {
        super(message);
    }

    public String getMessage() {
        return "Не понял! Значение должно быть от 0 до 100";
    }
}
