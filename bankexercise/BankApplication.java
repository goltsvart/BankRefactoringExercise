package bankexercise;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import static bankexercise.FileSystem.readFile;
import static bankexercise.FileSystem.saveFileAs;
import static bankexercise.FileSystem.writeFile;

public class BankApplication extends JFrame {

    static HashMap<Integer, BankAccount> table = new HashMap<Integer, BankAccount>();
    private final static int TABLE_SIZE = 29;
    JMenuBar menuBar;
    JMenu navigateMenu, recordsMenu, transactionsMenu, fileMenu, exitMenu;
    JMenuItem nextItem, prevItem, firstItem, lastItem, findByAccount, findBySurname, listAll;
    JMenuItem createItem, modifyItem, deleteItem, setOverdraft, setInterest;
    JMenuItem deposit, withdraw, calcInterest;
    JMenuItem open, save, saveAs;
    JMenuItem closeApp;
    JButton firstItemButton, lastItemButton, nextItemButton, prevItemButton;
    JLabel accountIDLabel, accountNumberLabel, firstNameLabel, surnameLabel, accountTypeLabel, balanceLabel, overdraftLabel;
    static JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    static JFileChooser fc;
    JTable jTable;
    double interestRate;

    static int currentItem = 1;

    static boolean openValues;

    public BankApplication() {

        super("Bank Application");
        initComponentsForDisplayPanel();
        initComponentsForButtonPanel();
        initComponentsForMenu();
        fileMenuFunctions();
        navigateMenuFunctions();
        navigateButtonsFunctions();
        recordsMenuFunctions();
        transactionsMenuFunctions();
    }

    public void initComponentsForDisplayPanel(){
        setLayout(new BorderLayout());
        JPanel displayPanel = new JPanel(new MigLayout());

        accountIDLabel = new JLabel("Account ID: ");
        accountIDTextField = new JTextField(15);
        accountIDTextField.setEditable(false);

        displayPanel.add(accountIDLabel, "growx, pushx");
        displayPanel.add(accountIDTextField, "growx, pushx, wrap");

        accountNumberLabel = new JLabel("Account Number: ");
        accountNumberTextField = new JTextField(15);
        accountNumberTextField.setEditable(false);

        displayPanel.add(accountNumberLabel, "growx, pushx");
        displayPanel.add(accountNumberTextField, "growx, pushx, wrap");

        surnameLabel = new JLabel("Last Name: ");
        surnameTextField = new JTextField(15);
        surnameTextField.setEditable(false);

        displayPanel.add(surnameLabel, "growx, pushx");
        displayPanel.add(surnameTextField, "growx, pushx, wrap");

        firstNameLabel = new JLabel("First Name: ");
        firstNameTextField = new JTextField(15);
        firstNameTextField.setEditable(false);

        displayPanel.add(firstNameLabel, "growx, pushx");
        displayPanel.add(firstNameTextField, "growx, pushx, wrap");

        accountTypeLabel = new JLabel("Account Type: ");
        accountTypeTextField = new JTextField(5);
        accountTypeTextField.setEditable(false);

        displayPanel.add(accountTypeLabel, "growx, pushx");
        displayPanel.add(accountTypeTextField, "growx, pushx, wrap");

        balanceLabel = new JLabel("Balance: ");
        balanceTextField = new JTextField(10);
        balanceTextField.setEditable(false);

        displayPanel.add(balanceLabel, "growx, pushx");
        displayPanel.add(balanceTextField, "growx, pushx, wrap");

        overdraftLabel = new JLabel("Overdraft: ");
        overdraftTextField = new JTextField(10);
        overdraftTextField.setEditable(false);

        displayPanel.add(overdraftLabel, "growx, pushx");
        displayPanel.add(overdraftTextField, "growx, pushx, wrap");

        add(displayPanel, BorderLayout.CENTER);
    }

    public void initComponentsForButtonPanel(){
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        nextItemButton = new JButton(new ImageIcon("next.png"));
        prevItemButton = new JButton(new ImageIcon("prev.png"));
        firstItemButton = new JButton(new ImageIcon("first.png"));
        lastItemButton = new JButton(new ImageIcon("last.png"));

        buttonPanel.add(firstItemButton);
        buttonPanel.add(prevItemButton);
        buttonPanel.add(nextItemButton);
        buttonPanel.add(lastItemButton);

        add(buttonPanel, BorderLayout.SOUTH);

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
    }

    public void initComponentsForMenu(){
        navigateMenu = new JMenu("Navigate");

        nextItem = new JMenuItem("Next Item");
        prevItem = new JMenuItem("Previous Item");
        firstItem = new JMenuItem("First Item");
        lastItem = new JMenuItem("Last Item");
        findByAccount = new JMenuItem("Find by Account Number");
        findBySurname = new JMenuItem("Find by Surname");
        listAll = new JMenuItem("List All Records");

        navigateMenu.add(nextItem);
        navigateMenu.add(prevItem);
        navigateMenu.add(firstItem);
        navigateMenu.add(lastItem);
        navigateMenu.add(findByAccount);
        navigateMenu.add(findBySurname);
        navigateMenu.add(listAll);

        menuBar.add(navigateMenu);

        recordsMenu = new JMenu("Records");

        createItem = new JMenuItem("Create Item");
        modifyItem = new JMenuItem("Modify Item");
        deleteItem = new JMenuItem("Delete Item");
        setOverdraft = new JMenuItem("Set Overdraft");
        setInterest = new JMenuItem("Set Interest");

        recordsMenu.add(createItem);
        recordsMenu.add(modifyItem);
        recordsMenu.add(deleteItem);
        recordsMenu.add(setOverdraft);
        recordsMenu.add(setInterest);

        menuBar.add(recordsMenu);

        transactionsMenu = new JMenu("Transactions");

        deposit = new JMenuItem("Deposit");
        withdraw = new JMenuItem("Withdraw");
        calcInterest = new JMenuItem("Calculate Interest");

        transactionsMenu.add(deposit);
        transactionsMenu.add(withdraw);
        transactionsMenu.add(calcInterest);

        menuBar.add(transactionsMenu);

        fileMenu = new JMenu("File");

        open = new JMenuItem("Open File");
        save = new JMenuItem("Save File");
        saveAs = new JMenuItem("Save As");

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(saveAs);

        menuBar.add(fileMenu);
        exitMenu = new JMenu("Exit");
        closeApp = new JMenuItem("Close Application");
        exitMenu.add(closeApp);
        menuBar.add(exitMenu);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void fileMenuFunctions(){

        open.addActionListener(e -> {
            readFile();
            currentItem = 1;
            while (!table.containsKey(currentItem)) {
                currentItem++;
            }
            Util.displayDetails(currentItem);
        });

        save.addActionListener(e -> writeFile());

        saveAs.addActionListener(e -> saveFileAs());

        closeApp.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(BankApplication.this, "Do you want to save before quitting?");
            if (answer == JOptionPane.YES_OPTION) {
                saveFileAs();
                dispose();
            } else if (answer == JOptionPane.NO_OPTION)
                dispose();
            else if (answer == 0) ;
        });
    }

    public void navigateButtonsFunctions(){
        ActionListener first = e -> {
            Util.saveOpenValues();
            currentItem = 1;
            Util.displayDetails(currentItem);
        };

        ActionListener next1 = e -> {
            if (currentItem != table.size()) {
                Util.saveOpenValues();
                currentItem++;
                Util.displayDetails(currentItem); }
        };

        ActionListener prev = e -> {
            if (currentItem != 1) {
                Util.saveOpenValues();
                currentItem--;
                Util.displayDetails(currentItem); }
        };

        ActionListener last = e -> {
            Util.saveOpenValues();
            currentItem = table.size();
            Util.displayDetails(currentItem);
        };

        nextItemButton.addActionListener(next1);
        nextItem.addActionListener(next1);

        prevItemButton.addActionListener(prev);
        prevItem.addActionListener(prev);

        firstItemButton.addActionListener(first);
        firstItem.addActionListener(first);

        lastItemButton.addActionListener(last);
        lastItem.addActionListener(last);
    }

    public void navigateMenuFunctions(){
        listAll.addActionListener(e -> {
            JFrame frame = new JFrame("TableDemo");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            String col[] = {"ID", "Number", "Name", "Account Type", "Balance", "Overdraft"};

            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            jTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(jTable);
            jTable.setAutoCreateRowSorter(true);
            Util.addListAllToTableModel(tableModel);
            frame.setSize(600, 500);
            frame.add(scrollPane);
            frame.setVisible(true);
        });
        findBySurname.addActionListener(e -> {
            String sName = JOptionPane.showInputDialog("Search for surname: ");
            boolean found = false;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                if (sName.equalsIgnoreCase((entry.getValue().getSurname().trim()))) {
                    found = true;
                    Util.setTextToFields(entry);
                }
            }
            if (found)
                JOptionPane.showMessageDialog(null, "Surname  " + sName + " found.");
            else
                JOptionPane.showMessageDialog(null, "Surname " + sName + " not found.");
        });

        findByAccount.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Search for account number: ");
            boolean found = false;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                    found = true;
                    Util.setTextToFields(entry);
                }
            }
            if (found)
                JOptionPane.showMessageDialog(null, "Account number " + accNum + " found.");
            else
                JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");

        });

    }

    public void recordsMenuFunctions() {
        deleteItem.addActionListener(e -> {
            table.remove(currentItem);
            JOptionPane.showMessageDialog(null, "Account Deleted");
            currentItem = 0;
            while (!table.containsKey(currentItem)) {
                currentItem++;
            }
            Util.displayDetails(currentItem);
        });

        createItem.addActionListener(e -> new CreateBankDialog(table));

        modifyItem.addActionListener(e -> {
            surnameTextField.setEditable(true);
            firstNameTextField.setEditable(true);

            openValues = true;
        });
        setOverdraft.addActionListener(e -> {
            if (table.get(currentItem).getAccountType().trim().equals("Current")) {
                String newOverdraftStr = JOptionPane.showInputDialog(null, "Enter new Overdraft", JOptionPane.OK_CANCEL_OPTION);
                overdraftTextField.setText(newOverdraftStr);
                table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
            } else
                JOptionPane.showMessageDialog(null, "Overdraft only applies to Current Accounts");

        });

        setInterest.addActionListener(e -> {
            String interestRateStr = JOptionPane.showInputDialog("Enter Interest Rate: (do not type the % sign)");
            if (interestRateStr != null)
                interestRate = Double.parseDouble(interestRateStr);
        });
    }
    public void transactionsMenuFunctions(){
        deposit.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Account number to deposit into: ");
            boolean found = false;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                    found = true;
                    String toDeposit = JOptionPane.showInputDialog("Account found, Enter Amount to Deposit: ");
                    entry.getValue().setBalance(entry.getValue().getBalance() + Double.parseDouble(toDeposit));
                    Util.displayDetails(entry.getKey());
                }
            }
            if (!found)
                JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");
        });

        withdraw.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Account number to withdraw from: ");
            String toWithdraw = JOptionPane.showInputDialog("Account found, Enter Amount to Withdraw: ");
            boolean found;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                    found = true;
                    if (entry.getValue().getAccountType().trim().equals("Current")) {
                        Util.withdrawCurrent(entry, toWithdraw);
                    } else if (entry.getValue().getAccountType().trim().equals("Deposit")) {
                        Util.withdrawDeposit(entry, toWithdraw);
                    }
                }
            }
        });

        calcInterest.addActionListener(e -> {
            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                if (entry.getValue().getAccountType().equals("Deposit")) {
                    double equation = 1 + ((interestRate) / 100);
                    entry.getValue().setBalance(entry.getValue().getBalance() * equation);
                    JOptionPane.showMessageDialog(null, "Balances Updated");
                    Util.displayDetails(entry.getKey());
                }
            }
        });
    }

    public static void main(String[] args) {
        BankApplication ba = new BankApplication();
        ba.setSize(1200, 400);
        ba.pack();
        ba.setVisible(true);
    }
}