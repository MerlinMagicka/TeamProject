
public class OfflineException extends Exception {
	private CurrencyPair[] offlinePairs;
	public OfflineException(CurrencyPair[] offlinePairs) {
		super();
		this.offlinePairs = offlinePairs;
	}
	public String offlinePairsToString() {
		String result = "";
		for(int i = 0; i < offlinePairs.length; i++) {
			result+=offlinePairs[i].getBaseCurrency() + "/" + offlinePairs[i].getQuoteCurrency();
			if(i!=offlinePairs.length-1)
				result+=", ";
		}
		return result;
	}
}
