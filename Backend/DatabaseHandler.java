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


import java.sql.*;
/**
 * The database this project uses is SQLite because no server is needed.
 * @author shokora
 * @author mrijke
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
