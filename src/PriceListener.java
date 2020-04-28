
//connects to a PriceSource Instance
//Monitors movements on the PriceSource Instance
//When conditions are met triggers ExecutionService to buy securities at specified price

public interface PriceListener {
	void priceUpdate(String security, double price);
}
