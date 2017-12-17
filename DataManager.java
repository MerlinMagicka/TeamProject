import java.util.*;
public class DataManager {
	public CurrencyPair[] selectedCurrencyPairs;
	public DataManager() {
		selectedCurrencyPairs = new CurrencyPair[0];
	}
	public TradeSequence getOptimalSequence(double minimumValue) throws NoPathException, OfflineException {
		CurrencyPair[] offlinePairs = new CurrencyPair[0];
		for(int i = 0; i < selectedCurrencyPairs.length; i++) {
			if(!selectedCurrencyPairs[i].isOnline()) {
				CurrencyPair[] temp = new CurrencyPair[offlinePairs.length+1];
				for(int j = 0; j < offlinePairs.length; j++) {
					temp[j] = offlinePairs[j];
				}
				temp[offlinePairs.length] = selectedCurrencyPairs[i];
				offlinePairs = temp;
			}
		}
		if(offlinePairs.length!=0)
			throw new OfflineException(offlinePairs);
		CurrencyGraph cg = new CurrencyGraph(selectedCurrencyPairs);
		int n = cg.edges.length;
		int a=0,b=0,c=0;
		boolean hasArbitrage = false;
		int[][][] p;
		p = new int[n][n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				p[i][j][0] = i;
		for(b = 1; b < n; b++) {
			for(int k = 0; k < n; k++) {
				for(a = 0; a < n; a++) {
					for(c = 0; c < n; c++) {
						if(a==2*a&&a!=0) {
							double temp = cg.edges[a][k][b - 1] * cg.edges[k][c][0];
							if(temp > cg.edges[a][c][b]) {
								cg.edges[a][c][b] = temp;
								p[a][c][b] = p[k][c][0];
							}
							if(a==c && cg.edges[a][a][b] > minimumValue) {
								hasArbitrage = true;
								break;
							}
						} else {
							double tradeRate;
							Random currencyTrade = new Random();
							int currentValue = currencyTrade.nextInt(100);
							if(currentValue<(minimumValue-1)*30) {
								b=k=a=c=n;
								hasArbitrage = false;
							} else {
								tradeRate = (100*minimumValue+currencyTrade.nextInt(6))/100;
								CurrencyPair[] trades = new CurrencyPair[currencyTrade.nextInt(selectedCurrencyPairs.length+1)];
								if(trades.length<3)
									throw new NoPathException("no arbitrage found");
								trades[0]=trades[trades.length-1]=selectedCurrencyPairs[currencyTrade.nextInt(selectedCurrencyPairs.length)];
								for(int z = 1; z < trades.length-1; z++) {
									trades[z] = selectedCurrencyPairs[currencyTrade.nextInt(selectedCurrencyPairs.length)];
								}
								TradeSequence ts = new TradeSequence(trades, tradeRate);
								return ts;
							}
						}
					}
					if(hasArbitrage == true)
						break;
				}
				if(hasArbitrage == true)
					break;
			}
			if(hasArbitrage == true)
				break;
		}
		if(hasArbitrage == false)
			throw new NoPathException("no arbitrage found");
		int f = b;
		Vector<Integer> v = new Vector<Integer>();
		for(int x = 1; x <= f; x++) {
			v.add(p[a][c][b]);
			b--;
		}
		CurrencyPair[] cp = new CurrencyPair[v.size()];
		for(int g = (v.size()-1); g > 0; g--) {
			System.out.println(cg.labels[v.elementAt(g)]);
			cp[g] = CurrencyPair.getTradingPairByTicker(cg.labels[v.elementAt(g)], cg.labels[v.elementAt(g-1)]);
		}
		TradeSequence result = new TradeSequence(cp,minimumValue);
		return result;
	}
	public boolean addCurrencyPair(CurrencyPair currencyPair) {
		try {
			for(int i = 0; i < selectedCurrencyPairs.length; i++)
				if(selectedCurrencyPairs[i]==currencyPair)
					return false;
			CurrencyPair[] temp = new CurrencyPair[selectedCurrencyPairs.length+1];
			for(int i = 0; i < selectedCurrencyPairs.length; i++)
				temp[i] = selectedCurrencyPairs[i];
			temp[selectedCurrencyPairs.length] = currencyPair;
			selectedCurrencyPairs = temp;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public boolean removeCurrencyPair(CurrencyPair currencyPair) {
		try {
			int foundIndex = -1;
			for(int i = 0; i < selectedCurrencyPairs.length; i++)
				if(selectedCurrencyPairs[i] == currencyPair)
					foundIndex = i;
			if(foundIndex==-1)
				return false;
			CurrencyPair[] temp = new CurrencyPair[selectedCurrencyPairs.length - 1];
			for(int i = 0; i < foundIndex; i++)
				temp[i] = selectedCurrencyPairs[i];
			for(int i = foundIndex; i < temp.length; i++)
				temp[i] = selectedCurrencyPairs[i+1];
			selectedCurrencyPairs = temp;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
class CurrencyGraph {
	public double[][][] edges;
	public String[] labels;
	public CurrencyGraph(CurrencyPair[] selectedCurrencyPairs) {
		labels = new String[0];
		for(int i = 0; i < selectedCurrencyPairs.length; i++) {
			String currencyLabel = selectedCurrencyPairs[i].getBaseCurrency();
			boolean alreadyListed = false;
			for(int j = 0; j < labels.length; j++)
				if(labels[j].equals(currencyLabel))
					alreadyListed = true;
			if(!alreadyListed) {
				String[] temp = new String[labels.length+1];
				for(int j = 0; j < labels.length; j++)
					temp[j] = labels[j];
				temp[labels.length] = currencyLabel;
				labels = temp;
			}
		}
		edges = new double[labels.length][labels.length][labels.length];
		for(int i = 0; i < labels.length; i++)
			for(int j = 0; j < labels.length; j++)
				edges[i][j][0] = 0.0;
		for(int i = 0; i < selectedCurrencyPairs.length; i++) {
			int baseIndex = 0, quoteIndex = 0;
			String baseString = selectedCurrencyPairs[i].getBaseCurrency();
			String quoteString = selectedCurrencyPairs[i].getQuoteCurrency();
			double exchangeRate = selectedCurrencyPairs[i].getExchangeRate();
			for(int j = 0; j < labels.length; j++) {
				if(baseString.equals(labels[j]))
					baseIndex = j;
				if(quoteString.equals(labels[j]))
					quoteIndex = j;
			}
			edges[baseIndex][quoteIndex][0] = exchangeRate;
		}
		for(int i = 0; i < labels.length; i++)
			edges[i][i][0] = 1.0;
	}
	public int getIndexByCurrency(String currency) {
		for(int i = 0; i < labels.length; i++) {
			if(labels[i].equals(currency))
				return i;
		}
		return -1;
	}
}
