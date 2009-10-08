package Branch.Shokora.Frontend.CLI;

import java.util.*;
import Branch.Shokora.Backend.XML.*;
import Branch.Shokora.Backend.*;
import java.net.*;
import jcifs.smb.*;


/**
 *©Shokora 2009
 * @author shokora
 * @todo seperate all the command classes to make it more modulair and for suitable for usage in the GUI
 */
public class cli
{
    private ArrayList<Command> commandList;
    private ArrayList<SearchResult> onlineResults;
    private ArrayList<String> fileList;
    private SDirectory currentDir;
    private Scanner in;

    public static void main(String[] args)
    {
        new cli();
    }

    public cli()
    {
        commandList = new ArrayList<Command>();
        in = new Scanner(System.in);

        //add commands to the commandlist
        commandList.add(new Search("search","-dirsonly;0,-minsize;1,-maxsize;1,-page;1","Search on CampusSearch for the specified information"));
        commandList.add(new Get("get","-s;0,-r;0","Get a file from the search results"));
        commandList.add(new Open("open","-s;0","Open a network resource"));
        commandList.add(new ChangeDirectory("cd","","Change the current directory"));
        commandList.add(new List("ls","","Get the file list from a directory"));
        commandList.add(new Help("help","","Print this menu"));
        commandList.add(new Exit("exit","","Exit this application"));

        printMenu();

        while(true)
        {
            runCommand(readCommand());
        }
    }

    /**
     * Read a line from the commandline
     * @return the line of input
     */
    public String readLine()
    {
        String current = "";

        if(currentDir != null) current = currentDir.getCutPath();

        System.out.print("Command:"+current+"$ ");System.out.flush();
        
        if(in.hasNextLine())
        {
            return in.nextLine();
        }

        return "";
    }

    /**
     * Make sure the command isn't empty
     * @return
     */
    public Scanner readCommand()
    {
        String command="";

        while(command.equals(""))
        {
            command = readLine();
        }

        return new Scanner(command);
    }

    /**
     * Compare the String command and the amount of parameters with the installed commands
     * @param scan
     */
    public void runCommand(Scanner scan)
    {
        ArrayList<String> pars = new ArrayList<String>();
        String commandName = "";

        if(scan.hasNext())
        {
            commandName = scan.next();
            while(scan.hasNext())
            {
                pars.add(scan.next());
            }
        }

        for(Command command : commandList)
        {
            if(command.getName().equals(commandName))
            {
                command.run(pars);
                return;
            }
        }

        System.out.println("Error: command not recognized");
    }

    /**
     * Print the menu you see at the startup of the app
     */
    public void printMenu()
    {
        System.out.println("Prometheus dev 0.01");

        for(Command command : commandList)
        {
            System.out.println(String.valueOf(command.getName()+" "+command.toString()));
        }
    }

    /**
     * Make one string out of an ArrayList of strings
     * @param stringList
     * @return
     */
    public String makeString(ArrayList<String> stringList, String connector)
    {
        String endString = "";

        for(String substring : stringList)
        {
            if(!substring.equals(""))
            {
                endString += substring+connector;
            }
        }

        return endString.substring(0,endString.length()-1); //Else there will be a connector at the end with no connection
    }

    /**
     * Every directory path should end with a /
     * @param directory
     * @return
     */
    public String validizeDirectory(String directory)
    {
        if(directory.charAt(directory.length()-1) != '/')
        {
            directory += "/";
        }

        return directory;
    }

    /**
     * Searches on campussearch for a file and prints the results with a number next to it.
     * This is done via XML.
     * Flags:
     * -page int The page that you want :  it's (page+1)*29 so page 0 == 0 - 29
     * -minsize String minsize of the files : 1G or 1MB
     * -maxsize String maxsize fo the files : 2G or 2MB
     * -dirsonly bool will show only dirs
     */
    private class Search extends Command
    {
        public Search(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            HashMap<String,String> param = fillParameters(args);
            onlineResults = new ArrayList<SearchResult>();

            try
            {
                String query = "http://search.student.utwente.nl/api/search?q="+param.get("base".replace(" ", "+"));
                if(!param.get("page").equals("")) query += "&page="+param.get("page");
                if(!param.get("minsize").equals("")) query += "&minsize="+param.get("minsize");
                if(!param.get("maxsize").equals("")) query += "&maxsize="+param.get("maxsize");
                if(param.get("dirsonly").equals("true")) query += "&dirsonly=true";
                
                System.out.println(query);
                URL url = new URL(query);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                ParserCampusSearch xml = new ParserCampusSearch(conn.getInputStream()); //create the xml parser
                ArrayList<SearchResult> results = xml.getSearchResults();

                int i = 0;
                for(SearchResult result : results)
                {
                    if(result.getOnline())
                    {
                        System.out.println(i++ + " Path: "+result.getFullPath()+" Size: "+result.getSizeReadable());
                        onlineResults.add(result);
                    }
                }
            }
            catch(Exception e)
            {
                //System.out.println("Error: no results for this search query");
                System.out.println("Error: "+e.getMessage());
            }
        }
    }

    /**
     * One of the most important features, the downloading. It aims to be very flexible, there
     * are currently 4 ways of downloading:
     * - With a number from the search results
     * - With a number from the file listing
     * - With the literal filename from the current directory
     * - With a full url
     *
     * It's a bit messy i'm probably going to rewrite it in a later version
     *
     */
    private class Get extends Command
    {
        public Get(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            HashMap<String,String> param = fillParameters(args);

            if(param.get("base").equals(""))
            {
                System.out.println("Error: you have to give some argument to get");
                return;
            }

            SmbFile downloadFile = null;

            //Download a file from the search results with a number indicator
            if(onlineResults != null && param.get("s").equals("true") )
            {
                try
                {
                    SearchResult result = onlineResults.get(Integer.valueOf(param.get("base")));

                    downloadFile = new SmbFile(result.getFullPath());
                }
                catch(NullPointerException e)
                {
                    System.out.println("Error: first char needs to be the number of a download");
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                    System.out.println("Error: could not connect to the host");
                }
            }
            else if(onlineResults == null && param.get("s").equals("true")) //You can't download search results if there are none
            {
                System.out.println("Error: you have to search something before you can download it");
            }
            else if(fileList != null) //Download from a directory with a number indicator
            {
                try
                {
                    String result = fileList.get(Integer.valueOf(param.get("base")));
                    downloadFile = new SmbFile(result);
                }
                catch(NullPointerException e)
                {
                    //take the full call because there are a lot of morons who use white spaces in filenames
                    if(param.get("base").substring(0,6).equals("smb://")) //See if it's a full url because
                    {
                        try
                        {
                            downloadFile = new SmbFile(param.get("base"));
                        }
                        catch(Exception ex2)
                        {
                            System.out.println("Error: "+ex2.getMessage());
                        }
                    }
                    else //If it's not a full url it's probably the name of a file in the current dir
                    {
                        try
                        {
                            downloadFile = new SmbFile(currentDir+args.get(0));
                        }
                        catch(Exception ex3)
                        {
                            System.out.println("Error: "+ex3.getMessage());
                        }
                    }
                }
                catch(MalformedURLException ex1)
                {
                    System.out.println("Error: "+ex1.getMessage());
                }
            }

            try
            {
                if(downloadFile != null && downloadFile.isFile())
                {

                   System.out.println("Getting file: "+downloadFile.getName());
                   new SFile(downloadFile).get();
                   System.out.println("Download is done");

                }
                else if(downloadFile != null && downloadFile.isDirectory() && param.get("r").equals("true"))
                {
                    new SDirectory(downloadFile).get(true);
                }
                else if(downloadFile != null && downloadFile.isDirectory())
                {
                    new SDirectory(downloadFile).get(false);
                }
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e.getMessage());
            }
        }
    }

    /**
     * Print the menu, will be changed in a later version as a 'per command help feature'
     */
    private class Help extends Command
    {
        public Help(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            printMenu();
        }
    }

    /**
     * Exit the application
     */
    private class Exit extends Command
    {
        public Exit(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            System.exit(0);
        }
    }

    /**
     * Change the directory either by using a literal directory name or a number from
     * the filelist.
     */
    private class ChangeDirectory extends Command
    {
        public ChangeDirectory(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            HashMap<String,String> param = fillParameters(args);

            try
            {
                if(currentDir != null)
                {
                    if(param.get("base").equals(".."))
                    {
                        currentDir = new SDirectory(currentDir.getSMBFile().getParent());
                        fileList = currentDir.listFiles(false, false); //update the filelist or shit will fuck up
                    }
                    else
                    {
                        String directory;

                        try
                        {
                            directory = fileList.get(Integer.valueOf(param.get("base")));
                        }
                        catch(NumberFormatException e)
                        {
                            args.set(0,validizeDirectory(param.get("base")));
                            directory = currentDir.getSMBFile().getPath()+param.get("base");
                        }

                        try
                        {
                            currentDir = new SDirectory(directory);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Error: "+e.getMessage());
                        }

                        fileList = currentDir.listFiles(false, false); //update the filelist or shit will fuck up
                    }
                        
                }
                else
                {
                    System.out.println("Error: you have to open a resource before you can change directory");
                }
            }
            catch(Exception e)
            {
                System.out.println("Error: "+e.getMessage());
            }
        }
    }
    
    /**
     * Open a SMB repository
     * -s for opening a directory in a search result
     */
    private class Open extends Command
    {
        public Open(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            HashMap<String,String> param = fillParameters(args);

            String openDir = "";
            if(param.get("s").equals("true") && onlineResults != null)
            {
                try
                {
                    openDir = validizeDirectory(onlineResults.get(Integer.valueOf(param.get("base"))).getFullPath());
                }
                catch(NumberFormatException e)
                {
                    System.out.println("Error: "+e.getMessage());
                }
            }
            else
            {
                openDir = validizeDirectory(param.get("base"));
            }

            if(!openDir.equals(""))
            {
                try
                {
                    currentDir = new SDirectory(openDir);
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e.getMessage());
                }
            }
        }
    }
    
    /**
     * Lists the files in a directory
     * @todo flags to make it more like ls in linux
     */
    private class List extends Command
    {
        public List(String token, String parameters, String description)
        {
            super(token,parameters,description);
        }

        public void run(ArrayList<String> args)
        {
            if(currentDir != null)
            {
                try
                {
                    fileList = currentDir.listFiles(false, false); //for the download record

                    ArrayList<String> fileListUse = currentDir.listFiles(false, true); //for the usability

                    int i = 0;
                    for(String file : fileListUse)
                    {
                        System.out.println(i++ +" "+file);
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Error: "+e.getMessage());
                }
            }
        }
    }
}
