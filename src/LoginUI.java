import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    private JTextField username;
    private JPasswordField passwordInput;
    private JButton newAccountButton;
    private JButton loginButton;
    private JPasswordField confirmPasswordInput;
    private JPanel loginPanel;
    private JPanel usernamePanel;
    private JPanel passwordPanel;
    private JPanel confirmPasswordPanel;
    private JLabel accountMessage;

    boolean creatingNewAccount = false;
    JFrame login;
    private TradingUI tradingInterface = new TradingUI();

    private StockTradingModel model = new StockTradingModel();

    LoginUI() {
        newAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!creatingNewAccount) {
                    confirmPasswordPanel.setVisible(true);
                    newAccountButton.setText("Create Account");
                    creatingNewAccount = true;
                } else {
                    String password = String.valueOf(passwordInput.getPassword());
                    String confirmPassword = String.valueOf(confirmPasswordInput.getPassword());

                    if (password.length() > 0) {
                        if (password.equals(confirmPassword)) {
                            boolean successful = model.createUser(username.getText(), password);
                            if (!successful) {
                                accountMessage.setText("Failed to create User");
                                accountMessage.setForeground(Color.red);
                                accountMessage.setVisible(true);
                            } else {
                                System.out.println("New account with username: " + username.getText() + " and password: " + password);
                                accountMessage.setText("Account created. Click Login to continue.");
                                accountMessage.setForeground(Color.decode("#7cba89"));
                                accountMessage.setVisible(true);
                                //resetLogin();
                            }

                        } else {
                            accountMessage.setText("Passwords don't match");
                            accountMessage.setForeground(Color.red);
                            accountMessage.setVisible(true);
                        }
                    }
                }
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!creatingNewAccount) {
                    int id = model.matchUser(username.getText(), String.valueOf(passwordInput.getPassword()));
                    if (id == -1) {
                        accountMessage.setText("Account not found");
                        accountMessage.setForeground(Color.red);
                        accountMessage.setVisible(true);
                    } else {
                        // check for valid email and password
                        login.setVisible(false);
                        login.dispose();
                        tradingInterface.startTradingUI(id);
                    }
                } else {
                    resetLogin();
                }
            }
        });
    }

    private void resetLogin() {
        confirmPasswordPanel.setVisible(false);
        newAccountButton.setText("New Account");
        creatingNewAccount = false;
        confirmPasswordInput.setText("");
        username.setText("");
        passwordInput.setText("");
        accountMessage.setVisible(false);
    }

    public void startLoginUI() {
        login = new JFrame("Login");
        login.setContentPane(loginPanel);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.getContentPane().setPreferredSize(new Dimension(800, 500));
        login.pack();
        login.setVisible(true);

        accountMessage.setVisible(false);
        confirmPasswordPanel.setVisible(false);

    }
}
