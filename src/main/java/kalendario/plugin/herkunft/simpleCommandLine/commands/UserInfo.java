package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.session.Session;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.List;

public class UserInfo implements Command {

    private Session session;
    public UserInfo(Session session){
        this.session = session;
    }
    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("userinfo")){
            if(session.getCurrentBenutzer().isPresent()){
                System.out.println("Benutzer ist angemeldet");
                System.out.println("Name: " + session.getCurrentBenutzerName().orElseThrow(()-> new Exception("Fehler beim holen des Namens")));
                System.out.println("ID: " + session.getCurrentBenutzer().get().getId().toString());
            }else{
                System.out.println("Kein Benutzer ist angemelted");
            }
            return true;
        }
        return false;
    }
}
