package bankexercise;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

import static bankexercise.BankApplication.*;

public class Util {
    public static void withdrawCurrent(Map.Entry<Integer, BankAccount> entry, String toWithdraw) {
        if (Double.parseDouble(toWithdraw) > entry.getValue().getBalance() + entry.getValue().getOverdraft())
            JOptionPane.showMessageDialog(null, "Transaction exceeds overdraft limit");
        else {
            entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
            displayDetails(entry.getKey());
        }
    }

    public  static void withdrawDeposit(Map.Entry<Integer, BankAccount> entry, String toWithdraw) {
        if (Double.parseDouble(toWithdraw) <= entry.getValue().getBalance()) {
            entry.getValue().setBalance(entry.getValue().getBalance() - Double.parseDouble(toWithdraw));
            displayDetails(entry.getKey());
        } else JOptionPane.showMessageDialog(null, "Insufficient funds.");
    }

    public static void addListAllToTableModel(DefaultTableModel tableModel) {
        for (Map.Entry<Integer, BankAccount> entry : table.entrySet()) {
            Object[] objs = {entry.getValue().getAccountID(), entry.getValue().getAccountNumber(),
                    entry.getValue().getFirstName().trim() + " " + entry.getValue().getSurname().trim(),
                    entry.getValue().getAccountType(), entry.getValue().getBalance(),
                    entry.getValue().getOverdraft()};
            tableModel.addRow(objs);
        }
    }

    public static void displayDetails(int currentItem) {

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

    public static void saveOpenValues() {
        if (openValues) {
            surnameTextField.setEditable(false);
            firstNameTextField.setEditable(false);

            table.get(currentItem).setSurname(surnameTextField.getText());
            table.get(currentItem).setFirstName(firstNameTextField.getText());
        }
    }
    public static void setTextToFields(Map.Entry<Integer, BankAccount> entry) {
        accountIDTextField.setText(entry.getValue().getAccountID() + "");
        accountNumberTextField.setText(entry.getValue().getAccountNumber());
        surnameTextField.setText(entry.getValue().getSurname());
        firstNameTextField.setText(entry.getValue().getFirstName());
        accountTypeTextField.setText(entry.getValue().getAccountType());
        balanceTextField.setText(entry.getValue().getBalance() + "");
        overdraftTextField.setText(entry.getValue().getOverdraft() + "");
    }
}
