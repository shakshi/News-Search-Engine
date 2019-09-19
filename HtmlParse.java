import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class HtmlParse {
	
   public static void main(final String[] args) throws IOException,SAXException, TikaException {

	   //Parse the fetched html pages to get a list of words 
	   //this will serve as the big.txt file for Spell Corrector program 
	   
	   String dir_path= "fetchedPages";
	   File dir = new File(dir_path);
       File[] files = dir.listFiles();
       
       ArrayList<String> wordList = new ArrayList<>();
       
       for(File x: files)
       {   		
            BodyContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();
            FileInputStream inputstream = new FileInputStream(x);
            ParseContext pcontext = new ParseContext();
            
            HtmlParser htmlparser = new HtmlParser();
            htmlparser.parse(inputstream, handler, metadata,pcontext);
            
            String str= handler.toString();
            ArrayList<String> list = new ArrayList<>(Arrays.asList(str.split("\\W+")));
            wordList.addAll(list);
	  }
      
       
       BufferedWriter writer = new BufferedWriter(new FileWriter("big.txt"));
       System.out.println("Started writing ");
 	   for(String s: wordList)
 		{
 		   System.out.println(s);
 		    writer.write(s+"\n");
 		}
 	  System.out.println("completed writing ");
 	   writer.close();
      }
}
