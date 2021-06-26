package commands;

import collection.Ticket;

import java.util.Date;
import java.util.LinkedList;

/**
 * Command class which outputs info about collection
 */
public class InfoCommand extends CommandWithoutAdditionalArgument {


    /**
     * Output info about collection (type, count of elements, creation time)
     */
    public String execute() {
        String result;
        result = "Тип коллекции: " + getCollection().getClass() + "\n";
        result += "Количество элементов: " + getCollection().size() + "\n";
        if (getCollection().size() != 0 ) {
            Date date = new Date();
            for (Ticket t : getCollection()) {
                if (t.getDateOfCreation().getTime() < date.getTime()) date = t.getDateOfCreation();
            }
            result += "Время создания: " + date;
        }
        return result;
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "info : вывести в стандартный поток вывода информацию о коллекции.";
    }
}
