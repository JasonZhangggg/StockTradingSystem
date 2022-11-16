import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Admin {
    private JPanel adminPanel;
    private JTextField symbolInput;
    private JTextField nameInput;
    private JTextField volumeInput;
    private JTextField priceInput;
    private JButton addButton;
    private JTextField marketOpen;
    private JTextField marketClose;
    private JButton changeHoursButton;
    JFrame admin;

    StockTradingModel model = new StockTradingModel();
    Admin(){
        admin = new JFrame("Admin");
        admin.setContentPane(adminPanel);
        admin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        admin.getContentPane().setPreferredSize(new Dimension(800, 500));
        admin.pack();
        admin.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addStock(symbolInput.getText(), nameInput.getText(), Integer.parseInt(volumeInput.getText()), new BigDecimal(priceInput.getText()));
                symbolInput.setText("");
                nameInput.setText("");
                volumeInput.setText("");
                priceInput.setText("");

            }
        });
        changeHoursButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.changeMarketHours(Integer.parseInt(marketOpen.getText()), Integer.parseInt(marketClose.getText()));
            }
        });
    }
    public static void main(String args[]){
        FlatDarkLaf.setup();
        new Admin();
    }
}
