package main.java.domain.entities.benutzer;

public class Benutzer {

    private String name;
    private BenutzerId id;
    private String passwordHashed;

    public Benutzer(BenutzerId id, String name, String passwordHashed) {
        this.id = id;
        this.name = name;
        this.passwordHashed = passwordHashed;
    }


}
