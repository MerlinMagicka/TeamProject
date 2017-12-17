
public class TradeSequence {
	private double expectedYield;
	private CurrencyPair[] currencyPairs;
	public TradeSequence(CurrencyPair[] currencyPairs, double expectedYield) {
		this.currencyPairs = currencyPairs;
		this.expectedYield = expectedYield;
	}
	public double getExpectedYield() {
		return expectedYield;
	}
	public CurrencyPair[] getCurrencyPairs() {
		return currencyPairs;
	}
	public String toString() {
		if(currencyPairs.length==0)
			return "empty trade sequence";
		String result = currencyPairs[0].getQuoteCurrency();
		for(int i = 0; i < currencyPairs.length; i++) {
			if(!(i > 0 && currencyPairs[i].getBaseCurrency().equals(currencyPairs[i-1].getBaseCurrency())))
				result += "-" + currencyPairs[i].getBaseCurrency();
		}
		result += "-" + currencyPairs[0].getQuoteCurrency();
		return result;
	}
}
