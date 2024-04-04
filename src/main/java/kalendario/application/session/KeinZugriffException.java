package kalendario.application.session;

public class KeinZugriffException extends Exception{
    public KeinZugriffException() {
        super("Kein Zugriff");
    }

    public KeinZugriffException(String message) {
        super(message);
    }

    public KeinZugriffException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeinZugriffException(Throwable cause) {
        super(cause);
    }

    public KeinZugriffException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
