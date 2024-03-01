package kalendario.application.herkunft;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.HerkunftId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class CommandLineHerkunftTest {

    BenutzerId benutzerId = mock();
    BenutzerId benutzerId2 = mock();
    HerkunftId herkunftId = mock();
    Session session = mock();
    CommandLineHerkunft commandLineHerkunft;

    @Test
    void commandLineHerkunftSollBesitzerVonSessionNehmen() throws KeinZugriffException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        commandLineHerkunft = new CommandLineHerkunft(session);
        assertEquals(benutzerId, commandLineHerkunft.getBesitzerId());
    }

    @Test
    void commandLineHerkunftSollScheiternWennKeinBenutzerAngemeldetIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> new CommandLineHerkunft(session));
    }

    @Test
    void commandLineHerkunftSollEqualsNurAufBasisVonBesitzerVergleichen() throws KeinZugriffException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        commandLineHerkunft = new CommandLineHerkunft(session);
        commandLineHerkunft.setId(herkunftId);
        assertEquals(commandLineHerkunft, new CommandLineHerkunft(session));
    }

}
