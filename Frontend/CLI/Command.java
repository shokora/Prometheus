package Branch.Shokora.Frontend.CLI;

import java.util.*;

/**
 *
 * @author shokora
 */
public abstract class Command
{
    private int argCount;
    private String description, token;

    public Command(String token, int argCount, String description)
    {
        this.token = token;
        this.argCount = argCount;
        this.description = description;
    }

    public String getToken()
    {
        return token;
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
        return this.token.equals(token) && (argCount == -1 || args.size() == argCount);
    }

    public abstract void run(ArrayList<String> args);
}
