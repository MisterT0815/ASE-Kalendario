package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieUpdateTest {

    SerienRepository serienRepository = mock();
    Serie serie = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer = mock();
    SerieUpdate serieUpdate;

    @BeforeEach
    void init(){
        serieUpdate = new SerieUpdate(serienRepository, schreibZugriffVerifizierer);
    }

    @Test
    void updateSerieSollEventRepositoryRufen() throws SaveException, KeinZugriffException {
        serieUpdate.updateSerie(serie);
        verify(serienRepository).editSerie(serie);
    }

    @Test
    void updateSerieSollFehlschlagenWennRepositoryFehlschlaegt() throws SaveException {
        doThrow(SaveException.class).when(serienRepository).editSerie(serie);
        assertThrows(SaveException.class, () -> serieUpdate.updateSerie(serie));
    }

    @Test
    void updateSerieSollFehlschlagenWennKeinSchreibZugriffExistiert() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerSerie(serie);
        assertThrows(KeinZugriffException.class, () -> serieUpdate.updateSerie(serie));
    }



}
