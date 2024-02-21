package kalendario.domain.entities.benutzer;

public class Benutzer {

    private final String name;
    private final BenutzerId id;
    private final String passwordHashed;

    public Benutzer(BenutzerId id, String name, String passwordHashed) {
        this.id = id;
        this.name = name;
        this.passwordHashed = passwordHashed;
    }


}
