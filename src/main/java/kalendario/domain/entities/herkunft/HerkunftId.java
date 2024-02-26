package kalendario.domain.entities.herkunft;

import java.util.Objects;

public class HerkunftId{
    private final int id;
    public HerkunftId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HerkunftId that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
