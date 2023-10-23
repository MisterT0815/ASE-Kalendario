package entities;

public class SerienZuordnung {

    private Benutzer benutzer;
    private SerienEventController serie;

    public SerienZuordnung(Benutzer benutzer, SerienEventController serie) {
        this.benutzer = benutzer;
        this.serie = serie;
    }

}
