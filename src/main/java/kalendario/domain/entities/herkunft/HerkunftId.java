package kalendario.domain.entities.herkunft;

import java.util.Objects;
import java.util.UUID;

public class HerkunftId{
    private final UUID id;
    public HerkunftId(UUID id){
        this.id = id;
    }

    public UUID getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HerkunftId that = (HerkunftId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
