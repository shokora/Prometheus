package Branch.Shokora.Backend;

import jcifs.smb.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author shokora
 * @date May 7, 2009
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
          Properties configFile = new Properties();
          configFile.load(this.getClass().getResourceAsStream("config.properties"));
          downloadDir = configFile.getProperty("downloadDir",".");
        }
        catch(Exception e)
        {
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

    public void setLocal(String local)
    {
        this.downloadDir = local;
    }
}
