import java.util.ArrayList;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */

public class TradingStrategy 
{

	class ListenerThread extends Thread
	{
		
		public ListenerThread() 
		{}
		
		public void start()
		{
			Thread t = new Thread();
			try {
				t.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			MarketWatcher watcher = new MarketWatcher();
		}
		
	}
	
	private static void setUpTestMarket() throws InterruptedException
	{
		ArrayList<Listing> ipoList = new ArrayList<Listing>();
		ipoList.add(new Listing("NFLX", 361.19));
		ipoList.add(new Listing("FB", 153.62));
		ipoList.add(new Listing("IBM", 106.08));
		ipoList.add(new Listing("INTC", 54.10));
		ipoList.add(new Listing("MSFT", 153.66));
		ipoList.add(new Listing("AAPL", 240.69));
		ipoList.add(new Listing("CSCO", 38.88));
		ipoList.add(new Listing("ATVI", 59.90));
		ipoList.add(new Listing("AMD", 42.52));
		ipoList.add(new Listing("GOOGL", 1089.00));
		ipoList.add(new Listing("KO", 43.68));
		ipoList.add(new Listing("EBAY", 29.36));
		ipoList.add(new Listing("EA", 102.56));
		ipoList.add(new Listing("F", 4.22));
		ipoList.add(new Listing("GS", 146.61));
		ipoList.add(new Listing("PYPL", 92.12));
		ipoList.add(new Listing("WORK", 24.23));
		ipoList.add(new Listing("SPOT", 121.86));
		ipoList.add(new Listing("TSLA", 480.05));
		ipoList.add(new Listing("UBER", 22.79));
		Market market = Market.getMarket(ipoList);
	}
	
	public static void main(String args[]) throws InterruptedException 
	{
		setUpTestMarket();
	}
	
}