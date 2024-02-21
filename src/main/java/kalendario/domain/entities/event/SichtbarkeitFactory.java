package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;

import java.util.Set;

public class SichtbarkeitFactory {

    public static Sichtbarkeit fuerAlle(){
        return new PublicSichtbarkeit();
    }

    public static Sichtbarkeit nurFuer(Set<BenutzerId> benutzer){
        return new PrivateSichtbarkeit(benutzer);
    }

}
