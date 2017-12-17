import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import javax.swing.*;

public class interfaceApp extends JFrame {
	private int period = 1000;
	private String[] userLanguage= {"English","廣東話","Français","普通话","Português"};
	private String[][] dialogue = {
			{"Trade sequence","Return value","No currencies selected","Select language","Current rates","Select currencies","Minimum return value","No currencies selected","No path found","FOREX market closed","Update period"},
			{"交易順序","返回值","沒有選擇貨幣","選擇語言","目前的價格","選擇貨幣","最小回報值","沒有選擇貨幣","找不到路徑","外匯市場發現","更新期間"},
			{"Séquence commerciale","Valeur de retour","Aucune devise sélectionnée","Choisir la langue","Les taux courants","Sélectionnez les devises","Valeur de retour minimum","Aucune devise sélectionnée","Aucun chemin trouvé","Marché FOREX fermé","Mise à jour"},
			{"交易顺序","返回值","没有选择货币","选择语言","目前的价格","选择货币","最小回报值","没有选择货币","找不到路径","外汇市场关闭","更新期间"},
			{"Seqüência comercial","Valor de retorno","Nenhuma moeda selecionada","Selecione o idioma","Taxas atuais","Selecione moedas","Valor mínimo de retorno","Nenhuma moeda selecionada","Nenhum caminho encontrado","Mercado FOREX fechado","Período de atualização"}
	};
	private int languageChoice = 0;
	private boolean beastModeActive = false;
	private DataManager dm = new DataManager();
	private int minimumValue = 100;
	
	private JPanel periodPanel, periodControlPanel, currencyCheckBoxPanel, minimumValuePanel, sequencePanel, ratesPanel, languagePanel, sequenceLabelPanel;
	private JCheckBox[] currencyCheckBoxes;
	private JLabel minimumValueLabel, currencyTitleLabel, minimumValueTitleLabel, sequenceLabel, sequenceTitleLabel, returnLabel, returnTitleLabel, languageLabel, currentRatesLabel, periodLabel, periodTitle;
	private JLabel[] tickerLabels, exchangeRateLabels;
	private JPanel[] tickerRatePairPanels;
	private JButton increaseMinimum, decreaseMinimum, increasePeriod, decreasePeriod;
	private JComboBox languageOptions;
	
	public interfaceApp() {
		this.setSize(700,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(new GridLayout(1,2));
		this.setTitle("FOREX Arbitrage Finder");
		increasePeriod = new JButton("+");
		increasePeriod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				increasePeriod();
			}
		});
		decreasePeriod = new JButton("-");
		decreasePeriod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decreasePeriod();
			}
		});
		decreasePeriod.setEnabled(false);
		periodLabel = new JLabel(""+period/1000+"s",SwingConstants.CENTER);
		periodTitle = new JLabel(dialogue[languageChoice][10],SwingConstants.CENTER);
		periodPanel = new JPanel();
		periodPanel.setLayout(new GridLayout(1,2));
		periodControlPanel = new JPanel();
		periodControlPanel.setLayout(new GridLayout(1,3));
		periodControlPanel.add(decreasePeriod);
		periodControlPanel.add(periodLabel);
		periodControlPanel.add(increasePeriod);
		periodPanel.add(periodTitle);
		periodPanel.add(periodControlPanel);
		sequenceTitleLabel = new JLabel(dialogue[languageChoice][0],SwingConstants.CENTER);
		returnTitleLabel = new JLabel(dialogue[languageChoice][1],SwingConstants.CENTER);
		sequenceLabelPanel = new JPanel();
		sequenceLabelPanel.setLayout(new GridLayout(1,2));
		sequenceLabelPanel.add(sequenceTitleLabel);
		sequenceLabelPanel.add(returnTitleLabel);
		sequenceLabel = new JLabel(dialogue[languageChoice][2],SwingConstants.CENTER);
		returnLabel = new JLabel("",SwingConstants.CENTER);
		sequencePanel = new JPanel();
		sequencePanel.setLayout(new GridLayout(1,2));
		sequencePanel.add(sequenceLabel);
		sequencePanel.add(returnLabel);
		ratesPanel = new JPanel();
		ratesPanel.setLayout(new GridLayout(CurrencyPair.tradingPairs.length/2 + 5,1));
		languageLabel = new JLabel(dialogue[languageChoice][3],SwingConstants.CENTER);
		languagePanel = new JPanel();
		languagePanel.setLayout(new GridLayout(1,2));
		languagePanel.add(languageLabel);
		languageOptions = new JComboBox(userLanguage);
		languageOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox jcb = (JComboBox)e.getSource();
				String language = (String)jcb.getSelectedItem();
				setLanguageChoice(language);
			}
		});
		languagePanel.add(languageOptions);
		ratesPanel.add(languagePanel);
		currentRatesLabel = new JLabel(dialogue[languageChoice][4],SwingConstants.CENTER);
		ratesPanel.add(currentRatesLabel);
		tickerLabels = new JLabel[CurrencyPair.tradingPairs.length/2];
		exchangeRateLabels = new JLabel[CurrencyPair.tradingPairs.length/2];
		tickerRatePairPanels = new JPanel[CurrencyPair.tradingPairs.length];
		for(int i = 0; i < CurrencyPair.tradingPairs.length/2; i++) {
			tickerLabels[i] = new JLabel("--",SwingConstants.CENTER);
			exchangeRateLabels[i] = new JLabel("--",SwingConstants.CENTER);
			tickerRatePairPanels[i] = new JPanel();
			tickerRatePairPanels[i].setLayout(new GridLayout(1,2));
			tickerRatePairPanels[i].add(tickerLabels[i]);
			tickerRatePairPanels[i].add(exchangeRateLabels[i]);
			ratesPanel.add(tickerRatePairPanels[i]);
		}
		ratesPanel.add(sequenceLabelPanel);
		ratesPanel.add(sequencePanel);
		currencyTitleLabel = new JLabel(dialogue[languageChoice][5], SwingConstants.CENTER);
		minimumValueTitleLabel = new JLabel(dialogue[languageChoice][6], SwingConstants.CENTER);
		currencyCheckBoxPanel = new JPanel();
		currencyCheckBoxPanel.setLayout(new GridLayout(CurrencyPair.tradingPairs.length/2 + 3, 1));
		currencyCheckBoxPanel.add(currencyTitleLabel);
		currencyCheckBoxes = new JCheckBox[CurrencyPair.tradingPairs.length/2];
		increaseMinimum = new JButton("+");
		increaseMinimum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				increaseMinimum();
			}
		});
		decreaseMinimum = new JButton("-");
		decreaseMinimum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				decreaseMinimum();
			}
		});
		decreaseMinimum.setEnabled(false);
		minimumValueLabel = new JLabel("1.00", SwingConstants.CENTER);
		minimumValuePanel = new JPanel();
		minimumValuePanel.setLayout(new GridLayout(1,3));
		minimumValuePanel.add(decreaseMinimum);
		minimumValuePanel.add(minimumValueLabel);
		minimumValuePanel.add(increaseMinimum);
		for(int i = 0; i < CurrencyPair.tradingPairs.length/2; i++) {
			String label = CurrencyPair.tradingPairs[2*i].getBaseCurrency() + "/" + CurrencyPair.tradingPairs[2*i].getQuoteCurrency();
			currencyCheckBoxes[i] = new JCheckBox(label);
			currencyCheckBoxes[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boxChecked(e);
				}
			});
			currencyCheckBoxPanel.add(currencyCheckBoxes[i]);
		}
		currencyCheckBoxPanel.add(minimumValueTitleLabel);
		currencyCheckBoxPanel.add(minimumValuePanel);
		ratesPanel.add(periodPanel);
		this.add(ratesPanel);
		this.add(currencyCheckBoxPanel);
		this.setVisible(true);
		long lastUpdate = System.currentTimeMillis();
		while(true) {
			if(System.currentTimeMillis()-lastUpdate >= period) {
				lastUpdate = System.currentTimeMillis();
				update();
			}
		}
	}
	
	public static void main(String[] args) {
		new interfaceApp();
	}
	
	public void increasePeriod() {
		period+=1000;
		periodLabel.setText(""+period/1000+"s");
		decreasePeriod.setEnabled(true);
	}
	
	public void decreasePeriod() {
		period-=1000;
		periodLabel.setText(""+period/1000+"s");
		if(period==1000)
			decreasePeriod.setEnabled(false);
	}
	
	public void increaseMinimum() {
		if(minimumValue==100)
			decreaseMinimum.setEnabled(true);
		minimumValue++;
		double d = ((double)minimumValue) / 100;
		DecimalFormat df = new DecimalFormat("0.00");
		minimumValueLabel.setText(df.format(d));
	}
	
	public void decreaseMinimum() {
		minimumValue--;
		if(minimumValue==100)
			decreaseMinimum.setEnabled(false);
		double d = ((double)minimumValue) / 100;
		DecimalFormat df = new DecimalFormat("0.00");
		minimumValueLabel.setText(df.format(d));
	}
	
	public void boxChecked(ActionEvent e) {
		int i = 0;
		while(currencyCheckBoxes[i] != e.getSource())
			i++;
		boolean isSelected = currencyCheckBoxes[i].isSelected();
		if(isSelected) {
			dm.addCurrencyPair(CurrencyPair.tradingPairs[2*i]);
			dm.addCurrencyPair(CurrencyPair.tradingPairs[2*i+1]);
		} else {
			dm.removeCurrencyPair(CurrencyPair.tradingPairs[2*i]);
			dm.removeCurrencyPair(CurrencyPair.tradingPairs[2*i+1]);
		}
		update();
	}
	
	public void displayOptimalSequence() {
		try {
			double d = (double) minimumValue;
			d /= 100.0;
			TradeSequence optimalSequence = dm.getOptimalSequence(d);
			sequenceLabel.setText(optimalSequence.toString());
		} catch (NoPathException e) {
			if(dm.selectedCurrencyPairs.length==0)
				sequenceLabel.setText(dialogue[languageChoice][7]);
			else
				sequenceLabel.setText(dialogue[languageChoice][8]);
			returnLabel.setText("");
		} catch (OfflineException e) {
			sequenceLabel.setText("--");
			returnLabel.setText("--");
		}
	}
	
	public void update() {
		ExchangeDatabaseDriver.fetchData();
		boolean isOnline = true;
		for(int i = 0; i < dm.selectedCurrencyPairs.length; i++) {
			if(!dm.selectedCurrencyPairs[i].isOnline())
				isOnline = false;
		}
		if(!isOnline) {
			setCurrencyRates();
			return;
		}
		for(int i = 0; i < dm.selectedCurrencyPairs.length/2; i++) {
			String ticker = dm.selectedCurrencyPairs[i*2].getBaseCurrency() + "/" + dm.selectedCurrencyPairs[i*2].getQuoteCurrency();
			tickerLabels[i].setText(ticker);
			exchangeRateLabels[i].setText(""+dm.selectedCurrencyPairs[i*2].getExchangeRate());
		}
		for(int i = dm.selectedCurrencyPairs.length/2; i < CurrencyPair.tradingPairs.length/2; i++) {
			tickerLabels[i].setText("--");
			exchangeRateLabels[i].setText("--");
		}
		setCurrencyRates();
	}
	
	public void setLanguageChoice(String choice) {
		languageChoice = 0;
		while(!userLanguage[languageChoice].equals(choice))
			languageChoice++;
		returnTitleLabel.setText(dialogue[languageChoice][1]);
		sequenceTitleLabel.setText(dialogue[languageChoice][0]);
		languageLabel.setText(dialogue[languageChoice][3]);
		currencyTitleLabel.setText(dialogue[languageChoice][5]);
		minimumValueTitleLabel.setText(dialogue[languageChoice][6]);
		periodTitle.setText(dialogue[languageChoice][10]);
		update();
	}
	
	public void becomeBeast() {
		beastModeActive=true;
		languageChoice=0;
	}
	
	public void setCurrencyRates() {
		ExchangeDatabaseDriver.fetchData();
		boolean isOnline = true;
		for(int i = 0; i < dm.selectedCurrencyPairs.length; i++) {
			if(!dm.selectedCurrencyPairs[i].isOnline())
				isOnline = false;
		}
		if(!isOnline) {
			for(int i = 0; i < tickerLabels.length; i++) {
				tickerLabels[i].setText("--");
				exchangeRateLabels[i].setText("--");
			}
			currentRatesLabel.setText(dialogue[languageChoice][9]);
			displayOptimalSequence();
			return;
		}
		currentRatesLabel.setText(dialogue[languageChoice][4]);
		displayOptimalSequence();
	}
}
