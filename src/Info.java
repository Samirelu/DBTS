import java.time.LocalDateTime;

public class Info 
	{
		private LocalDateTime dt;
		private Listing listing;
		
		public Info(LocalDateTime dt, Listing listing)
		{
			this.dt = dt;
			this.listing = listing;
		}
		
		public LocalDateTime getDt()
		{
			return dt;
		}
		
		public double getPrice()
		{
			return listing.getPrice();
		}
		
		public String getCompanyName()
		{
			return listing.getName();
		}
	}