package commands;

import collection.Ticket;

public class GetAllIdsCommand extends CommandWithoutAdditionalArgument {
    @Override
    public String execute() {
        StringBuilder s = new StringBuilder();
        for (Ticket t : getCollection()) s.append(t.getId() +"\n");
        return s.toString();
    }
}
