package commands;

import collection.Ticket;
import exceptions.IdNotFoundException;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Command class that remove tickets from the collection greater than given one
 */
public class RemoveGreaterCommand extends CommandWithAdditionalArgument{
    /**ticket name*/
    private int id;

    /**
     * Remove tickets from the collection greater than given one
     */
    @Override
    public String execute(String userName) {
        Ticket ticket;
        try {
            ticket = getCollection().stream().filter(t -> t.getId() == id && t.getOwner().equals(userName)).collect(Collectors.toList()).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IdNotFoundException();
        }
        Ticket finalTicket = ticket;
        if (getCollection().removeIf(i -> i.getPrice() > finalTicket.getPrice() && i.getOwner().equals(userName))) {
            setCollectionChanged(true);
            return "Сколько-то элементов было удалено";
        }
        return "К сожалению, ничего удалить не удалось";
    }

    /**
     * Getting ticket name
     * @param obj - ticket name
     */
    @Override
    public void addArgument(String obj) {
        id = Integer.parseInt(obj);
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "remove_greater <ticket id> : удалить из коллекции все элементы, превышающие заданный";
    }
}
