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

import java.util.regex.*;

/**
 *
 * @author shokora
 * @author mrijke
 */
public class Series
{
    private String seriesName, regex = "";
    private int season, episode;

    public Series(String seriesName, int season, int episode)
    {
        this.seriesName = seriesName;
        this.season = season;
        this.episode = episode;
        createRegex();
    }

    public Series(String seriesName)
    {
        this.seriesName = seriesName;
        this.season = 1;
        this.episode = 1;
    }

    public String getSeriesName()
    {
        return seriesName;
    }

    public int getSeason()
    {
        return season;
    }

    public int getEpisode()
    {
        return episode;
    }

    public String getRegex()
    {
        return regex;
    }

    public void setSeason(int season)
    {
        this.season = season;
    }

    public void setEpisode(int episode)
    {
        this.episode = episode;
    }

    public void createRegex()
    {
        String[] nameSplit = Pattern.compile(" ").split(seriesName);

        for(int i=0;i<nameSplit.length;i++)
        {
            regex += nameSplit[i].toLowerCase()+".*";
        }

        regex += this.season+".*";
        regex += this.episode+".*";
    }
}
