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


/**
 *
 * @author shokora
 * @author mrijke
*/
public class SearchResult
{
    String netbiosName, directory, filename, fullPath, type;
    String ip;
    long size;
    boolean online;

    /**
     * @param netbiosName
     * @param directory
     * @param fullPath
     * @param type
     * @param ip
     * @param size
     * @param online
     */
    public SearchResult(String netbiosName, String directory, String fullPath, String filename, String type, String ip, long size, boolean online)
    {
        this.netbiosName = netbiosName;
        this.directory = directory;
        this.fullPath = fullPath;
        this.type = type;
        this.ip = ip;
        this.size = size;
        this.online = online;
        this.filename = filename;

        if(type.equals("directory")) this.fullPath += "/";
    }

    public String getSizeReadable()
    {
        if((size/1000000000)>=1)
        {
            return String.valueOf((float)(size/1000000000)+"GB");
        }
        else if((size/1000000)>=1)
        {
            return String.valueOf((float)(size/1000000)+"MB");
        }

        return String.valueOf(size+"KB");
    }


    public String getNetbiosName()
    {
        return netbiosName;
    }

    public String getDirectory()
    {
        return directory;
    }

    public String getFilename()
    {
        return filename;
    }

    public String getFullPath()
    {
        return fullPath;
    }

    public String getIP()
    {
        return ip;
    }

    public long getSize()
    {
        return size;
    }

    public boolean getOnline()
    {
        return online;
    }

    public String getType()
    {
        return type;
    }
    
    public String toString()
    {
        return "NetbiosName: "+netbiosName+"Directory: "+directory+"\nFull Path: "+fullPath+"\nType: "+type+"\nIP: "+ip+"\nSize: "+size+"\nOnline: "+String.valueOf(online)+"\n";
    }
}
