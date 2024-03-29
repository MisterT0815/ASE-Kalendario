package kalendario.domain.entities.serie;

import java.util.Objects;
import java.util.UUID;

public class SerienId {
    private final UUID id;
    public SerienId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerienId serienId = (SerienId) o;
        return Objects.equals(id, serienId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
