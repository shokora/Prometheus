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

/**
 * @author shokora
 * @author mrijke
 */
public class Episode
{
    String series;
    int episode, season;

    public Episode(String series, int season, int episode)
    {
        this.series = series;
        this.season = season;
        this.episode = episode;
    }

    public String getSeries()
    {
        return series;
    }

    public int getSeason()
    {
        return season;
    }

    public int getEpisode()
    {
        return episode;
    }

    public String toString()
    {
        return "Series: "+series+" Season: "+season+" Episode: "+episode;
    }
}
