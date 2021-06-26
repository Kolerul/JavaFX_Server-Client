package commands;

import java.util.LinkedList;

/**
 * Basic interface for all commands with additional argument
 */
public abstract class CommandWithAdditionalArgument extends CommandWithoutAdditionalArgument{

    /**
     * Add additional argument to command field
     * @param obj - additional argument
     */
    public void addArgument(String obj) {};

    /**
     * @param countOfArguments - count of arguments
     * @return true if count of arguments = 1, else false
     */
    @Override
    public boolean correctCountOfArguments(int countOfArguments) {
        return countOfArguments == 1;
    }
}
