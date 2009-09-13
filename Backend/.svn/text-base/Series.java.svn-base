package Branch.Shokora.Backend;

import java.util.regex.*;

/**
 *
 * @author shokora
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
