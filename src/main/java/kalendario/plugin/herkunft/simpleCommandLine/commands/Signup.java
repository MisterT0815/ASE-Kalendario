package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.crud.benutzer.BenutzerNameExistiertException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.PasswortHasher;
import kalendario.domain.repositories.SaveException;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import javax.security.auth.login.LoginException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Signup implements Command {

    private Session session;
    public Signup(Session session){
        this.session = session;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws LoginException, NoSuchAlgorithmException, SaveException, BenutzerNameExistiertException {
        if(command.equals("signup")){
            if(parameters.size()<2){
                throw new LoginException("Login braucht mindestens 2 Parameter: Benutzername und Passwort");
            }
            session.signUp(
                    parameters.get(0).asText(),
                    new PasswortHasher().hashPasswort(parameters.get(1).asText())
            );
            return true;
        }
        return false;
    }
}
