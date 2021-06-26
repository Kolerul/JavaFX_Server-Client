package mainPart;

import commands.*;
import exceptions.IllegalCountOfArgumentsException;
import collection.Ticket;

import java.io.Serializable;
import java.util.*;

/**
 * Commands decoder class that which processes incoming commands and controls collection
 */
public class CommandDecoder implements Serializable {
    /**hash map with all commands and their classes*/
    private final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();
    /**collection of tickets*/
    private final LinkedList<Ticket> c;

    /**
     * Constructor without parameters
     */
    public CommandDecoder() {
        c = new LinkedList<>();
        addCommands();
    }

    /**
     * Constructor with parameter
     * @param c - collection of tickets
     */
    public CommandDecoder(LinkedList<Ticket> c) {
        this.c = c;
        addCommands();
    }

    /**
     * Put commands to the hash map
     */
    private void addCommands() {
        commands.put("help", new HelpCommand(commands));
        commands.put("info", new InfoCommand());
        commands.put("show", new ShowCommand());
        commands.put("add", new AddCommand());
        commands.put("update", new UpdateCommand());
        commands.put("remove_by_id", new RemoveByIdCommand());
        commands.put("clear", new ClearCommand());
        commands.put("execute_script", new ExecuteScriptCommand());
        commands.put("remove_head", new RemoveHeadCommand());
        commands.put("add_if_max", new AddIfMaxCommand());
        commands.put("remove_greater", new RemoveGreaterCommand());
        commands.put("sum_of_price", new SumOfPriceCommand());
        commands.put("filter_greater_than_price", new FilterGreaterThanPriceCommand());
        commands.put("print_field_descending_type", new PrintFieldDescendingTypeCommand());
    }

    /**
     * Command decoder
     * @param com - incoming command
     */
    public Command decode(String com) {

            String[] s, s1;
            s = com.split(" ");
            s1 = com.split("\t");
            if (s1.length > s.length) s = s1;
            if (s.length == 0) throw new NullPointerException();
            if (!s[0].equals("exit")) {
                int countOfArguments = s.length - 1;
                Command cd = commands.get(s[0].toLowerCase());
                try {
                    if (cd.correctCountOfArguments(countOfArguments))  {
                        if (countOfArguments == 1) ((CommandWithAdditionalArgument) cd).addArgument(s[1]);
                        return cd;
                    } else throw new IllegalCountOfArgumentsException();
                } catch (NumberFormatException e) {
                    System.out.println("Аргумент имеет неправльный тип (для id - int, для price - double)");
                }

            }
        return null;
    }

    /**
     * Collection of tickets sorter by price and id
     * @param c - collection of tickets
     */
    public static void sort(LinkedList<Ticket> c) {
        Comparator<Ticket> comparator = Comparator.comparing(Ticket::getPrice).thenComparing(Ticket::getId);
        c.sort(comparator);
    }

    /**
     * Getter {@link CommandDecoder#c}
     * @return collection of tickets
     */
    public LinkedList<Ticket> getCollection() {
        return c;
    }

}
