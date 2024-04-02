package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.session.Session;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.List;

public class Logout implements Command {

    Session session;

    public Logout(Session session) {
        this.session = session;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("logout")){
            session.logout();
            return true;
        }
        return false;
    }
}
