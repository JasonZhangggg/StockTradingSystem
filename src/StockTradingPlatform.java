import com.formdev.flatlaf.FlatDarkLaf;

public class StockTradingPlatform {
    LoginUI loginInterface = new LoginUI();
    StockTradingPlatform(){
        loginInterface.startLoginUI();
    }
    public static void main(String[] args){
        FlatDarkLaf.setup();
        new StockTradingPlatform();
    }

}
