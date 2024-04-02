package kalendario.application.wiederholung;

import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.value_objects.Zeitraum;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ZeitlicherAbstandWiederholung implements Wiederholung {

    private final Duration abstand;
    private final long abstandInMilli;
    private final Date start;

    public ZeitlicherAbstandWiederholung(Duration abstand, Date start){
        this.abstand = abstand;
        this.abstandInMilli = abstand.getSeconds()*1000;
        this.start = start;
    }

    @Override
    public Date naechsterZeitpunktAb(Date date) {
        long startDateDiff = date.getTime() - start.getTime();
        int wiederholungenBisDate = (int) Math.floor((double) startDateDiff / abstandInMilli);
        long zeit = start.getTime() + (wiederholungenBisDate+1) * abstandInMilli;
        return new Date(zeit);
    }

    @Override
    public List<Date> alleZeitpunkteInZeitraum(Zeitraum zeitraum) {
        List<Date> dates = new ArrayList<>();
        if(this.istZeitpunktInWiederholung(zeitraum.getStart())){
            dates.add(zeitraum.getStart());
        }
        Date naechsterZeitpunkt = naechsterZeitpunktAb(zeitraum.getStart());
        while(zeitraum.enthaelt(naechsterZeitpunkt)){
            if(istZeitpunktInWiederholung(naechsterZeitpunkt)){
                dates.add(naechsterZeitpunkt);
            }
            naechsterZeitpunkt = this.naechsterZeitpunktAb(naechsterZeitpunkt);
        }
        return dates;
    }

    @Override
    public boolean istZeitpunktInWiederholung(Date date) {
        long startDateDiff = date.getTime() - start.getTime();
        return (startDateDiff % abstandInMilli) == 0 && (date.after(start) || date.equals(start));
    }

    public Duration getAbstand(){
        return this.abstand;
    }
}
