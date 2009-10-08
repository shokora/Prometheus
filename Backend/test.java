/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Branch.Shokora.Backend;

import Branch.Shokora.Frontend.CLI.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class test
{
    public static void main(String[] args)
    {
        new test();
    }

    public test()
    {
        Search search = new Search();
        ArrayList<String> call = new ArrayList<String>();
        call.add("-dirsonly");
        call.add("-minsize");
        call.add("1GB");
        call.add("family");
        call.add("guy");
        search.run(call);
    }

    private class Search extends Command
    {
        public Search()
        {
            super("search","-dirsonly,-maxsize,-minsize","A command to search campussearch");
        }

        @Override
        public void run(ArrayList<String> args)
        {

         HashMap<String,String> parametersCurrent = fillParameters(args);

            System.out.println("base "+parametersCurrent.get("base").replace(" ", "+")+" dirsonly "+parametersCurrent.get("dirsonly")+" maxsize "+parametersCurrent.get("-maxsize")+" minsize "+parametersCurrent.get("minsize"));
        }

    }
}
