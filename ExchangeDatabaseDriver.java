import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExchangeDatabaseDriver {
	public static void fetchData() {
		String url;
		url = "http://webrates.truefx.com/rates/connect.html?f=html";
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder dBuilder = builderFactory.newDocumentBuilder();
			Document document = dBuilder.parse(url);
			document.normalize();

			NodeList rootNodes = document.getElementsByTagName("table");
			Node rootNode = rootNodes.item(0);
			Element rootElement = (Element) rootNode;
			NodeList currencyList = rootElement.getElementsByTagName("tr");
			for(int i = 0; i < currencyList.getLength(); i++) {
				String value;
				long timestamp;
				double bidPrice = 0.0;
				double offerPrice = 0.0;
				Node currencyNode = currencyList.item(i);
				Element currencyElement = (Element) currencyNode;
				NodeList valuesList = currencyElement.getElementsByTagName("td");
				Element exchangeRateTag = (Element) valuesList.item(2);
				value = exchangeRateTag.getTextContent();
				exchangeRateTag = (Element) valuesList.item(3);
				value += exchangeRateTag.getTextContent();
				bidPrice = Double.parseDouble(value);
				value = "";
				exchangeRateTag = (Element) valuesList.item(4);
				value += exchangeRateTag.getTextContent();
				exchangeRateTag = (Element) valuesList.item(5);
				value += exchangeRateTag.getTextContent();
				exchangeRateTag = (Element) valuesList.item(1);
				timestamp = Long.parseLong(exchangeRateTag.getTextContent());
				offerPrice = Double.parseDouble(value);
				CurrencyPair.tradingPairs[2*i].setExchangeRate(bidPrice, timestamp);
				CurrencyPair.tradingPairs[2*i+1].setExchangeRate(1/offerPrice, timestamp);
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
