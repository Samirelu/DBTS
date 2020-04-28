import java.util.ArrayList;

public class MarketWatcher implements PriceListener
{
	
	private ArrayList<Order> portfolio = new ArrayList<Order>();
	private ArrayList<Order> buyTriggers = new ArrayList <Order>();
	private ArrayList<Order> sellTriggers = new ArrayList <Order>();
	private double profit = 0;
	private Market market = Market.getMarket();
	
	public MarketWatcher()
	{
		Market.getMarket().addPriceListener(this);
		awaitInput();
	}
	
	@Override
	public void priceUpdate(String security, double price)
	{
		Listing listing = new Listing(security, price);
		for(Order o : buyTriggers)
		{
			if(o.getListing()==listing)
			{
				Broker b = new Broker();
				buyTriggers.remove(o);
				addToPortfolio(o);
				b.buy(o.getListing().getName(), o.getListing().getPrice(), o.getVolume());
			}	
		}
		for(Order o : sellTriggers)
		{
			if(o.getListing()==listing)
			{
				Broker b = new Broker();
				sellTriggers.remove(o);
				b.sell(listing.getName(), listing.getPrice(), removeFromPortfolio(o));
			}	
		}
	}

	public String addBuyTrigger(Order order)
	{
		if(order == null)
			return ("Order was null");
		if(order.getListing()==null)
			return ("Order's listing was null");
		if(order.getListing().getName()==null)
			return ("The name of the ordered security was null");
		if(!market.IsSecurityListed(order.getListing().getName()))
			return ("The ordered security is not listed in the market");
		for(Order o : buyTriggers)
		{
			if(o.getListing().getName()==order.getListing().getName() && o.getListing().getPrice() == order.getListing().getPrice())
			{
				buyTriggers.remove(o);
				Order finalOrder = new Order(o.getListing(), o.getVolume()+order.getVolume());
				buyTriggers.add(finalOrder);
				return("Trigger already exists, for a volume of " + order.getVolume() + ". Pre-existing trigger's volume was increased by the desired volume of the new trigger, to a final volume of " + finalOrder.getVolume() + ".");
			}
		}
		buyTriggers.add(order);
		return ("Trigger set to purchase " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + ", at a volume of " + order.getVolume() + ".");
	}
	
	public String addSellTrigger(Order order)
	{
		if(order == null)
			return ("Order was null");
		if(order.getListing()==null)
			return ("Order's listing was null");
		if(order.getListing().getName()==null)
			return ("The name of the ordered security was null");
		if(!market.IsSecurityListed(order.getListing().getName()))
			return ("The ordered security is not listed in the market");
		for(Order o : sellTriggers)
		{
			if(o.getListing().getName()==order.getListing().getName() && o.getListing().getPrice() == order.getListing().getPrice())
			{
				sellTriggers.remove(o);
				Order finalOrder = new Order(o.getListing(), o.getVolume()+order.getVolume());
				sellTriggers.add(finalOrder);
				return("Trigger already exists, for a volume of " + order.getVolume() + ". Pre-existing trigger's volume was increased by the desired volume of the new trigger, to a final volume of " + finalOrder.getVolume() + ".");
			}
		}
		buyTriggers.add(order);
		return ("Trigger set to sell " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + ", at a volume of " + order.getVolume() + ".");
	}
	
	public String removeBuyTrigger(Order order)
	{
		if(order == null)
			return ("Order was null");
		if(order.getListing()==null)
			return ("Order's listing was null");
		if(order.getListing().getName()==null)
			return ("The name of the ordered security was null");
		if(!market.IsSecurityListed(order.getListing().getName()))
			return ("The ordered security is not listed in the market");
		for(Order o : buyTriggers)
		{
			if(o.getListing().getName()==order.getListing().getName() && o.getListing().getPrice() == order.getListing().getPrice())
			{
				if(o.getVolume()>order.getVolume())
				{
				buyTriggers.remove(o);
				Order finalOrder = new Order(o.getListing(), o.getVolume()-order.getVolume());
				buyTriggers.add(finalOrder);
				return("The trigger you wished to remove was set to purchase " + order.getVolume() + " amounts. Pre-existing trigger's volume was decreased by the desired volume of the new trigger, to a final volume of " + finalOrder.getVolume() + ".");
				}
				buyTriggers.remove(o);
				if(o.getVolume()<order.getVolume())
					return("You wished to reduce your order for security " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + " by more than what was triggered to be purchased. As a precaution the entire trigger was cancelled.");
				return("Your order for security " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + " for a volume of " + order.getVolume() + "was cancelled.");
			}
		}
		return ("Could not find a trigger set to purchase " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + ", at a volume of " + order.getVolume() + ".");
	}
	
	public String removeSellTrigger(Order order)
	{
		if(order == null)
			return ("Order was null");
		if(order.getListing()==null)
			return ("Order's listing was null");
		if(order.getListing().getName()==null)
			return ("The name of the ordered security was null");
		if(!market.IsSecurityListed(order.getListing().getName()))
			return ("The ordered security is not listed in the market");
		for(Order o : sellTriggers)
		{
			if(o.getListing().getName()==order.getListing().getName() && o.getListing().getPrice() == order.getListing().getPrice())
			{
				if(o.getVolume()>order.getVolume())
				{
				buyTriggers.remove(o);
				Order finalOrder = new Order(o.getListing(), o.getVolume()-order.getVolume());
				buyTriggers.add(finalOrder);
				return("The trigger you wished to remove was set to sell " + order.getVolume() + " amounts. Pre-existing trigger's volume was decreased by the desired volume of the new trigger, to a final volume of " + finalOrder.getVolume() + ".");
				}
				buyTriggers.remove(o);
				if(o.getVolume()<order.getVolume())
					return("You wished to reduce your order for selling security " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + " by more than what was triggered to be sold. As a precaution the entire trigger was cancelled.");
				return("Your order for selling security " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + " for a volume of " + order.getVolume() + "was cancelled.");
			}
		}
		return ("Could not find a trigger set to sell " + order.getListing().getName() + " at a price of " + order.getListing().getPrice() + ", at a volume of " + order.getVolume() + ".");
	}

	public double getProfit()
	{
		return profit;
	}
	
	public double getWorth()
	{
		double worth = 0;
		for(Order o : portfolio)
		{
			worth+=(o.getVolume()*market.getLastPrice(o.getListing().getName()));
		}
		return worth;
	}
	
	public double getGrowth()
	{
		double growth = 0;
		for(Order o : portfolio)
		{
			growth+=(o.getVolume()*(market.getLastPrice(o.getListing().getName())-o.getListing().getPrice()));
		}
		return growth;
	}
	
	public double getGrowth(String security)
	{
		double growth = 0;
		for(Order o : portfolio)
		{
			if(o.getListing().getName()==security)
				growth += (o.getVolume()*((market.getLastPrice(security)-o.getListing().getPrice())));
		}
		return growth;
	}
	
	private void addToPortfolio(Order order)
	{
		for(Order o : portfolio)
		{
			if(o.getListing()==order.getListing())
			{
				portfolio.remove(o);
				portfolio.add(new Order(order.getListing(), order.getVolume()+o.getVolume()));
				return;
			}
		}
		portfolio.add(order);
	}
	
	private int removeFromPortfolio(Order order)
	{
		String security = order.getListing().getName();
		int volume = order.getVolume();
		int finalVolume = 0;
		for(Order o : portfolio)
		{
			if(o.getListing().getName()==security)
			{
				if(volume<=o.getVolume())
				{
					finalVolume+=volume;
					profit += (volume*(order.getListing().getPrice()-o.getListing().getPrice()));
					portfolio.remove(o);
					if(volume!=o.getVolume())
					{
						portfolio.add(new Order(o.getListing(), o.getVolume()-volume));
					}
					return finalVolume;
				}
				portfolio.remove(o);
				profit += (o.getVolume()*(order.getListing().getPrice()-o.getListing().getPrice()));
				finalVolume += o.getVolume();
				volume -= o.getVolume();
			}
		}
		return finalVolume;
	}
	
	private void awaitInput()
	{
		
	}
}
