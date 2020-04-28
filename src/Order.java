
public class Order {
	
	private Listing listing;
	private int volume;
	
	public Order(Listing listing, int volume)
	{
		this.listing = listing;
		this.volume = volume;
	}
	
	public Listing getListing()
	{
		return listing;
	}
	
	public int getVolume()
	{
		return volume;
	}

}
