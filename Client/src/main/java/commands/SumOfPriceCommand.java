package commands;
import collection.Ticket;

import java.util.LinkedList;

/**
 * Command class that outputs sum of the ticket price
 */
public class SumOfPriceCommand extends CommandWithoutAdditionalArgument{


    /**
     * Output sum of the ticket price
     */
    @Override
    public String execute() {
        double k = getCollection().stream().mapToDouble(Ticket::getPrice).sum();
        return "Общая стоимость билетов: " + k;
    }

    /**
     * @return info about command
     */
    @Override
    public String toString() {
        return "sum_of_price : вывести сумму значений поля price для всех элементов коллекции";
    }
}
