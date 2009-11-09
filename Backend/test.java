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

package Backend;

import Frontend.CLI.*;
import java.util.*;

/**
 * @author shokora
 * @author mrijke
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
