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
import java.util.regex.*;
import java.util.*;
import java.io.*;

/**
 * @author shokora
 * @author mrijke
 */
public class SDirectory
{
    String url;
    SmbFile smbFile;
    String downloadDir;
    
    public SDirectory(String url) throws Exception
    {
        this.url = url;

        try
        {
            smbFile = new SmbFile(url);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        if(!smbFile.isDirectory()) throw new Exception("This is not a directory");

        init();
    }

    public SDirectory(SmbFile smbFile) throws Exception
    {
        this.smbFile = smbFile;
        this.url = smbFile.getPath();

        if(!smbFile.isDirectory()) throw new Exception("This is not a directory");

        init();
    }

    public void init()
    {
        try
        {
            File configFile = new File("config.properties");
            InputStream input = new BufferedInputStream(new FileInputStream(configFile.getAbsoluteFile()));

            Properties configProperties = new Properties();
            configProperties.load(input);
            downloadDir = configProperties.getProperty("downloadDir",".")+"/"+this.getSMBFile().getName();
            System.out.println(configFile.getAbsoluteFile());
        }
        catch(FileNotFoundException e)
        {
        	//File doesn't exist, assume the default value for downloadDir: "."
            downloadDir = ".";
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }

    /**
     * Give a list of files in the current directory, ready to be used by smbFiles
     * @param recursive - list files in directories on deeper levels too
     * @param cut - list files without parentdirs in front of it
     * @return
     * @throws java.lang.Exception
     */
    public ArrayList<String> listFiles(boolean recursive, boolean cut) throws Exception
    {
        SmbFile[] files = new SmbFile[0];
        ArrayList<SmbFile> dirs = new ArrayList<SmbFile>();
        ArrayList<String> fileList = new ArrayList<String>();
        int a = 0;
        
        dirs.add(smbFile); //add this directory to the directory list
        
        while(a < dirs.size())
        {
            try
            {
                files = dirs.get(a).listFiles();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

                for(int i=0;i<files.length;i++)
                {
                    //if the file is a directory add it to the directory list so we can list it
                    if(recursive &&  files[i].isDirectory()) dirs.add(files[i]);
                    
                    if(cut)
                    {
                        fileList.add(files[i].getName());
                    }
                    else
                    {
                        fileList.add(dirs.get(a)+files[i].getName());
                    }
                }
            a++;
        }
        return fileList;
    }

    /**
     * Generates a list of all the files in a certain directory and is able to do that
     * recursively. Then will check every filename with regex. Will make a better algorithem
     * in the future, this is just something that works.
     * @param searchPattern
     * @param recursive
     * @return if nothing is found null else the list of SmbFiles
     * @throws java.lang.Exception
     */
    public ArrayList<SmbFile> search(String searchPattern, boolean recursive) throws Exception
    {
        ArrayList<String> fileList;
        ArrayList<SmbFile> smbList = new ArrayList<SmbFile>();

        try
        {
            if(recursive)
            {
                fileList = listFiles(true,false);
            }
            else
            {
                fileList = listFiles(false,false);
            }
        }
        catch(Exception e){throw e;}
        
        Pattern pattern = Pattern.compile(searchPattern);
        for(int i=0;i<fileList.size();i++)
        {
            Matcher matcher = pattern.matcher(fileList.get(i).toLowerCase()); //cast to lowercase to stop it whining about case
            if(matcher.find())
            {
                smbList.add(new SmbFile(fileList.get(i)));
            }
        }

        return smbList;
    }

    /**
     * Make the directory and download all the files in the directory
     * @param recursive - will recursively download all the files and directories in the directory to all sublevels
     */
    public void get(boolean recursive)
    {
        try
        {
            ArrayList<String> fileList = listFiles(false,false);

            new File(downloadDir).mkdir();

            for(String file : fileList)
            {
                SFile smbFile = new SFile(file);
                smbFile.setDownloadDir(downloadDir);

                if(smbFile.getSMBFile().isFile())
                {
                    System.out.println("Getting file: "+smbFile.getSMBFile().getName());
                    smbFile.get();
                    System.out.println("Download is done");
                }
                else if(recursive && smbFile.getSMBFile().isDirectory())
                {
                    SDirectory temp = new SDirectory(file);
                    temp.setDownloadDir(downloadDir+"/"+temp.getSMBFile().getName());
                    temp.get(recursive);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }

    public String getURL()
    {
        return url;
    }

    /**
     * Removes the protocol and the server name from a path
     * @param path
     * @return
     */
    public String cutPath(String path)
    {
        return path.substring(("smb://"+smbFile.getServer()).length());
    }

    /**
     * Returns the cut path of this directory object
     * @return
     */
    public String getCutPath()
    {
        return cutPath(smbFile.getPath());
    }

    public SmbFile getSMBFile()
    {
        return smbFile;
    }

    public void setDownloadDir(String downloadDir)
    {
        this.downloadDir = downloadDir;
    }

    public String getDownloadDir()
    {
        return downloadDir;
    }
}
