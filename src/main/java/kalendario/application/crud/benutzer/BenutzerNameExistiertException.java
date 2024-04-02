package kalendario.application.crud.benutzer;

public class BenutzerNameExistiertException extends Exception{
    public BenutzerNameExistiertException(String message) {
        super(message);
    }
}
