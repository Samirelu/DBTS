import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Market implements PriceSource 
{

	Random rand = new Random();
	private ArrayList<PriceListener> subscribers = new ArrayList<PriceListener>();
	private LinkedList<Info> marketHistory = new LinkedList<Info>();
	private ArrayList<String> listedSecurities = new ArrayList<String>();
	private LinkedList<ScheduledUpdate> schedule = new LinkedList<ScheduledUpdate>();
	private static Market instance;
	
	private Market(ArrayList<Listing> ipoList) throws InterruptedException {
		for (Listing l : ipoList) {
			updateMarketHistory(l);
			scheduleUpdate(l.getName());
			listedSecurities.add(l.getName());
		}
		invisibleHandOfTheMarket();
	}

	private class ScheduledUpdate 
	{
		private int waitTime;
		private Listing listing;

		public ScheduledUpdate(int waitTime, Listing listing) {
			this.waitTime = waitTime;
			this.listing = listing;
		}

		public int getWaitTime() {
			return waitTime;
		}

		public Listing getListing() {
			return listing;
		}

		public void reduceWaitTime(int x) {
			waitTime -= x;
		}
	}

	public Boolean IsSecurityListed(String security)
	{
		if(listedSecurities.contains(security))
			return true;
		return false;
	}
	
	public synchronized static Market getMarket(ArrayList<Listing> ipoList) throws InterruptedException
	{
		if(instance == null)
			instance = new Market(ipoList);
		return instance;
	}
	
	public LinkedList<Info> getMarketHistory()
	{
		return marketHistory;
	}
	
	public static Market getMarket()
	{
		return instance;
	}

	public void placeOrder(Order order, Boolean buying)
	{
		double price = getLastPrice(order.getListing().getName());
		
		if(buying)
			price = price*1.02;
		else
			price = price * 0.98;
		
		Listing newListing = new Listing(order.getListing().getName(), price);
		ScheduledUpdate newUpdate;
		
		for(ScheduledUpdate s : schedule)
		{
			if(s.getListing().getName() == order.getListing().getName())
			{
				newUpdate = new ScheduledUpdate(s.getWaitTime(), newListing);
				scheduleUpdate(newUpdate);
				return;
			}
		}
	}
	
	@Override
	public void addPriceListener(PriceListener listener) 
	{
		if (listener != null && !subscribers.contains(listener))
			subscribers.add(listener);
	}

	@Override
	public void removePriceListener(PriceListener listener) 
	{
		if (subscribers.contains(listener))
			subscribers.remove(listener);
	}

	private void updateMarketHistory(Listing listing) 
	{
		marketHistory.add(new Info(LocalDateTime.now(), listing));
	}

	public double getLastPrice(String name) 
	{
		for (int i = marketHistory.size() - 1; i > -1; i--) {
			Info info = marketHistory.get(i);
			if (info.getCompanyName() == name) {
				return info.getPrice();
			}
		}
		return 1;
	}

	private double getNewPrice(double price) 
	{
		if (rand.nextInt(2) == 0 || price < 1)
			return (price + rand.nextDouble());
		else
			return (price - rand.nextDouble());
	}

	private void scheduleUpdate(String name) 
	{
		ScheduledUpdate update = new ScheduledUpdate(rand.nextInt(30),
				new Listing(name, getNewPrice(getLastPrice(name))));
		scheduleUpdate(update);
	}

	private void scheduleUpdate(ScheduledUpdate update) 
	{
		for (ScheduledUpdate s : schedule) {
			if (update.getWaitTime() > s.getWaitTime()) {
				schedule.add(schedule.indexOf(s), update);
				return;
			}
		}
		schedule.add(0, update);
	}

	private void invisibleHandOfTheMarket() throws InterruptedException 
	{
		while (true) {
			ScheduledUpdate nextUpdate = schedule.getFirst();
			schedule.remove();
			for (ScheduledUpdate s : schedule)
				s.reduceWaitTime(nextUpdate.getWaitTime());
			TimeUnit.SECONDS.sleep(nextUpdate.getWaitTime());
			updateMarketHistory(nextUpdate.getListing());
			for(PriceListener listener : subscribers)
				listener.priceUpdate(nextUpdate.getListing().getName(), nextUpdate.getListing().getPrice());
			scheduleUpdate(nextUpdate.getListing().getName());
		}
	}

}
