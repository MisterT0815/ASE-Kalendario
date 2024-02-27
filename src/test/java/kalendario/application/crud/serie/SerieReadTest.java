package kalendario.application.crud.serie;

import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieReadTest {

    SerienRepository serienRepository = mock();
    SerienId serienId = mock();
    Serie serie = mock();
    SerieRead serieRead;

    @BeforeEach
    void init(){
        serieRead = new SerieRead(serienRepository);
    }

    @Test
    void getSerieSollSerieVonSerienRepositoryHolen(){
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertEquals(serie, serieRead.getSerie(serienId).get());
    }

    @Test
    void getSerieSollEmptyOptionalZurueckgebenWennSerienRepositoryNichtsZurueckgibt(){
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertTrue(serieRead.getSerie(serienId).isEmpty());
    }

}
