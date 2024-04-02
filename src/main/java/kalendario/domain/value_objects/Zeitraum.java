package kalendario.domain.value_objects;

import java.util.Date;
import java.util.Objects;

public final class Zeitraum {
    private final Date start;
    private final Date ende;


    public Zeitraum(Date start, Date ende) throws IllegalArgumentException {
        super();
        if(ende.before(start)){
            throw new IllegalArgumentException("Endzeitpunkt darf nicht vor dem Startzeitpunkt sein");
        }
        this.start = start;
        this.ende = ende;
    }

    public boolean enthaelt(Date zeitpunkt){
        return start.equals(zeitpunkt) || (start.before(zeitpunkt) && ende.after(zeitpunkt));
    }

    public Date getStart(){
        return new Date(this.start.getTime());
    }

    public Date getEnde() {
        return new Date(this.ende.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zeitraum zeitraum = (Zeitraum) o;
        return Objects.equals(start, zeitraum.start) && Objects.equals(ende, zeitraum.ende);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, ende);
    }

    @Override
    public String toString() {
        return "Zeitraum{" +
                "start=" + start +
                ", ende=" + ende +
                '}';
    }
}
