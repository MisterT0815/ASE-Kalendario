package kalendario.domain.entities.benutzer;

public class Benutzer {

    private final String name;
    private final BenutzerId id;
    private final String passwort;

    public Benutzer(BenutzerId id, String name, String passwort) {
        this.id = id;
        this.name = name;
        this.passwort = passwort;
    }

    public BenutzerId getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }


    public boolean isSamePassword(String passwordHashed){
        return this.passwort.equals(passwordHashed);
    }


}
