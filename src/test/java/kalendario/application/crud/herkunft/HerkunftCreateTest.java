package kalendario.application.crud.herkunft;

import kalendario.application.herkunft.CommandLineHerkunft;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.HerkunftRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class HerkunftCreateTest {

    HerkunftRepository herkunftRepository = mock();
    Session session = mock();
    Herkunft herkunft = mock();
    BenutzerId benutzer = mock();
    HerkunftId herkunftId = mock();
    HerkunftCreate herkunftCreate;

    @BeforeEach
    void init(){
        herkunftCreate = new HerkunftCreate(herkunftRepository, session);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
    }

    @Test
    void createOrGetCommandLineHerkunftVonAktuellemBenutzerSollHerkunftVonRepositoryNehmenWennBenutzerBereitsCommandLineHerkunftHat() throws KeinZugriffException {
        CommandLineHerkunft commandLineHerkunftBereitsExistend = new CommandLineHerkunft(session);
        commandLineHerkunftBereitsExistend.setId(herkunftId);
        List<Herkunft> bereitsExistierendeHerkuenfte = List.of(herkunft, commandLineHerkunftBereitsExistend);
        when(herkunftRepository.getHerkuenfteVonBesitzer(benutzer)).thenReturn(bereitsExistierendeHerkuenfte);
        assertEquals(commandLineHerkunftBereitsExistend, herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer());
        verify(herkunftRepository, never()).saveHerkunft(herkunft);
    }

    @Test
    void createOrGetCommandLineHerkunftVonAktuellemBenutzerSollNeueHerkunftSpeichernWennNochKeineFuerBenutzerExistiert() throws KeinZugriffException {
        List<Herkunft> bereitsExistierendeHerkuenfte = List.of(herkunft, herkunft);
        when(herkunftRepository.getHerkuenfteVonBesitzer(benutzer)).thenReturn(bereitsExistierendeHerkuenfte);
        assertEquals(benutzer, herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer().getBesitzerId());
    }

    @Test
    void createOrGetCommandLineHerkunftVonAktuellenBenutzerSollExcpetionWErfenWennKeinUserAngemeldet(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer());
    }

}