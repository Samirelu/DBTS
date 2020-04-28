
public class Broker implements ExecutionService {

	@Override
	public void buy(String security, double price, int volume)
	{
		Order order = new Order(new Listing(security, price), volume);
		notifyMarket(order, true);
	}
	
	@Override
    public void sell(String security, double price, int volume)
    {
		Order order = new Order(new Listing(security, price), volume);
		notifyMarket(order, false);
    }
	
	private void notifyMarket(Order o, Boolean buying)
	{
		Market market = Market.getMarket();
		if(market == null)
			return;
		
		market.placeOrder(o, buying);
	}
	
}
