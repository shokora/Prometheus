package Branch.Shokora.Backend;

/**
 *
 * @author shokora
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
