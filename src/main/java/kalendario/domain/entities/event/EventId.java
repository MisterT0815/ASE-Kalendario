package kalendario.domain.entities.event;

import java.util.Objects;

public class EventId{
    private final int id;
    protected EventId(int id){
        this.id = id;
    }

    public int getId(){
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
