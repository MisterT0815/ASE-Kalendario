package kalendario.plugin.herkunft.simpleCommandLine;

import kalendario.application.session.Session;
import kalendario.plugin.herkunft.simpleCommandLine.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleCommandLine {

    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    List<Command> commands;

    public SimpleCommandLine(Session session, List<Command> commands){
        this.commands = commands;
    }

    public void run() throws IOException {
        System.out.println("Start");
        while(true){
            String line = input.readLine();
            String[] words = line.split(" ");
            List<Parameter> parameterList = new ArrayList<>();
            for(int i = 1; i< words.length; i++){
                parameterList.add(new Parameter(words[i]));
            }
            if(words[0].equals("exit")){
                System.exit(0);
            }
            if(!commands.stream().map(command -> {
                try {
                    return command.executeWithParameters(words[0], parameterList);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return true;
                }}
                ).anyMatch(commandGefunden -> commandGefunden)){
                System.out.println("\nBefehl wurde nicht erkannt!\n");
            }
        }
    }

}
