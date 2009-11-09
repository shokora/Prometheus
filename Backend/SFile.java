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

import jcifs.smb.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author shokora
 * @author mrijke
 * @description This represents a file from the network
*/
public class SFile
{
    String url;
    String downloadDir;
    SmbFile smbFile;
    SmbFileInputStream in;
    FileOutputStream out;
    Timer timer;

    public SFile(String url)
    {
        this.url = url;
        try
        {
            smbFile = new SmbFile(url);
            init();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public SFile(SmbFile smbFile)
    {
        this.smbFile = smbFile;
        try
        {
            init();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void init() throws Exception
    {
        in = new SmbFileInputStream(smbFile);

        try
        {
            File configFile = new File("config.properties");
            InputStream input = new BufferedInputStream(new FileInputStream(configFile.getAbsoluteFile()));

            Properties configProperties = new Properties();
            configProperties.load(input);
            downloadDir = configProperties.getProperty("downloadDir",".");
        }

        catch(Exception e)
        {
            System.out.println(e.toString());
        	System.out.println("Error: "+e.getMessage());
        }
    }

    public void get() throws Exception
    {
        byte[] b = new byte[8192];
        int n = 0,t = 0; //t == total amount of downloaded bytes
        long length = smbFile.length(), initialTime, currentTime;
        float percentage;

        out = new FileOutputStream(downloadDir+"/"+smbFile.getName());

        Date date = new Date();
        initialTime = date.getTime();

        while((n = in.read(b)) > 0)
        {
            t += n;
            percentage = ( (float) t / (float) length) * 100;
            currentTime = new Date().getTime();

            float iets = (float) t;
            float tijd = (float) ((currentTime - initialTime)+1);
            float speed = iets/tijd;

            System.out.printf("%3.2f   %f KB/s\r",percentage,speed);
            System.out.flush();
            out.write(b);
        }

        out.close();
        System.out.println();
    }

    public SmbFile getSMBFile()
    {
        return smbFile;
    }

    public void setDownloadDir(String local)
    {
        this.downloadDir = local;
    }

    public String getDownloadDir()
    {
        return this.downloadDir;
    }
}
