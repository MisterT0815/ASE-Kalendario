package kalendario.domain.entities.serie;


import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.List;

public interface Wiederholung {
    Date naechsterZeitpunktAb(Date date);
    List<Date> alleZeitpunkteInZeitraum(Zeitraum zeitraum);
    boolean istZeitpunktInWiederholung(Date date);
}
