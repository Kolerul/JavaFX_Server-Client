package commands;

import java.util.HashMap;

/**
 * Command class that outputs other commands
 */
public class HelpCommand extends CommandWithoutAdditionalArgument {
    /**hash map with all commands and their classes*/
    private final HashMap<String, Command> h;

    /**
     * Constructor with parameter
     * @param h - collection of commands
     */
    public HelpCommand(HashMap<String, Command> h) {this.h = h;}

    /**
     * Output other commands
     */
    @Override
    public String execute() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Command com : h.values()) {
           stringBuilder.append(com).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "help : вывести справку по доступным командам";
    }
}
