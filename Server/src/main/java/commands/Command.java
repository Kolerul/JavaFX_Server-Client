package commands;

import java.io.Serializable;

/**
 * Basic interface for all commands
 */
public interface Command extends Serializable {
    /**
     * Method which executes the command
     */
    default String execute(String userName) {return execute();};
    default String execute() {return "";};

    /**
     * @param countOfArguments - count of arguments
     * @return true or false depending on the count of arguments
     */
    boolean correctCountOfArguments(int countOfArguments);
}

