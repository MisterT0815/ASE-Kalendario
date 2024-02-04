package kalendario.domain.entities.benutzer;

import java.util.Objects;

public class BenutzerId{
    private final int id;
    protected BenutzerId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BenutzerId that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
