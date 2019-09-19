import java.util.HashSet;
import java.util.Set;

public class CrawlStat {
	
	public int totalURLs, totalOks, totalN_Oks;
	public int totalUniqueOks, totalUniqueN_Oks;
	
	public int outgoingLinks;
	public Set<String> insideURLs;
	public Set<String> outsideURLs;
	
	public int size1, size2, size3, size4, size5; //based on file sizes
	
    public CrawlStat() {
    	
    	outgoingLinks=0;
    	totalURLs=0; totalOks=0; totalN_Oks=0;
    	totalUniqueOks=0; totalUniqueN_Oks=0;
    	
    	insideURLs= new HashSet<String>();
    	outsideURLs= new HashSet<String>();
    	
    	size1=0; size2=0; size3=0; size4=0; size5=0;
    }
}
