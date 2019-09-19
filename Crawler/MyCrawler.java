import java.util.Set;
import java.util.regex.Pattern;
import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class MyCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js"
            + "|wav|avi|mov|mpeg|mpg|ram|m4v|wma|wmv|mid|txt|rss" + "|mp2|mp3|mp4|zip|rar|gz|exe))$");
	
	/*
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|exe))$");
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(jpg|doc|html|png))$");
	*/
	
	CrawlStat myCrawlStat;
	int inside=0, outside=0;
	
	public MyCrawler() {
		myCrawlStat= new CrawlStat();
	}
	/**
	* This method receives two parameters. The first parameter is the page
	* in which we have discovered this new url and the second parameter is
	* the new url. You should implement this function to specify whether
	* the given url should be crawled or not (based on your crawling logic).
	* 
	* In this example, we are instructing the crawler to ignore urls that
	* have css, js, git, ... extensions and to only accept urls that start
	* with "https://www.reuters.com/"
	*/
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		
		String href = url.getURL().toLowerCase();
		myCrawlStat.totalURLs++;
		
		boolean ans= href.startsWith("https://www.reuters.com/") ||
        		href.startsWith("http://www.reuters.com/")||
        		href.startsWith("https://reuters.com/")||
        		href.startsWith("https://reuters.com/");
		
		if(ans) {
			//inside 
			if(!myCrawlStat.insideURLs.contains(href)) {
				myCrawlStat.insideURLs.add(href);
			}			
		}
		else {
			//outside
			if(!myCrawlStat.outsideURLs.contains(href)) {
				myCrawlStat.outsideURLs.add(href);
			}
		}
		
		ans = ans && !FILTERS.matcher(href).matches();
		return ans;
	}
	
	/**
	* This function is called when a page is fetched and ready
	* to be processed by your program.
	*/
	@Override
	public void visit(Page page) {
		
		/* all visited pages are downloaded
		   so successfully downloaded pages are pages that come in this function
			we need to print URL of the donwloaded page, size of downloaded file, 
			# outlinks found, content type 
		*/
		
		String url = page.getWebURL().getURL();
		String contentType = page.getContentType().split(";")[0];
		//System.out.println(contentType);
		//System.out.println("URL: " + url+ "  Content Type: "+ contentType);
		
		if(contentType.startsWith("text/html") || contentType.startsWith("image/gif") || 
			contentType.startsWith("image/jpeg") ||	contentType.startsWith("image/png") || 
			contentType.startsWith("application/pdf")) {
		
			int filesize= page.getContentData().length;
			
			if(filesize<1024) {
				myCrawlStat.size1++;
			}
			else if(filesize>=1024 && filesize< 10240) {
				myCrawlStat.size2++;
			}
			else if( filesize>=10240 && filesize< 102400) {
				myCrawlStat.size3++;
			}
			else if( filesize>=102400 && filesize< 1048576) {
				myCrawlStat.size4++;
			}
			else {
				myCrawlStat.size5++;
			}
						
			if (page.getParseData() instanceof HtmlParseData) {
				
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				String text = htmlParseData.getText();
				String html = htmlParseData.getHtml();
				Set<WebURL> links = htmlParseData.getOutgoingUrls();
				
				myCrawlStat.outgoingLinks +=  links.size();	
				System.out.println(url+";"+ filesize +";" + links.size()+ ";"+ contentType);
				/*
				System.out.println("Content Data Size: "+page.getContentData().length+" bytes");
				System.out.println(url+ " "+ contentType+ " " + filesize+ " "+ links.size());
				System.out.println("Text length: " + text.length());
				System.out.println("Html length: " + html.length());
				System.out.println("Number of outgoing links: " + links.size());
				*/
			}
			else {
				System.out.println(url+ ";" + filesize + ";0;" + contentType );
			}
		}
		else {
			//System.out.println("Not parsed URL: " + url+ "  Content Type: "+ contentType);
		}
	}
	
	@Override
	public Object getMyLocalData() {
		return myCrawlStat;
	}
	
	@Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {

		//To analyze results 
		//System.out.println("URL: "+ webUrl.getURL() + "   HTTP Status Code: "+ statusCode);
		// Total count of these URLS will be the URLs attempted to fetch
		System.out.println("CODE;"+webUrl.getURL() + ";"+ statusCode);
    }
}


