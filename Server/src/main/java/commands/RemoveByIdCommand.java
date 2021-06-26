package commands;

import collection.Ticket;
import exceptions.IdNotFoundException;

import java.util.LinkedList;

/**
 * Command class that outputs the ticket by its id and delete it
 */
public class RemoveByIdCommand extends CommandWithAdditionalArgument{
    /**ticket id*/
    private int ID;


    /**
     * Output the ticket by its id and delete it
     */
    @Override
    public String execute(String userName) {
        if (!getCollection().removeIf(i -> i.getId() == ID && i.getOwner().equals(userName))) throw new IdNotFoundException();
        setCollectionChanged(true);
        return "Билет с id: " + ID + " был успешно удалён.";
    }

    /**
     * Getting ticket id
     * @param obj - ticket id
     */
    @Override
    public void addArgument(String obj) {
        ID = Integer.parseInt(obj);
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "remove_by_id <id> : удалить элемент из коллекции по его id";
    }
}
