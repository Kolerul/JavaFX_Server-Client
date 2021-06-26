package commands;

import collection.Ticket;

import java.util.LinkedList;

/**
 * Command class that outputs the types of tickets in collection in descending order
 */
public class PrintFieldDescendingTypeCommand extends CommandWithoutAdditionalArgument{


    /**
     * Output types of tickets in collection in descending order
     */
    @Override
    public String execute() {
        StringBuilder s = new StringBuilder();
        getCollection().stream().filter(t -> t.getType() != null).forEach(t -> s.insert(0, t.getType() + "\n"));
        return s.toString();
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "print_field_descending_type : вывести значения поля type всех элементов в порядке убывания";
    }
}
