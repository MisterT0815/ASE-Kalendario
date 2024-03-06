package kalendario.application.crud.sicherheit;

public class ExistiertNichtException extends Exception {
    public ExistiertNichtException() {
    }

    public ExistiertNichtException(String message) {
        super(message);
    }

    public ExistiertNichtException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistiertNichtException(Throwable cause) {
        super(cause);
    }

    public ExistiertNichtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
