package kalendario.application.crud.herkunft;

import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.HerkunftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class HerkunftReadTest {

    HerkunftRepository herkunftRepository = mock();
    HerkunftId herkunftId = mock();
    Herkunft herkunft = mock();
    HerkunftRead herkunftRead;

    @BeforeEach
    void init(){
        herkunftRead = new HerkunftRead(herkunftRepository);
    }

    @Test
    void getHerkunftSollAnRepositoryDelegieren(){
        when(herkunftRepository.getHerkunftWithId(herkunftId)).thenReturn(herkunft);
        assertEquals(herkunft, herkunftRead.getHerkunft(herkunftId).get());
        when(herkunftRead.getHerkunft(herkunftId)).thenReturn(null);
        assertTrue(herkunftRead.getHerkunft(herkunftId).isEmpty());
    }


}
