package Branch.Shokora.Frontend.CLI;

import java.util.*;

/**
 *
 * @author shokora
 * @todo Remove the argcount and add a list of parameters the command has.
 * These parameters will be added to a list and the command call will be checked for
 * an instance of these parameters. Then the list of parameters will be send to the run command
 * so the run command itself doesn't have to do any parameter checking.
 */
public abstract class Command
{
    private int argCount;
    private String description, name;

    public Command(String name, int argCount, String description)
    {
        this.name = name;
        this.argCount = argCount;
        this.description = description;
    }

    public String getToken()
    {
        return name;
    }

    public int getArgCount()
    {
        return argCount;
    }

    public String toString()
    {
        return description;
    }

    public boolean validizeCall(String token, ArrayList<String> args) //variable amount of arguments
    {
        return this.name.equals(token) && (argCount == -1 || args.size() == argCount);
    }

    public abstract void run(ArrayList<String> args);
}
