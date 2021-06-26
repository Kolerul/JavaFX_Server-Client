package commands;

import collection.Ticket;

import java.util.LinkedList;

/**
 * Command class which outputs elements in the collection
 */
public class ShowCommand extends CommandWithoutAdditionalArgument{


    @Override
    public String execute() {
        StringBuilder result = new StringBuilder();
        getCollection().forEach(t -> result.append(t).append("\n"));
        if (getCollection().size() != 0) return result.toString();
        return "Нечего показывать";
    }

    @Override
    public String toString() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
