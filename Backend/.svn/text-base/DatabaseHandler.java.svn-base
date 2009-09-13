/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Branch.Shokora.Backend;


import java.sql.*;
/**
 * The database this project uses is SQLite because no server is needed.
 * @author shokora
 */
public class DatabaseHandler
{
    private Connection conn;
    private Statement stat;

    public DatabaseHandler()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:prometheus.db");
            Statement stat = conn.createStatement();
            init();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void init() throws SQLException
    {
         stat.executeQuery("CREATE TABLE IF NOT EXISTS series(id,name,season,episode");
    }

    public void addSeries(Series serie) throws SQLException
    {
         PreparedStatement prep = conn.prepareStatement("INSERT INTO series(?,?,?);");
         prep.setString(1, serie.getSeriesName());
         prep.setInt(2, serie.getSeason());
         prep.setInt(3, serie.getEpisode());
    }

    public void editSeries(Series serie) throws SQLException
    {
        PreparedStatement prep = conn.prepareStatement("UPDATE ");
    }
}
