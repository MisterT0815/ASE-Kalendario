package value_objects;

import java.util.Date;
import java.util.List;

public interface Wiederholung {
    public Date naechsterZeitpunktAb(Date date);
    public List<Date> alleZeitpunkteInZeitraum(Zeitraum zeitraum);
}
