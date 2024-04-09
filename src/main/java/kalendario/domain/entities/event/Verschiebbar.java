package kalendario.domain.entities.event;

import java.time.Duration;

public interface Verschiebbar {
    void pushByDuration(Duration duration);
}
