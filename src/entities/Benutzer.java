package entities;

public class Benutzer {

    private String name;
    private String passwordHashed;

    public Benutzer(String name, String passwordHashed) {
        this.name = name;
        this.passwordHashed = passwordHashed;
    }
}
