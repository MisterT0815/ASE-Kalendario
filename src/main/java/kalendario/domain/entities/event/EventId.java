package kalendario.domain.entities.event;

import java.util.Objects;
import java.util.UUID;

public class EventId{
    private final UUID id;
    public EventId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventId that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
