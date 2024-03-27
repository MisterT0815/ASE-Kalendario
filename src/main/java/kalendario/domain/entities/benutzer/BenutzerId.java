package kalendario.domain.entities.benutzer;

import java.util.Objects;
import java.util.UUID;

public class BenutzerId{
    private final UUID id;
    public BenutzerId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BenutzerId that = (BenutzerId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
