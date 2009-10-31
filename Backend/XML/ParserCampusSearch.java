package Backend.XML;

import Backend.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;

/**
 *
 * @author shokora
 *     This file is part of Prometheus.

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
public class ParserCampusSearch extends XMLParser
{
    private ArrayList<SearchResult> searchResults;

    public ParserCampusSearch(InputStream in) throws Exception
    {
        super(in);
        searchResults = new ArrayList<SearchResult>();
        parseDocument();
    }

    public void parseDocument() throws Exception
    {
        //get the root element
        Element docElement = dom.getDocumentElement();

        //Get a nodelist of tvshows
        NodeList nl = docElement.getElementsByTagName("result");

        if(nl != null && nl.getLength() > 0)
        {
            for(int i=0;i<nl.getLength();i++)
            {
                    Element el = (Element)nl.item(i);
                    String netbiosName = getTextValue(el,"netbios_name");
                    String directory = getTextValue(el,"dir");
                    String filename = getTextValue(el,"name");
                    String ip = getTextValue(el,"ip");
                    String type = getTextValue(el,"type");
                    String fullPath = getTextValue(el,"full_path");
                    String name = getTextValue(el,"name");
                    String online = getTextValue(el,"online");
                    long size = getLongValue(el,"size");

                    boolean blOnline = online.equals("yes");
                    searchResults.add(new SearchResult(netbiosName,directory,fullPath,filename,type,ip,size,blOnline));
            }
        }
    }

    public ArrayList<SearchResult> getSearchResults()
    {
        return searchResults;
    }
}
