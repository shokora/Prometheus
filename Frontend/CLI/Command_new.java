package Frontend.CLI;

import java.util.*;

/**
 *
 * @author shokora
 * @todo Remove the argcount and add a list of parameters the command has.
 * These parameters will be added to a list and the command call will be checked for
 * an instance of these parameters. Then the list of parameters will be send to the run command
 * so the run command itself doesn't have to do any parameter checking.
 */
public abstract class Command_new
{
    private String description, name, parameters;
    private HashMap<String,String> parametersMap;

    public Command_new(String name, String parameters, String description)
    {
        this.name = name;
        this.parameters = parameters;
        this.description = description;
        parametersMap = new HashMap<String,String>();
        getParameters();
    }

    public void getParameters()
    {
        StringTokenizer st = new StringTokenizer(parameters,",");

        while(st.hasMoreTokens())
        {
            parametersMap.put(st.nextToken(), "");
        }
    }

    /**
     * It might seem ugly to use String,String even though we are going to save
     * other kinds of objects in the map. But because this is a general function
     * we have no way of knowing what kind of objects are going to be used as values
     * that's why we just save it as Strings and later convert it to the right type.
     * @param args
     */
    public HashMap<String,String> fillParameters(ArrayList<String> args)
    {
        HashMap<String,String> parametersCurrent = parametersMap;

        for(int i=0;i<args.size();i++)
        {
            if(parametersCurrent.containsKey(args.get(i)))
            {
                parametersCurrent.remove(args.get(i));
                if(args.get(i+1).charAt(0) == '-')
                {

                    parametersCurrent.put(args.get(i).substring(1), "true");
                }
                else
                {
                   parametersCurrent.put(args.get(i).substring(1), args.get(++i));
                }
            }
        }

        return parametersMap;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return description;
    }

    public abstract void run(ArrayList<String> args);
}
