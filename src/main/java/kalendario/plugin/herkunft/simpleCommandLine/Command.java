package kalendario.plugin.herkunft.simpleCommandLine;

import javax.security.auth.login.LoginException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface Command {

    public abstract boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception;

}
