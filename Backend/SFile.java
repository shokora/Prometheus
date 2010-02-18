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
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author shokora
 * @description This represents a file from the network
 *
 * @todo change the run exception shit
*/
public class SFile extends Thread
{
    private String url, downloadDir;
    private long timeLeft;
    private double speed;
    private float percentage;
    private Boolean downloading = false;
    private SmbFile smbFile;
    private SmbFileInputStream in;
    private FileOutputStream out;
    private Timer timer;

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

    public void run()
    {
        try
        {
            this.downloading = true;
            byte[] b = new byte[8192];
            int n = 0,t = 0; //t == total amount of downloaded bytes
            long length = smbFile.length(), initialTime, currentTime;

            out = new FileOutputStream(downloadDir+"/"+smbFile.getName());

            Date date = new Date();
            initialTime = date.getTime();

            while((n = in.read(b)) > 0)
            {
                t += n;
                this.percentage = ( (float) t / (float) length) * 100;
                currentTime = new Date().getTime();

                float tijd = (float) ((currentTime - initialTime)+1);
                this.speed = (double) ((double) t/(double) tijd)*1000;
                this.timeLeft = (((long) length - (long) t) / (long) speed);

                out.write(b);
            }

            this.downloading = false;
            out.close();
            return;
        }
        catch(IOException e)
        {
            System.out.println("Error: "+e.getMessage()); //this is really bad and should change
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

    public void get()
    {
        this.run();
    }

    /**
     * This should probably go in a more general library since more files will probably use this
     * @param time
     */
    public String makeTimeReadable(long time)
    {
        String readableTime = "";
        long orgtime = time;
        if(time/3600 >= 1)
        {
            time = time % 3600;
            readableTime += "H:"+(orgtime-time)/3600+" ";
            orgtime = orgtime % 3600;
        }
        else if(time/60 >= 1)
        {
            time = time % 60;
            readableTime += "M:"+(orgtime-time)/60+" ";
        }

        readableTime += "S:"+time;
        readableTime += "               "; //Return doesn't clear the whole line

        return readableTime;
    }

    public String makeSpeedReadable(double speed)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        String readableSpeed = "";
        if(speed/1000000000 >= 1)
        {
            readableSpeed = twoDForm.format(speed/1000000000)+" GB/s"; //If you are ever going to need this you have one hell of a connection
        }
        else if(speed/1000000 >= 1)
        {
            readableSpeed = twoDForm.format(speed/1000000)+" MB/s";
        }
        else if(speed/1000 >= 1)
        {
            readableSpeed = twoDForm.format(speed/1000)+" KB/s";
        }
        else
        {
            readableSpeed = twoDForm.format(speed)+" B/s"; //If you are ever going to need this... good luck
        }

        return readableSpeed;
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

    public long getTimeLeft() throws Exception
    {
        if(downloading)
        {
            return this.timeLeft;
        }

        throw new Exception("This file is not being downloaded right now");
    }

    public double getSpeed() throws Exception
    {
        if(downloading)
        {
            return this.speed;
        }

        throw new Exception("This file is not being downloaded right now");
    }

    public float getPercentage() throws Exception
    {
        if(downloading)
        {
            return percentage;
        }

        throw new Exception("This file is not being downloaded right now");
    }

    public boolean getDownloading()
    {
        return this.downloading;
    }
}
