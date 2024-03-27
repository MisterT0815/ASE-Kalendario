package kalendario.domain.entities.benutzer;

public class Benutzer {

    protected final String name;
    protected final BenutzerId id;
    protected final String passwort;

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
    public String getPasswort(){return this.passwort;}


    public boolean isSamePassword(String passwordHashed){
        return this.passwort.equals(passwordHashed);
    }


}
