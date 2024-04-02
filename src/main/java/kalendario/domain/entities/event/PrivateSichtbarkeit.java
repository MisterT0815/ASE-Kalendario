package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.BenutzerId;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PrivateSichtbarkeit implements Sichtbarkeit {

    Set<BenutzerId> sichtbarFuer;

    public PrivateSichtbarkeit(Set<BenutzerId> sichtbarFuer){
        this.sichtbarFuer = sichtbarFuer;
    }
    @Override
    public boolean istSichtbarFuer(BenutzerId benutzer) {
        return sichtbarFuer.contains(benutzer);
    }

    public boolean machSichtbarFuer(BenutzerId benutzer){
        return sichtbarFuer.add(benutzer);
    }

    public boolean entferneSichtbarkeitFuer(BenutzerId benutzer){
        return sichtbarFuer.remove(benutzer);
    }
    public Set<BenutzerId> getSichtbarFuer(){
        return sichtbarFuer;
    }

    @Override
    public String toString() {
        return "PrivateSichtbarkeit{" +
                "sichtbarFuer=" + sichtbarFuer +
                '}';
    }
}
