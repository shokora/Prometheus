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
 *
 * @author shokora
 * @author mrijke
 */
public class databaseTest {
    
    public static void main(String[] args)
    {
        try
        {
                  Class.forName("org.sqlite.JDBC");
                  Connection conn = DriverManager.getConnection("jdbc:sqlite:prometheus.db");
                  Statement stat = conn.createStatement();

                  //stat.execute("drop table if exists neger;");

                  stat.execute("CREATE TABLE IF NOT EXISTS neger(naam,kleur)");
                  PreparedStatement prep = conn.prepareStatement("INSERT INTO neger values(?,?);");

                  prep.setString(1,"homo");
                  prep.setString(2, "diep bruin");
                  prep.addBatch();
                  prep.setString(1,"biggie");
                  prep.setString(2,"kanker zwart");
                  prep.addBatch();
                  prep.setString(1, "2pac");
                  prep.setString(1, "lafjes getint");
                  prep.addBatch();

                  conn.setAutoCommit(false);
                  prep.executeBatch();
                  conn.setAutoCommit(true);

                  ResultSet rs = stat.executeQuery("SELECT * FROM neger");
                  while(rs.next())
                  {
                      System.out.printf("name:%s\nkleur:%s\n",rs.getString("naam"),rs.getString("kleur"));
                      System.out.flush();
                  }

                  rs.close();
                  conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}
