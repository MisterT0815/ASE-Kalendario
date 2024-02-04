package main.java.domain.entities.benutzer;

public class Benutzer {

    private String name;
    private BenutzerId id;
    private String passwordHashed;

    public Benutzer(String name, String passwordHashed) {
        this.name = name;
        this.passwordHashed = passwordHashed;
    }


}
