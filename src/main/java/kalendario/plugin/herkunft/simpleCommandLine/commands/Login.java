package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.PasswortHasher;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import javax.security.auth.login.LoginException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Login implements Command {

    private Session session;
    public Login(Session session) {
        this.session = session;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws NoSuchAlgorithmException, LoginException {
        if(command.equals("login")){
            if(parameters.size()<2){
                throw new LoginException("Login braucht mindestens 2 Parameter: Benutzername und Passwort");
            }
            session.login(
                    parameters.get(0).asText(),
                    new PasswortHasher().hashPasswort(parameters.get(1).asText())
            );
            return true;
        }
        return false;
    }


}
