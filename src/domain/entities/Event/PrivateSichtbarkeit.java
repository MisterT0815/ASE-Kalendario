package domain.entities.Event;

import domain.entities.benutzer.Benutzer;

import java.util.List;

public class PrivateSichtbarkeit implements Sichtbarkeit {

    List<Benutzer> sichtbarFuer;

    public PrivateSichtbarkeit(List<Benutzer> sichtbarFuer){
        this.sichtbarFuer = sichtbarFuer;
    }
    @Override
    public boolean istSichtbarFuer(Benutzer benutzer) {
        return sichtbarFuer.contains(benutzer);
    }
}
