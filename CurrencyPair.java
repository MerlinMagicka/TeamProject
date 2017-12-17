public class CurrencyPair {
	private String baseCurrency;
	private String quoteCurrency;
	private long timestamp;
	private double exchangeRate;
	public static final CurrencyPair[] tradingPairs = {
			new CurrencyPair("EUR","USD"),
			new CurrencyPair("USD","EUR"),
			
			new CurrencyPair("USD","JPY"),
			new CurrencyPair("JPY","USD"),
			
			new CurrencyPair("GBP","USD"),
			new CurrencyPair("USD","GBP"),
			
			new CurrencyPair("EUR","GBP"),
			new CurrencyPair("GBP","EUR"),
			
			new CurrencyPair("USD","CHF"),
			new CurrencyPair("CHF","USD"),
			
			new CurrencyPair("EUR","JPY"),
			new CurrencyPair("JPY","EUR"),
			
			new CurrencyPair("EUR","CHF"),
			new CurrencyPair("CHF","EUR"),
			
			new CurrencyPair("USD","CAD"),
			new CurrencyPair("CAD","USD"),
			
			new CurrencyPair("AUD","USD"),
			new CurrencyPair("USD","AUD"),
			
			new CurrencyPair("GBP","JPY"),
			new CurrencyPair("JPY","GBP")
	};
	public CurrencyPair(String baseCurrency, String quoteCurrency) {
		this.baseCurrency = baseCurrency;
		this.quoteCurrency = quoteCurrency;
		this.timestamp = System.currentTimeMillis();
	}
	public static CurrencyPair getTradingPairByTicker(String baseCurrency, String quoteCurrency) {
		for(int i = 0; i < tradingPairs.length; i++) {
			if(tradingPairs[i].baseCurrency.equals(baseCurrency) && tradingPairs[i].quoteCurrency.equals(quoteCurrency))
				return tradingPairs[i];
		}
		return null;
	}
	public CurrencyPair[] getTradablePairs(CurrencyPair[] selectedPairs) {
		int tradablePairCount = 0;
		for(int i = 0; i < selectedPairs.length; i++)
			if(baseCurrency.equals(selectedPairs[i].getQuoteCurrency()))
				tradablePairCount++;
		CurrencyPair[] tradablePairs = new CurrencyPair[tradablePairCount];
		int tradablePairIndex = 0;
		for(int i = 0; i < selectedPairs.length; i++)
			if(baseCurrency.equals(selectedPairs[i].getQuoteCurrency()))
				tradablePairs[tradablePairIndex++] = selectedPairs[i];
		return tradablePairs;
	}
	public double getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(double exchangeRate, long timestamp) {
		this.exchangeRate = exchangeRate;
		this.timestamp = timestamp;
	}
	public boolean isOnline() {
		if(System.currentTimeMillis() - timestamp > 5000)
			return false;
		else
			return true;
	}
	public String getTicker() {
		String output;
		output = baseCurrency + "/" + quoteCurrency + "\t" + exchangeRate;
		return output;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public String getQuoteCurrency() {
		return quoteCurrency;
	}
}