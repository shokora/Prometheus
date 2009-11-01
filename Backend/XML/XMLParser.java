package Backend.XML;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 *
 * @author shokora
 *     This file is part of Prometheus.

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
public abstract class XMLParser
{
    public Document dom;


    public XMLParser(InputStream in)
    {
        parseXML(in);
    }


    /**
     * Create the factory and then create the document by parsing the XML information.
     * @param lines String that holds the xml document
     */
    public void parseXML(InputStream in)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();

            dom = db.parse(in);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch(SAXException e)
        {
            e.printStackTrace();
        }
    }

    public abstract void parseDocument() throws Exception;
    
    /**
    * I take a xml element and the tag name, look for the tag and get
    * the text content
    * i.e for <employee><name>John</name></employee> xml snippet if
    * the Element points to employee node and tagName is name I will return John
    * @param ele
    * @param tagName
    * @return
    */
    public String getTextValue(Element ele, String tagName)
    {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }

        return textVal;
    }


	/**
	* Calls getTextValue and returns a int value
	* @param ele
	* @param tagName
	* @return
	*/
	public int getIntValue(Element ele, String tagName) throws Exception
    {
       return Integer.parseInt(getTextValue(ele,tagName));
	}
    
    public long getLongValue(Element ele, String tagName) throws Exception
    {
        return Long.parseLong(getTextValue(ele,tagName));
    }


}
