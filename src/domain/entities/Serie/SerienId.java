package domain.entities.Serie;

import java.util.Objects;

public class SerienId {
    private int id;

    public SerienId(int id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SerienId serienId)) return false;
        return id == serienId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
