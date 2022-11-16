import static sts.generated.Tables.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.conf.Settings;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.*;
import sts.generated.enums.StsStockOrderExecType;
import sts.generated.enums.StsStockOrderOrderStatus;
import sts.generated.enums.StsUserRole;
import sts.generated.tables.StsUser;

import static org.jooq.impl.DSL.*;

public class StockTradingModel {
    private DSLContext db = null;
    Connection conn;

    MathContext m = new MathContext(2);

    StockTradingModel() {
    }

    private void connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/mydev", "root", "Zb121101");
            System.out.println("Connected");
            db = DSL.using(conn, SQLDialect.MYSQL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(String username, String password) {
        connectDB();
        try {
            db.insertInto(STS_USER, STS_USER.LOGIN, STS_USER.PASSWARD, STS_USER.ROLE, STS_USER.CASH_BALANCE).values(username, password, StsUserRole.valueOf("user"), new BigDecimal(0)).execute();
            return true;
        } catch (DataAccessException e) {
            System.out.println(e);
            return false;
        }
    }

    public int matchUser(String username, String password) {
        try {
            connectDB();
            org.jooq.Record user = db.select(STS_USER.USER_ID).from(STS_USER).where(STS_USER.LOGIN.eq(username), STS_USER.PASSWARD.eq(password)).fetchSingle();
            return user.getValue(STS_USER.USER_ID);

        } catch (DataAccessException e) {
            System.out.println(e);
            return -1;
        }
    }

    public BigDecimal getCashBalance(int id) {
        try {
            connectDB();
            org.jooq.Record user = db.select(STS_USER.CASH_BALANCE).from(STS_USER).where(STS_USER.USER_ID.eq(id)).fetchSingle();
            BigDecimal cashBalance = user.getValue(STS_USER.CASH_BALANCE);
            cashBalance = cashBalance.setScale(2, RoundingMode.HALF_UP);
            return cashBalance;

        } catch (DataAccessException e) {
            System.out.println(e);
            return null;
        }
    }

    public BigDecimal[] getOverview(int id) {
        try {
            connectDB();
            Result<Record> holdingRecord = db.select().from(STS_PORTFOLIO).where(STS_PORTFOLIO.USER_ID.eq(id)).fetch();
            BigDecimal[] overview = new BigDecimal[4];

            BigDecimal cashBalance = getCashBalance(id);
            BigDecimal accountTotalVal = cashBalance;
            BigDecimal totalOpeningPrice = new BigDecimal(0);
            BigDecimal totalCurPrice = new BigDecimal(0);
            BigDecimal percentChange = new BigDecimal(0);
            BigDecimal change = new BigDecimal(0);

            for (Record holdings : holdingRecord) {
                // Get number of shares and tiker
                String ticker = holdings.getValue(STS_PORTFOLIO.TICKER);
                int shares = holdings.getValue(STS_PORTFOLIO.SHARES);

                BigDecimal[] keyPrices = getKeyPrices(ticker);
                BigDecimal curPrice = keyPrices[0];
                BigDecimal openingPrice = keyPrices[1];
                totalOpeningPrice = totalOpeningPrice.add(openingPrice.multiply(BigDecimal.valueOf(shares)));
                totalCurPrice = totalCurPrice.add(curPrice.multiply(BigDecimal.valueOf(shares)));

                BigDecimal stockTotalVal = curPrice.multiply(BigDecimal.valueOf(shares));
                stockTotalVal = stockTotalVal.setScale(2, RoundingMode.HALF_UP);
                accountTotalVal = accountTotalVal.add(stockTotalVal);
            }
            if (totalOpeningPrice.compareTo(BigDecimal.valueOf(0)) != 0) {
                change = totalCurPrice.subtract(totalOpeningPrice);
                change = change.setScale(2, RoundingMode.HALF_UP);
                percentChange = change.divide(totalOpeningPrice, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

                percentChange = percentChange.setScale(2, RoundingMode.HALF_UP);
            }

            overview[0] = accountTotalVal;
            overview[1] = change;
            overview[2] = percentChange;
            overview[3] = cashBalance;

            return overview;
        } catch (DataAccessException e) {
            System.out.println(e);
            return new BigDecimal[0];
        }
    }

    public String[][] getHoldings(int id) {
        try {
            connectDB();
            Result<Record> holdingRecord = db.select().from(STS_PORTFOLIO).where(STS_PORTFOLIO.USER_ID.eq(id)).fetch();
            int holdingCount = 0;
            String[][] holdingStocks = new String[holdingRecord.size()][8];
            for (Record holdings : holdingRecord) {
                // Get number of shares and tiker
                String ticker = holdings.getValue(STS_PORTFOLIO.TICKER);
                int shares = holdings.getValue(STS_PORTFOLIO.SHARES);

                // holdingsColumnNames: Symbol, Current Price, Shares, Total Value, Opening Price, High Price, Low Price

                BigDecimal[] keyPrices = getKeyPrices(ticker);

                BigDecimal totalVal = keyPrices[0].multiply(BigDecimal.valueOf(shares));
                totalVal = totalVal.setScale(2, RoundingMode.HALF_UP);

                holdingStocks[holdingCount][0] = ticker;
                holdingStocks[holdingCount][1] = "$" + keyPrices[0];
                holdingStocks[holdingCount][2] = String.valueOf(shares);
                holdingStocks[holdingCount][3] = "$" + String.format("%.2f", totalVal);
                holdingStocks[holdingCount][4] = "$" + keyPrices[1];
                holdingStocks[holdingCount][5] = "$" + keyPrices[2];
                holdingStocks[holdingCount][6] = "$" + keyPrices[3];

                holdingCount++;
            }
            return holdingStocks;
        } catch (DataAccessException e) {
            System.out.println(e);
            return new String[0][0];
        }
    }

    public String[][] getStocks() {
        try {
            // Get current day

            connectDB();
            Result<Record> stockRecord = db.select().from(STS_STOCK).fetch();

            int stockCount = 0;
            String[][] stockVals = new String[stockRecord.size()][8];
            for (Record stock : stockRecord) {
                // Get attributes of stock

                String ticker = stock.getValue(STS_STOCK.TICKER);
                String companyName = stock.getValue(STS_STOCK.COMPANY);
                int volume = stock.getValue(STS_STOCK.SHARES);
                BigDecimal initPrice = stock.getValue(STS_STOCK.INIT_PRICE);
                int isListed = stock.getValue(STS_STOCK.IS_LISTED);

                BigDecimal[] keyPrices = getKeyPrices(ticker);
                BigDecimal marketCap = keyPrices[0].multiply(BigDecimal.valueOf(volume));
                marketCap = marketCap.setScale(2, RoundingMode.HALF_UP);

                stockVals[stockCount][0] = companyName;
                stockVals[stockCount][1] = ticker;
                stockVals[stockCount][2] = "$" + keyPrices[0];
                stockVals[stockCount][3] = String.valueOf(volume);
                stockVals[stockCount][4] = "$" + String.format("%.2f", marketCap);
                stockVals[stockCount][5] = "$" + keyPrices[1];
                stockVals[stockCount][6] = "$" + keyPrices[2];
                stockVals[stockCount][7] = "$" + keyPrices[3];

                stockCount++;
            }
            return stockVals;

        } catch (DataAccessException e) {
            System.out.println(e);
            return new String[0][0];
        }
    }

    public BigDecimal[] getAllPrices(String ticker) {
        try {
            // Get current day

            connectDB();
            Record marketRecord = db.select().from(STS_MARKET).fetchOne();
            int current_day = marketRecord.getValue(STS_MARKET.CURRENT_MARKET_DAY);

            // Find prices of current stock
            Result<Record> stockHistoryRecord = db.select().from(STS_STOCK_HISTORY).where(STS_STOCK_HISTORY.TICKER.eq(ticker), STS_STOCK_HISTORY.MARKET_DAY.eq(current_day)).fetch();
            BigDecimal[] allPrices = new BigDecimal[stockHistoryRecord.size()];
            int i = 0;
            for (Record stockHistory : stockHistoryRecord) {
                BigDecimal price = stockHistory.getValue(STS_STOCK_HISTORY.PRICE);
                price = price.setScale(2, RoundingMode.HALF_UP);
                allPrices[i] = price;
                i++;
            }
            return allPrices;

        } catch (DataAccessException e) {
            System.out.println(e);
            return new BigDecimal[0];
        }
    }

    public BigDecimal[] getKeyPrices(String ticker) {
        try {
            // Get current day

            connectDB();
            Record marketRecord = db.select().from(STS_MARKET).fetchOne();
            int current_day = marketRecord.getValue(STS_MARKET.CURRENT_MARKET_DAY);

            // Find prices of current stock
            Result<Record> stockHistoryRecord = db.select().from(STS_STOCK_HISTORY).where(STS_STOCK_HISTORY.TICKER.eq(ticker), STS_STOCK_HISTORY.MARKET_DAY.eq(current_day)).fetch();
            BigDecimal[] keyPrices = new BigDecimal[4];
            BigDecimal minPrice = new BigDecimal(Integer.MAX_VALUE);
            BigDecimal maxPrice = new BigDecimal(Integer.MIN_VALUE);
            BigDecimal curPrice = new BigDecimal(0);
            BigDecimal openingPrice = new BigDecimal(-1);
            for (Record stockHistory : stockHistoryRecord) {
                BigDecimal price = stockHistory.getValue(STS_STOCK_HISTORY.PRICE);
                price = price.setScale(2, RoundingMode.HALF_UP);
                if (price.compareTo(maxPrice) > 0) {
                    maxPrice = price;
                }
                if (price.compareTo(minPrice) < 0) {
                    minPrice = price;
                }
                if (openingPrice.compareTo(BigDecimal.valueOf(-1)) == 0) {
                    openingPrice = price;
                }
                curPrice = price;
            }
            keyPrices[0] = curPrice;
            keyPrices[1] = openingPrice;
            keyPrices[2] = minPrice;
            keyPrices[3] = maxPrice;
            return keyPrices;

        } catch (DataAccessException e) {
            System.out.println(e);
            return new BigDecimal[0];
        }
    }

    public int getMarketTimer() {
        try {

            connectDB();
            Calendar now = Calendar.getInstance();
            int timeMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

            Record market = (Record) db.select().from(STS_MARKET).fetchSingle(); // assume 1 market
            int openTime = market.getValue(STS_MARKET.OPEN_TIME);
            int closeTime = market.getValue(STS_MARKET.CLOSE_TIME);
            boolean isOpen = market.getValue(STS_MARKET.IS_OPEN) == 1 ? true : false;
            int sign = 1;
            int timeLeft = 0;
            int openMinutes = (openTime / 100) * 60 + (openTime % 10);
            int closeMinutes = (closeTime / 100) * 60 + (closeTime % 10);

            if (isOpen) {
                timeLeft = closeMinutes - timeMinutes;
            } else {
                sign = -1;
                timeLeft = openMinutes - timeMinutes;
            }
            if (timeLeft < 0) {
                timeLeft += 1440;
            }
            timeLeft = Integer.parseInt((timeLeft / 60) + "" + (timeLeft % 60));
            return timeLeft * sign;
        } catch (DataAccessException e) {
            System.out.println(e);
            return 0;
        }
    }

    public int getVolume(String ticker) {
        try {
            connectDB();

            // Find prices of current stock
            Record stockRecord = db.select().from(STS_STOCK).where(STS_STOCK.TICKER.eq(ticker)).fetchOne();
            int volume = stockRecord.getValue(STS_STOCK.SHARES);

            return volume;

        } catch (DataAccessException e) {
            System.out.println(e);
            return -1;
        }
    }

    public boolean marketBuy(int quantity, String ticker, int id, BigDecimal curPrice) {
        try {
            connectDB();

            BigDecimal amount = curPrice.multiply(BigDecimal.valueOf(quantity));

            Record marketRecord = db.select().from(STS_MARKET).fetchOne();
            int current_day = marketRecord.getValue(STS_MARKET.CURRENT_MARKET_DAY);
            Record stockRecord = db.select().from(STS_PORTFOLIO).where(STS_PORTFOLIO.USER_ID.eq(id), STS_PORTFOLIO.TICKER.eq(ticker)).fetchOne();

            if(stockRecord == null){
                db.insertInto(STS_PORTFOLIO, STS_PORTFOLIO.USER_ID, STS_PORTFOLIO.TICKER, STS_PORTFOLIO.SHARES).values(id, ticker, 0).execute();
            }

            db.update(STS_STOCK).set(STS_STOCK.SHARES, STS_STOCK.SHARES.subtract(quantity)).where(STS_STOCK.TICKER.eq(ticker)).execute();

            db.update(STS_PORTFOLIO).set(STS_PORTFOLIO.SHARES, STS_PORTFOLIO.SHARES.add(quantity)).where(STS_PORTFOLIO.TICKER.eq(ticker)).execute();
            withdraw(amount, id);

            db.insertInto(STS_STOCK_ORDER, STS_STOCK_ORDER.USER_ID, STS_STOCK_ORDER.TICKER, STS_STOCK_ORDER.ORDER_STATUS, STS_STOCK_ORDER.EXPIRATION, STS_STOCK_ORDER.LIMIT_PRICE, STS_STOCK_ORDER.EXEC_PRICE, STS_STOCK_ORDER.SHARES, STS_STOCK_ORDER.ORDER_TIME, STS_STOCK_ORDER.EXEC_TIME, STS_STOCK_ORDER.EXEC_TYPE, STS_STOCK_ORDER.MARKET_DAY)
                    .values(id, ticker, StsStockOrderOrderStatus.valueOf("close"), 0, new BigDecimal(0), curPrice, quantity, LocalDateTime.now(), LocalDateTime.now(), StsStockOrderExecType.valueOf("buy_market"), current_day).execute();
            return true;

        } catch (DataAccessException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean marketSell(int quantity, String ticker, int id, BigDecimal curPrice) {
        try {
            connectDB();

            BigDecimal amount = curPrice.multiply(BigDecimal.valueOf(quantity));

            Calendar now = Calendar.getInstance();
            int curTime = now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE);

            Record marketRecord = db.select().from(STS_MARKET).fetchOne();
            int current_day = marketRecord.getValue(STS_MARKET.CURRENT_MARKET_DAY);

            db.update(STS_STOCK).set(STS_STOCK.SHARES, STS_STOCK.SHARES.add(quantity)).where(STS_STOCK.TICKER.eq(ticker)).execute();

            db.update(STS_PORTFOLIO).set(STS_PORTFOLIO.SHARES, STS_PORTFOLIO.SHARES.subtract(quantity)).where(STS_PORTFOLIO.TICKER.eq(ticker)).execute();

            deposit(amount, id);

            db.insertInto(STS_STOCK_ORDER, STS_STOCK_ORDER.USER_ID, STS_STOCK_ORDER.TICKER, STS_STOCK_ORDER.ORDER_STATUS, STS_STOCK_ORDER.EXPIRATION, STS_STOCK_ORDER.LIMIT_PRICE, STS_STOCK_ORDER.EXEC_PRICE, STS_STOCK_ORDER.SHARES, STS_STOCK_ORDER.ORDER_TIME, STS_STOCK_ORDER.EXEC_TIME, STS_STOCK_ORDER.EXEC_TYPE, STS_STOCK_ORDER.MARKET_DAY)
                    .values(id, ticker, StsStockOrderOrderStatus.valueOf("close"), 0, new BigDecimal(0), curPrice, quantity, LocalDateTime.now(), LocalDateTime.now(), StsStockOrderExecType.valueOf("sell_market"), current_day).execute();
            return true;

        } catch (DataAccessException e) {
            System.out.println(e);
            return false;
        }
    }

    public String[][] getTransactions(int id) {
        try {
            connectDB();

            Result<Record> transactionRecord = db.select().from(STS_STOCK_ORDER).where(STS_STOCK_ORDER.USER_ID.eq(id)).fetch();
            String[][] transactions = new String[transactionRecord.size()][10];
            int transactionCount = 0;
            for (Record transaction : transactionRecord) {
                String ticker = transaction.getValue(STS_STOCK_ORDER.TICKER);
                String order_status = String.valueOf(transaction.getValue(STS_STOCK_ORDER.ORDER_STATUS));
                String expiration = String.valueOf(transaction.getValue(STS_STOCK_ORDER.EXPIRATION));
                String limitPrice = String.valueOf(transaction.getValue(STS_STOCK_ORDER.LIMIT_PRICE));
                String execPrice = String.valueOf(transaction.getValue(STS_STOCK_ORDER.EXEC_PRICE));
                String shares = String.valueOf(transaction.getValue(STS_STOCK_ORDER.SHARES));
                String orderTime = String.valueOf(transaction.getValue(STS_STOCK_ORDER.EXEC_TIME));
                String execTime = String.valueOf(transaction.getValue(STS_STOCK_ORDER.ORDER_TIME));
                String execType = String.valueOf(transaction.getValue(STS_STOCK_ORDER.EXEC_TYPE));
                String marketDay = String.valueOf(transaction.getValue(STS_STOCK_ORDER.MARKET_DAY));
                transactions[transactionCount][0] = ticker;
                transactions[transactionCount][1] = order_status;
                transactions[transactionCount][2] = expiration;
                transactions[transactionCount][3] = limitPrice;
                transactions[transactionCount][4] = execPrice;
                transactions[transactionCount][5] = shares;
                transactions[transactionCount][6] = orderTime;
                transactions[transactionCount][7] = execTime;
                transactions[transactionCount][8] = execType;
                transactions[transactionCount][9] = marketDay;

                transactionCount++;
            }
            return transactions;
        }catch (DataAccessException e) {
            System.out.println(e);
            return new String[0][0];
        }
    }

    public boolean addStock(String ticker, String companyName, int volume, BigDecimal startingPrice){
        try {
            connectDB();

            db.insertInto(STS_STOCK, STS_STOCK.TICKER, STS_STOCK.COMPANY, STS_STOCK.SHARES, STS_STOCK.INIT_PRICE, STS_STOCK.IS_LISTED)
                    .values(ticker, companyName, volume, startingPrice, (byte)0).execute();

            return true;

        } catch (DataAccessException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean changeMarketHours(int openTime, int closeTime){
        try {
            connectDB();

            db.update(STS_MARKET).set(STS_MARKET.OPEN_TIME, openTime).where(STS_MARKET.MARKET_NAME.eq("DOW")).execute();
            db.update(STS_MARKET).set(STS_MARKET.CLOSE_TIME, closeTime).where(STS_MARKET.MARKET_NAME.eq("DOW")).execute();

            return true;

        } catch (DataAccessException e) {
            System.out.println(e);
            return false;
        }
    }

    public void deposit(BigDecimal amount, int id) {
        try {
            connectDB();

            db.update(STS_USER).set(STS_USER.CASH_BALANCE, STS_USER.CASH_BALANCE.add(amount)).where(STS_USER.USER_ID.eq(id)).execute();


        } catch (DataAccessException e) {
            System.out.println(e);
        }

    }

    public void withdraw(BigDecimal amount, int id) {
        try {
            connectDB();

            db.update(STS_USER).set(STS_USER.CASH_BALANCE, STS_USER.CASH_BALANCE.subtract(amount)).where(STS_USER.USER_ID.eq(id)).execute();


        } catch (DataAccessException e) {
            System.out.println(e);
        }

    }
}
