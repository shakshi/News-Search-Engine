import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Controller {

	public static void main(String[] args) {
		
		String crawlStorageFolder = "/data/crawl"; 
		int numberOfCrawlers = 7; 
		CrawlConfig config = new CrawlConfig(); 
		config.setCrawlStorageFolder(crawlStorageFolder); 
		
		int maxPagestoFetch= 20000;
		int maxDepthOfCrawling= 16;
		config.setMaxPagesToFetch(maxPagestoFetch);
		config.setMaxDepthOfCrawling(maxDepthOfCrawling);
		config.setIncludeBinaryContentInCrawling(true);
		config.setMaxDownloadSize(104857600);     //1048576 bytes = 1 MB 
		
		// Instantiate the controller for this crawl
		PageFetcher pageFetcher = new PageFetcher(config); 
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher); 
		CrawlController controller;
		try {
			
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			
			/* For each crawl, we have to add some seed urls. These are the first
			URLs that are fetched and then the crawler starts following links * 
			which are found in these pages */ 
			controller.addSeed("https://www.reuters.com/");
			
			/* Start the crawl. This is a blocking operation, meaning that your code 
			will reach the line after this only when crawling is finished. */ 		
			controller.start(MyCrawler.class, numberOfCrawlers);
			List<Object> crawlersLocalData = controller.getCrawlersLocalData();
	        
			int totaloutgoingLinks=0;
	        Set<String> insideURLs= new HashSet<String>();
	        Set<String> outsideURLs= new HashSet<String>();
	        
	        int t1=0, t2=0, t3=0, t4=0, t5=0;
	        
	        for (Object localData : crawlersLocalData) {
	        
	        	CrawlStat stat = (CrawlStat) localData;
	            totaloutgoingLinks+= stat.outgoingLinks;
	            insideURLs.addAll(stat.insideURLs);
	            outsideURLs.addAll(stat.outsideURLs);
	            
	            t1+= stat.size1; t2+= stat.size2; 
	            t3+= stat.size3; t4+= stat.size4; 
	            t5+= stat.size5; 
	        }
	        
	        System.out.println("Total Outgoing Links : " + totaloutgoingLinks);
	        System.out.println("Total Unique Inside URls : " + insideURLs.size());
	        System.out.println("Total Unique Outside URls : " + outsideURLs.size());
	        
	       
	        System.out.println("File Sizes : ");
	        System.out.println("< 1 KB: "+ t1); 
	        System.out.println("1 KB ~ < 10 KB: "+ t2);
	        System.out.println("10 KB ~ < 100 KB: "+ t3);
	        System.out.println("100 KB ~ < 1 MB: "+ t4);
	        System.out.println(">= 1MB: "+ t5);
	        
	   		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
