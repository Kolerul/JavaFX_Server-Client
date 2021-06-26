package commands;

import collection.Ticket;

import java.util.LinkedList;

/**
 * Command class that clears the collection
 */
public class ClearCommand extends CommandWithoutAdditionalArgument{


    /**
     * Clear the collection
     */
    @Override
    public String execute(String userName) {
        if (getCollection().removeIf((t) -> t.getOwner().equals(userName))) {
            setCollectionChanged(true);
            return "Часть вашей коллекции, принадлежащей вам, была удалена.";
        }
        return "Не нашлось билетов, принадлежащих вам.";
}

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "clear : очистить коллекцию";
    }
}
