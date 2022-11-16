import com.formdev.flatlaf.FlatDarkLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;


public class TradingUI {
    private JTabbedPane tabs;
    private JPanel panel;
    private JPanel graphPanel;
    private JLabel performanceLabel;
    private JPanel PageTitle;
    private JPanel graphContainer;
    private JPanel Overview;
    private JTable holdingsTable;
    private JPanel accountValPanel;
    private JPanel todayChangePanel;
    private JPanel holdingsPanel;
    private JLabel holdingsLabel;
    private JPanel tableContainer;
    private JPanel overviewPanel;
    private JLabel accountValueLabel;
    private JLabel tcLabel;
    private JLabel cashLabel;
    private JPanel cashPanel;
    private JLabel tcLabelPercent;
    private JPanel marketTime;
    private JComboBox symbolInput;
    private JPanel SymbolPanel;
    private JPanel optionPanel;
    private JPanel tradePanel;
    private JPanel stocksTitlePanel;
    private JComboBox actionInput;
    private JTextField quantityInput;

    private JComboBox orderTypeInput;
    private JPanel symbolPanel;
    private JPanel actionPanel;
    private JPanel quantityPanel;
    private JPanel orderTypePanel;
    private JPanel stockPanel;
    private JTextField priceInput;
    private JTextField durationInput;
    private JPanel stockGraph;
    private JButton order;
    private JPanel orderPanel;
    private JPanel limitPanel;
    private JLabel priceLabel;
    private JLabel durationLabel;
    private JLabel marketTimer;
    private JLabel marketStatus;
    private JComboBox wdActionInput;
    private JPanel transactionPanel;
    private JTextField wdAmount;
    private JButton wdButton;
    private JPanel wdOrderPanel;
    private JTable stockTable;
    private JTable transactionTable;
    private String orderType = "";
    JFreeChart stockChart;
    int id;
    private StockTradingModel model = new StockTradingModel();

    BigDecimal cash = new BigDecimal(0);

    TradingUI() {
        FlatDarkLaf.setup();

        priceInput.setVisible(false);
        durationInput.setVisible(false);

        priceLabel.setVisible(false);
        durationLabel.setVisible(false);
        orderTypeInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderType = String.valueOf(orderTypeInput.getSelectedItem());
                if (orderType.equals("") || orderType.equals("Market")) {
                    priceInput.setText("");
                    durationInput.setText("");

                    priceInput.setVisible(false);
                    durationInput.setVisible(false);

                    priceLabel.setVisible(false);
                    durationLabel.setVisible(false);
                } else {
                    priceInput.setVisible(true);
                    durationInput.setVisible(true);

                    priceLabel.setVisible(true);
                    durationLabel.setVisible(true);
                }
            }
        });
        symbolInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticker = String.valueOf(symbolInput.getSelectedItem());
                updateStockGraph(ticker);
            }
        });
        order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ticker = String.valueOf(symbolInput.getSelectedItem());
                String action = String.valueOf(actionInput.getSelectedItem());
                String orderType = String.valueOf(orderTypeInput.getSelectedItem());

                if (isInt(quantityInput.getText())) {
                    int quantity = Integer.parseInt(quantityInput.getText());
                    BigDecimal curPrice = model.getKeyPrices(ticker)[0];
                    BigDecimal totalCost = curPrice.multiply(BigDecimal.valueOf(quantity));
                    if (action.equals("Buy")) {
                        int volume = model.getVolume(ticker);
                        if (cash.compareTo(totalCost) >= 0) {
                            if (quantity <= volume) {
                                if (model.marketBuy(quantity, ticker, id, curPrice)) {
                                    System.out.println("Transaction done");
                                }
                            }
                        }
                    } else if (action.equals("Sell")) {
                        if (model.marketSell(quantity, ticker, id, curPrice)) {
                            System.out.println("Transaction done");
                        }
                    }
                    updateOverview();
                    updateHoldingsTable();
                    updateStockTable();
                    updateTransactionTable();

                    symbolInput.setSelectedIndex(0);
                    actionInput.setSelectedIndex(0);
                    quantityInput.setText("");

                }
            }
        });
        actionInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionType = String.valueOf(actionInput.getSelectedItem());
                if (actionType.equals("Buy") || actionType.equals("Sell")) {
                    order.setEnabled(true);
                } else {
                    order.setEnabled(false);
                }
            }
        });
        wdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String wdActionType = String.valueOf(wdActionInput.getSelectedItem());
                if (isInt(wdAmount.getText())) {
                    BigDecimal amount = new BigDecimal(wdAmount.getText());
                    if (wdActionType.equals("Withdraw")) {
                        if (cash.compareTo(amount) >= 0) {
                            model.withdraw(amount, id);
                        }
                    } else {
                        model.deposit(amount, id);
                    }
                    updateOverview();
                }

            }
        });
    }

    public void startTradingUI(int id) {

        this.id = id;

        JFrame stockTrading = new JFrame("Stock Trading");
        stockTrading.setContentPane(panel);
        stockTrading.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stockTrading.getContentPane().setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        stockTrading.pack();
        stockTrading.setVisible(true);

        tabs.putClientProperty("JTabbedPane.tabHeight", 50);
        tabs.putClientProperty("JTabbedPane.minimumTabWidth", 200);
        tabs.putClientProperty("JTabbedPane.tabAreaAlignment", "center");
        tabs.setFont(new Font("Helvetica Light", Font.PLAIN, 18));

        /*
         Add in graphPanel
         */

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(200, "", "1");
        dataset.addValue(150, "", "2");
        dataset.addValue(100, "", "3");
        dataset.addValue(210, "", "4");
        dataset.addValue(240, "", "5");
        dataset.addValue(195, "", "6");
        dataset.addValue(245, "", "7");

        JFreeChart chart = ChartFactory.createLineChart("", // Chart title
                "", // X-Axis Label
                "", // Y-Axis Label
                dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        chart.setBackgroundPaint(Color.getColor("2b2b2b"));
        chart.getPlot().setBackgroundPaint(Color.getColor("2b2b2b"));
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setOutlineVisible(false);
        plot.setAxisOffset(RectangleInsets.ZERO_INSETS);

        Font axisFont = new Font("Helvetica", Font.PLAIN, 16);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelPaint(Color.lightGray);
        xAxis.setTickLabelFont(axisFont);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickUnit(new NumberTickUnit(30));
        yAxis.setTickLabelPaint(Color.lightGray);
        yAxis.setTickLabelFont(axisFont);

        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);
        //plot.setRangeGridlinesVisible(false);
        Stroke gridStroke = new BasicStroke((float) 0.1);
        plot.setRangeGridlineStroke(gridStroke);

        ChartPanel CP = new ChartPanel(chart);
        CP.setPreferredSize(new Dimension(1400, 500));
        graphPanel.setLayout(new java.awt.BorderLayout());

        graphPanel.setBorder(new EmptyBorder(10, 50, 10, 50));


        graphPanel.add(CP, BorderLayout.CENTER);
        graphPanel.validate();


        /*
         Add in Holdings Table
         */

        holdingsTable = new JTable();

        holdingsTable.setBackground(Color.getColor("2b2b2b"));
        holdingsTable.setFont(new Font("Helvetica", Font.PLAIN, 16));

        holdingsTable.setRowHeight(35);
        holdingsTable.getTableHeader().setPreferredSize(new Dimension(-1, 40));
        holdingsTable.getTableHeader().setFont(new Font("Helvetica", Font.PLAIN, 16));

        holdingsPanel.setMinimumSize(new Dimension(-1, 500));

        holdingsTable.setFillsViewportHeight(true);
        JScrollPane sp = new JScrollPane(holdingsTable);
        holdingsPanel.setLayout(new java.awt.BorderLayout());
        holdingsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        holdingsPanel.add(sp, BorderLayout.CENTER);


        /*
         Add in Stock Table
         */

        stockTable = new JTable();
        stockTable.setBackground(Color.getColor("2b2b2b"));
        stockTable.setFont(new Font("Helvetica", Font.PLAIN, 16));

        stockTable.setRowHeight(35);
        stockTable.getTableHeader().setPreferredSize(new Dimension(-1, 40));
        stockTable.getTableHeader().setFont(new Font("Helvetica", Font.PLAIN, 16));

        stockPanel.setPreferredSize(new Dimension(1000, stockTrading.getHeight()));

        JScrollPane sp2 = new JScrollPane(stockTable);
        stockPanel.setLayout(new java.awt.BorderLayout());
        stockPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        stockPanel.add(sp2, BorderLayout.CENTER);

        /*
        Add in Stock Graph
         */

        stockChart = ChartFactory.createLineChart("", // Chart title
                "", // X-Axis Label
                "", // Y-Axis Label
                new DefaultCategoryDataset(),
                PlotOrientation.VERTICAL,
                false,
                false,
                false);
        stockChart.setBackgroundPaint(Color.getColor("2b2b2b"));
        stockChart.getPlot().setBackgroundPaint(Color.getColor("2b2b2b"));
        CategoryPlot stockPlot = stockChart.getCategoryPlot();
        stockPlot.setOutlineVisible(false);
        stockPlot.setAxisOffset(RectangleInsets.ZERO_INSETS);

        stockChart.getTitle().setFont(new Font("Helvetica", Font.PLAIN, 20));
        stockChart.getTitle().setPaint(Color.decode("#bbbbbb"));


        stockPlot.getDomainAxis().setTickLabelsVisible(false);
        stockPlot.getRangeAxis().setTickLabelsVisible(false);
        stockPlot.getDomainAxis().setVisible(false);
        stockPlot.getRangeAxis().setVisible(false);

        stockPlot.setRangeGridlinesVisible(false);
        stockPlot.setRangeGridlineStroke(gridStroke);

        ChartPanel stockCP = new ChartPanel(stockChart);
        stockCP.setPreferredSize(new Dimension(500, 300));
        stockGraph.setLayout(new java.awt.BorderLayout());

        stockGraph.setBorder(new EmptyBorder(0, 50, 10, 50));

        stockGraph.add(stockCP, BorderLayout.CENTER);
        stockGraph.validate();

        /*
        Transaction Table
         */
        transactionTable = new JTable();
        transactionTable.setBackground(Color.getColor("2b2b2b"));
        transactionTable.setFont(new Font("Helvetica", Font.PLAIN, 16));

        transactionTable.setRowHeight(35);
        transactionTable.getTableHeader().setPreferredSize(new Dimension(-1, 40));
        transactionTable.getTableHeader().setFont(new Font("Helvetica", Font.PLAIN, 16));

        transactionPanel.setPreferredSize(new Dimension(1000, stockTrading.getHeight()));

        JScrollPane sp3 = new JScrollPane(transactionTable);
        transactionPanel.setLayout(new java.awt.BorderLayout());
        transactionPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        transactionPanel.add(sp3, BorderLayout.CENTER);

        order.setPreferredSize(new Dimension(-1, 50));
        wdButton.setPreferredSize(new Dimension(-1, 50));

        updateStockTable();
        updateSymbolBox();
        updateHoldingsTable();
        updateOverview();
        updateTransactionTable();
    }

    private void updateStockTable() {
        String[][] stocks = model.getStocks();
        // Column Names
        String[] stockColumnNames = {"Name", "Symbol", "Current Price", "Volume", "Market Capitalization", "Opening Price", "Low Price", "High Price"};

        DefaultTableModel tableModel = new DefaultTableModel(stocks, stockColumnNames);
        stockTable.setModel(tableModel);


    }

    private void updateTransactionTable() {
        String[][] transactions = model.getTransactions(id);

        String[] transactionColumns = {"Symbol", "Order Status", "Expiration", "Limit Price", "Price", "Shares", "Order Time", "Exec Time", "Action", "Day"};
        DefaultTableModel tableModel = new DefaultTableModel(transactions, transactionColumns);
        transactionTable.setModel(tableModel);
    }

    private void updateHoldingsTable() {
        String[][] portfolio = model.getHoldings(id);
        String[] holdingsColumnNames = {"Symbol", "Current Price", "Shares", "Total Value", "Opening Price", "Low Price", "High Price"};

        DefaultTableModel tableModel = new DefaultTableModel(portfolio, holdingsColumnNames);
        holdingsTable.setModel(tableModel);
    }

    private void updateSymbolBox() {
        String[][] stocks = model.getStocks();

        String[] tickers = new String[stocks.length + 1];
        tickers[0] = "";
        for (int i = 0; i < stocks.length; i++) {
            tickers[i + 1] = stocks[i][1];
        }
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(tickers);
        symbolInput.setModel(model);
    }

    private void updateOverview() {
        BigDecimal[] overview = model.getOverview(id);

        cash = overview[3];
        accountValueLabel.setText("$" + overview[0]);
        tcLabel.setText("$" + overview[1]);
        tcLabelPercent.setText(overview[2] + "%");
        cashLabel.setText("$" + cash);

        int timeLeft = model.getMarketTimer();
        int hours = Math.abs(timeLeft) / 100;
        int minutes = Math.abs(timeLeft) % 100;
        if (timeLeft > 0) {
            marketStatus.setText("Market is Open");
            marketTimer.setText("Closes in " + hours + "hr, " + minutes + "min");
        } else if (timeLeft < 0) {
            marketStatus.setText("Market is Closed");
            marketTimer.setText("Opens in " + hours + "hr, " + minutes + "min");
        }
    }

    private void updateStockGraph(String ticker) {
        BigDecimal[] prices = model.getAllPrices(ticker);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int increment = (int) Math.ceil(prices.length / 20.0);
        for (int i = 0; i < prices.length; i += increment) {
            dataset.addValue(prices[i], "", String.valueOf(i));
        }
        stockChart.setTitle(ticker);
        stockChart.getCategoryPlot().setDataset(dataset);

    }

    private boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
