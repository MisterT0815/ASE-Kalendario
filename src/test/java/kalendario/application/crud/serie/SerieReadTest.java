package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.ZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieReadTest {

    SerienRepository serienRepository = mock();
    SerienId serienId = mock();
    Serie serie = mock();
    SerieRead serieRead;
    ZugriffVerfizierer zugriffVerfizierer = mock();

    @BeforeEach
    void init(){
        serieRead = new SerieRead(serienRepository, zugriffVerfizierer);
    }

    @Test
    void getSerieSollSerieVonSerienRepositoryHolen() throws KeinZugriffException {
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertEquals(serie, serieRead.getSerie(serienId).get());
    }

    @Test
    void getSerieSollEmptyOptionalZurueckgebenWennSerienRepositoryNichtsZurueckgibt() throws KeinZugriffException {
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertTrue(serieRead.getSerie(serienId).isEmpty());
    }

    @Test
    void getSerieSollNoAccessExceptionWerfenWennNutzerKeinZugriffAufSerieHat() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(zugriffVerfizierer).verifiziereZugriffFuerSerie(serie);
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertThrows(KeinZugriffException.class, () -> serieRead.getSerie(serienId));
    }


}
