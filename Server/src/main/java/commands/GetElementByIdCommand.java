package commands;

public class GetElementByIdCommand extends CommandWithAdditionalArgument{

    public Integer id;

    @Override
    public void addArgument(String obj) {
        id = Integer.parseInt(obj);
    }
}
