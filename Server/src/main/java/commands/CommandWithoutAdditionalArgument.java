package commands;

import collection.Ticket;

import java.util.LinkedList;

/**
 * Basic interface for all commands without additional argument
 */
public abstract class CommandWithoutAdditionalArgument implements Command{

    private LinkedList<Ticket> c;
    private boolean collectionChanged = false;


    public synchronized void updateCollection(LinkedList<Ticket> collection) {
        c = collection;
    }

    public synchronized LinkedList<Ticket> getCollection() {return c;}
    /**
     * @param countOfArguments - count of arguments
     * @return true if count of arguments = 0, else false
     */

    @Override
    public boolean correctCountOfArguments(int countOfArguments) {
        return countOfArguments == 0;
    }

    public boolean isCollectionChanged() {
        return collectionChanged;
    }

    public void setCollectionChanged(boolean collectionChanged) {
        this.collectionChanged = collectionChanged;
    }
}
