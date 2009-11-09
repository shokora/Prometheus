/*
    This file is part of Prometheus.

    Prometheus is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Prometheus is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Prometheus.  If not, see <http://www.gnu.org/licenses/>.
*/

package Frontend.CLI;

import java.util.*;

/**
 *
 * @author shokora
 * @author mrijke
 */
public abstract class Command
{
    private String description, name, parameters;
    private HashMap<String,String> parametersMap;

    public Command(String name, String parameters, String description)
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
            String[] splitted = st.nextToken().split(";");
            parametersMap.put(splitted[0].substring(1), splitted[1]); //substring(1) to remove the leading -
        }
    }

    /**
     * It might seem ugly to use String,String even though we are going to save
     * other kinds of objects in the map. But because this is a general function
     * we have no way of knowing what kind of objects are going to be used as values
     * that's why we just save it as Strings and later convert it to the right type.
     *
     * It will return all the Strings with their param name as key and their value as value.
     * The rest will be returned with key Base and the rest of the values with spaces between them.
     * For instance "search -dirsonly -page 2 family guy" will be:
     * dirsonly -> true
     * page -> 2
     * base -> family guy
     * @param args
     */
    public HashMap<String,String> fillParameters(ArrayList<String> args)
    {
        HashMap<String,String> parametersCurrent = new HashMap<String,String>();
        int counter = 0; //counts when the last parameter is parsed so we know what the base call is

        for(int i=0;i<args.size();i++)
        {
            String argument = args.get(i).substring(1);
            if(parametersMap.containsKey(argument)) //substring(1) to remove the leading -
            {
                //If the value is 0 that means it's a boolean and it does not have a parameter
                if(parametersMap.get(argument).equals("0"))
                {
                    parametersCurrent.remove(argument);
                    parametersCurrent.put(argument, "true");
                    counter++;
                }
                else
                {
                   parametersCurrent.remove(argument);
                   parametersCurrent.put(argument, args.get(++i));
                   counter+=2;
                }
            }
        }

        Set<String> keySet = parametersMap.keySet();
        Iterator it = keySet.iterator();

        while(it.hasNext())
        {
            String key = (String) it.next();
            if(parametersCurrent.get(key) == null) //check if the key is in the set
            {
                parametersCurrent.put(key, ""); //if not put it in there with an empty string as value
            }
        }


        String baseCall = "";
        for(int a=counter;a<args.size();a++)
        {
            baseCall += " "+args.get(a);
        }

        if(baseCall.length() > 0) baseCall = baseCall.substring(1);
        parametersCurrent.put("base", baseCall); //substring(1) to remove the first leading space

        return parametersCurrent;
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
