package bankexercise;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.*;
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
    JTextField accountIDTextField, accountNumberTextField, firstNameTextField, surnameTextField, accountTypeTextField, balanceTextField, overdraftTextField;
    static JFileChooser fc;
    JTable jTable;
    double interestRate;
    static private final String newline = "\n";

    int currentItem = 1;

    boolean openValues;

    public BankApplication() {

        super("Bank Application");
        initComponents();
    }

    public void initComponents() {
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

        setOverdraft.addActionListener(e -> {
            if (table.get(currentItem).getAccountType().trim().equals("Current")) {
                String newOverdraftStr = JOptionPane.showInputDialog(null, "Enter new Overdraft", JOptionPane.OK_CANCEL_OPTION);
                overdraftTextField.setText(newOverdraftStr);
                table.get(currentItem).setOverdraft(Double.parseDouble(newOverdraftStr));
            } else
                JOptionPane.showMessageDialog(null, "Overdraft only applies to Current Accounts");

        });

        ActionListener first = e -> {
            saveOpenValues();
            currentItem = 1;
            displayDetails(currentItem);
        };

        ActionListener next1 = e -> {
            if (currentItem != table.size()) {
                saveOpenValues();
                currentItem++;
                displayDetails(currentItem);
            }
        };

        ActionListener prev = e -> {
            if (currentItem != 1) {
                saveOpenValues();
                currentItem--;
                displayDetails(currentItem);
            }
        };

        ActionListener last = e -> {
            saveOpenValues();
            currentItem = table.size();
            displayDetails(currentItem);
        };

        nextItemButton.addActionListener(next1);
        nextItem.addActionListener(next1);

        prevItemButton.addActionListener(prev);
        prevItem.addActionListener(prev);

        firstItemButton.addActionListener(first);
        firstItem.addActionListener(first);

        lastItemButton.addActionListener(last);
        lastItem.addActionListener(last);

        deleteItem.addActionListener(e -> {

            table.remove(currentItem);
            JOptionPane.showMessageDialog(null, "Account Deleted");
            currentItem = 0;
            while (!table.containsKey(currentItem)) {
                currentItem++;
            }
            displayDetails(currentItem);
        });

        createItem.addActionListener(e -> new CreateBankDialog(table));

        modifyItem.addActionListener(e -> {
            surnameTextField.setEditable(true);
            firstNameTextField.setEditable(true);

            openValues = true;
        });

        setInterest.addActionListener(e -> {
            String interestRateStr = JOptionPane.showInputDialog("Enter Interest Rate: (do not type the % sign)");
            if (interestRateStr != null)
                interestRate = Double.parseDouble(interestRateStr);
        });

        listAll.addActionListener(e -> {
            JFrame frame = new JFrame("TableDemo");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            String col[] = {"ID", "Number", "Name", "Account Type", "Balance", "Overdraft"};

            DefaultTableModel tableModel = new DefaultTableModel(col, 0);
            jTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(jTable);
            jTable.setAutoCreateRowSorter(true);
            addListAllToTableModel(tableModel);
            frame.setSize(600, 500);
            frame.add(scrollPane);
            frame.setVisible(true);
        });

        open.addActionListener(e -> {
            readFile();
            currentItem = 1;
            while (!table.containsKey(currentItem)) {
                currentItem++;
            }
            displayDetails(currentItem);
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

        findBySurname.addActionListener(e -> {
            String sName = JOptionPane.showInputDialog("Search for surname: ");
            boolean found = false;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {

                if (sName.equalsIgnoreCase((entry.getValue().getSurname().trim()))) {
                    found = true;
                    setTextToFields(entry);
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
                    setTextToFields(entry);
                }
            }
            if (found)
                JOptionPane.showMessageDialog(null, "Account number " + accNum + " found.");
            else
                JOptionPane.showMessageDialog(null, "Account number " + accNum + " not found.");

        });

        deposit.addActionListener(e -> {
            String accNum = JOptionPane.showInputDialog("Account number to deposit into: ");
            boolean found = false;

            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                if (accNum.equals(entry.getValue().getAccountNumber().trim())) {
                    found = true;
                    String toDeposit = JOptionPane.showInputDialog("Account found, Enter Amount to Deposit: ");
                    entry.getValue().setBalance(entry.getValue().getBalance() + Double.parseDouble(toDeposit));
                    displayDetails(entry.getKey());
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
                        withdrawCurrent(entry, toWithdraw);
                    } else if (entry.getValue().getAccountType().trim().equals("Deposit")) {
                        withdrawDeposit(entry, toWithdraw);
                    }
                }
            }
        });

        calcInterest.addActionListener(e -> {
            for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
                if (entry.getValue().getAccountType().equals("Deposit")) {
                    double equation = 1 + ((interestRate) / 100);
                    entry.getValue().setBalance(entry.getValue().getBalance() * equation);
                    //System.out.println(equation);
                    JOptionPane.showMessageDialog(null, "Balances Updated");
                    displayDetails(entry.getKey());
                }
            }
        });
    }

    public void withdrawCurrent(Map.Entry<Integer, BankAccount> entry, String toWithdraw) {
        if (Double.parseDouble(toWithdraw) > entry.getValue().getBalance() + entry.getValue().getOverdraft())
            JOptionPane.showMessageDialog(null, "Transaction exceeds overdraft limit");
        else {
            entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
            displayDetails(entry.getKey());
        }
    }

    public void withdrawDeposit(Map.Entry<Integer, BankAccount> entry, String toWithdraw) {
        if (Double.parseDouble(toWithdraw) <= entry.getValue().getBalance()) {
            entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
            displayDetails(entry.getKey());
        } else JOptionPane.showMessageDialog(null, "Insufficient funds.");
    }

    public void addListAllToTableModel(DefaultTableModel tableModel) {
        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
            Object[] objs = {entry.getValue().getAccountID(), entry.getValue().getAccountNumber(),
                    entry.getValue().getFirstName().trim() + " " + entry.getValue().getSurname().trim(),
                    entry.getValue().getAccountType(), entry.getValue().getBalance(),
                    entry.getValue().getOverdraft()};
            tableModel.addRow(objs);
        }
    }

    public void setTextToFields(Map.Entry<Integer, BankAccount> entry) {
        accountIDTextField.setText(entry.getValue().getAccountID() + "");
        accountNumberTextField.setText(entry.getValue().getAccountNumber());
        surnameTextField.setText(entry.getValue().getSurname());
        firstNameTextField.setText(entry.getValue().getFirstName());
        accountTypeTextField.setText(entry.getValue().getAccountType());
        balanceTextField.setText(entry.getValue().getBalance() + "");
        overdraftTextField.setText(entry.getValue().getOverdraft() + "");
    }

    public void saveOpenValues() {
        if (openValues) {
            surnameTextField.setEditable(false);
            firstNameTextField.setEditable(false);

            table.get(currentItem).setSurname(surnameTextField.getText());
            table.get(currentItem).setFirstName(firstNameTextField.getText());
        }
    }

    public void displayDetails(int currentItem) {

        accountIDTextField.setText(table.get(currentItem).getAccountID() + "");
        accountNumberTextField.setText(table.get(currentItem).getAccountNumber());
        surnameTextField.setText(table.get(currentItem).getSurname());
        firstNameTextField.setText(table.get(currentItem).getFirstName());
        accountTypeTextField.setText(table.get(currentItem).getAccountType());
        balanceTextField.setText(table.get(currentItem).getBalance() + "");
        if (accountTypeTextField.getText().trim().equals("Current"))
            overdraftTextField.setText(table.get(currentItem).getOverdraft() + "");
        else
            overdraftTextField.setText("Only applies to current accs");
    }

//    private static RandomAccessFile input;
//    private static RandomAccessFile output;
//
//    public static void openFileRead() {
//        table.clear();
//        fc = new JFileChooser();
//        int returnVal = fc.showOpenDialog(null);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fc.getSelectedFile();
//        } else {
//        }
//        openFile();
//    } // end method openFile
//
//    public static void openFile() {
//        try // open file
//        {
//            if (fc.getSelectedFile() != null)
//                input = new RandomAccessFile(fc.getSelectedFile(), "r");
//        } // end try
//        catch (IOException ioException) {
//            JOptionPane.showMessageDialog(null, "File Does Not Exist.");
//        } // end catch
//    }
//
//    static String fileToSaveAs = "";
//
//    public static void openFileWrite() {
//        if (fileToSaveAs != "") {
//            try // open file
//            {
//                output = new RandomAccessFile(fileToSaveAs, "rw");
//                JOptionPane.showMessageDialog(null, "Accounts saved to " + fileToSaveAs);
//            } // end try
//            catch (IOException ioException) {
//                JOptionPane.showMessageDialog(null, "File does not exist.");
//            } // end catch
//        } else
//            saveToFileAs();
//    }
//
//    public static void saveToFileAs() {
//        fc = new JFileChooser();
//
//        int returnVal = fc.showSaveDialog(null);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            File file = fc.getSelectedFile();
//            fileToSaveAs = file.getName();
//            JOptionPane.showMessageDialog(null, "Accounts saved to " + file.getName());
//        } else {
//            JOptionPane.showMessageDialog(null, "Save cancelled by user");
//        }
//        validateSaveToFileAs();
//    }
//
//    public static void validateSaveToFileAs() {
//        try {
//            if (fc.getSelectedFile() == null) {
//                JOptionPane.showMessageDialog(null, "Cancelled");
//            } else
//                output = new RandomAccessFile(fc.getSelectedFile(), "rw");
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    public static void closeFile() {
//        try // close file and exit
//        {
//            if (input != null)
//                input.close();
//        } // end try
//        catch (IOException ioException) {
//            JOptionPane.showMessageDialog(null, "Error closing file.");//System.exit( 1 );
//        } // end catch
//    } // end method closeFile
//
//    public static void readRecords() {
//        RandomAccessBankAccount record = new RandomAccessBankAccount();
//            readRecord(record);
//    }
//
//    public static void readRecord(RandomAccessBankAccount record) {
//
//        try {
//        if (input != null) {
//            while (true)
//                record.read(input);
//        }
//        } catch (EOFException e) {
//            displayRecords(record);
//            e.printStackTrace();
//            return;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void displayRecords(RandomAccessBankAccount record) {
//        BankAccount ba = new BankAccount(record.getAccountNumber(), record.getFirstName(),
//                record.getSurname(), record.getAccountType(), record.getBalance(), record.getOverdraft());
////		Integer key = Integer.valueOf(ba.getAccountNumber().trim());
////		int hash = (key%TABLE_SIZE);
////
////		while(table.containsKey(hash)){
////
////			hash = hash+1;
////		}
////		table.put(hash, ba);
//        table.put(ba.getAccountID(), ba);
//    }
//
//    public static void saveToFile() {
//
//        RandomAccessBankAccount record = new RandomAccessBankAccount();
//
//        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
//            record.setAccountNumber(entry.getValue().getAccountNumber());
//            record.setFirstName(entry.getValue().getFirstName());
//            record.setAccountID(entry.getValue().getAccountID());
//            record.setSurname(entry.getValue().getSurname());
//            record.setAccountType(entry.getValue().getAccountType());
//            record.setBalance(entry.getValue().getBalance());
//            record.setOverdraft(entry.getValue().getOverdraft());
//            if (output != null) {
//                try {
//                    record.write(output);
//                } catch (IOException u) {
//                    u.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void writeFile() {
//        openFileWrite();
//        saveToFile();
//        closeFile();
//    }
//
//    public static void saveFileAs() {
//        saveToFileAs();
//        saveToFile();
//        closeFile();
//    }
//
//    public static void readFile() {
//        openFileRead();
//        readRecords();
//        closeFile();
//    }
//
//    public void put(int key, BankAccount value) {
//        int hash = (key % TABLE_SIZE);
//        while (table.containsKey(key)) {
//            hash = hash + 1;
//        }
//        table.put(hash, value);
//
//    }

    public static void main(String[] args) {
        BankApplication ba = new BankApplication();
        ba.setSize(1200, 400);
        ba.pack();
        ba.setVisible(true);
    }
}