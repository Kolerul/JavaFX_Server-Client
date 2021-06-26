package commands;

public class SendTicketDataCommand extends CommandWithoutAdditionalArgument {

    public String data;

    public SendTicketDataCommand(String data) {
        this.data = data;
    }
}
