package commands;
import collection.Ticket;
import mainPart.CommandDecoder;

import java.util.LinkedList;

/**
 * Command class that adds the element to the collection
 */
public class AddCommand extends CommandWithoutAdditionalArgument {

    public Ticket ticket = new Ticket();

    /**
     * add element to the collection
     */
    @Override
    public String execute() {
        getCollection().add(ticket);
        CommandDecoder.sort(getCollection());
        return "Новый элемент был успешно добавлен в коллекцию";
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "add : добавить новый элемент в коллекцию";
    }
}
