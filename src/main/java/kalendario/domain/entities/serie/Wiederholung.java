package kalendario.domain.entities.serie;


import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.List;

public interface Wiederholung {
    public Date naechsterZeitpunktAb(Date date);
    public List<Date> alleZeitpunkteInZeitraum(Zeitraum zeitraum);
    public boolean istZeitpunktInWiederholung(Date date);
}
