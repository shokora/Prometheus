package Backend.XML;

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

import Backend.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.w3c.dom.*;

public class ParserEpisode extends XMLParser
{
    private ArrayList<String> episodesString;
    private ArrayList<Episode> episodes;

    public ParserEpisode(InputStream in)
    {
        super(in);
        episodesString = new ArrayList<String>();
        episodes = new ArrayList<Episode>();
        parseDocument();
    }

    public void parseDocument()
    {
        //get the root element
        Element docElement = dom.getDocumentElement();

        //Get a nodelist of tvshows
        NodeList nl = docElement.getElementsByTagName("item");

        if(nl != null && nl.getLength() > 0)
        {
            for(int i=0;i<nl.getLength();i++)
            {
                //Get the item element
                Element el = (Element)nl.item(i);

                String penis = getEpisode(el);

                episodesString.add(penis);
            }
        }
    }

    public ArrayList<String> getEpisodesString()
    {
        return episodesString;
    }

    public ArrayList<Episode> getEpisodes()
    {
        return episodes;
    }

        /**
     * Gets the actual information from the element, only takes title now but
     * it will take more later.
     * @param el
     */
    public String getEpisode(Element el)
    {
        //for each <item> get title
        return getTextValue(el,"title");
    }

    public void formatEpisodes()
    {
        Pattern p = Pattern.compile("- (.*) \\(([0-9]+)x([0-9]+)\\)");
        ArrayList<Episode> episodesNew = new ArrayList<Episode>();

        for(String episodeString : episodesString)
        {
            Matcher m = p.matcher(episodeString);

            if(m.find())
            {
                String series = m.group(1);
                int season = Integer.valueOf(m.group(2));
                int episode = Integer.valueOf(m.group(3));


                episodesNew.add(new Episode(series,season,episode));
            }
        }

        episodes = episodesNew;
    }
}
