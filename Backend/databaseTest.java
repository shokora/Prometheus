package Branch.Shokora.Backend;

import java.util.regex.*;
import java.sql.*;
/**
 *
 * @author shokora
 * @date May 18, 2009
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
