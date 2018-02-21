package bankexercise;

import javax.swing.*;
import java.io.*;
import java.util.Map;

import static bankexercise.BankApplication.fc;

public class FileSystem {
    private static RandomAccessFile input;
    private static RandomAccessFile output;
    static String fileToSaveAs = "";

    public static void openFileRead() {
        BankApplication.table.clear();
        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        } else {
        }
        openFile();
    } // end method openFile

    public static void openFile() {
        try // open file
        {
            if (fc.getSelectedFile() != null)
                input = new RandomAccessFile(fc.getSelectedFile(), "r");
        } // end try
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "File Does Not Exist.");
        } // end catch
    }

    public static void openFileWrite() {
        if (fileToSaveAs != "") {
            try // open file
            {
                output = new RandomAccessFile(fileToSaveAs, "rw");
                JOptionPane.showMessageDialog(null, "Accounts saved to " + fileToSaveAs);
            } // end try
            catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "File does not exist.");
            } // end catch
        } else
            saveToFileAs();
    }

    public static void saveToFileAs() {
        fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            fileToSaveAs = file.getName();
            JOptionPane.showMessageDialog(null, "Accounts saved to " + file.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Save cancelled by user");
        }
        validateSaveToFileAs();
    }

    public static void validateSaveToFileAs() {
        try {
            if (fc.getSelectedFile() == null) {
                JOptionPane.showMessageDialog(null, "Cancelled");
            } else
                output = new RandomAccessFile(fc.getSelectedFile(), "rw");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void closeFile() {
        try // close file and exit
        {
            if (input != null)
                input.close();
        } // end try
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error closing file.");//System.exit( 1 );
        } // end catch
    } // end method closeFile

    public static void readRecords() {
        RandomAccessBankAccount record = new RandomAccessBankAccount();
        readRecord(record);
    }

    public static void readRecord(RandomAccessBankAccount record) {
        try // read a record and display
        {
            while (true) {
                if (input != null)
                    record.read(input);
                displayRecords(record);
            } // end while
        } // end try
        catch (EOFException eofException) // close file
        {
            return; // end of file was reached
        } // end catch
        catch (IOException ioException) {
            JOptionPane.showMessageDialog(null, "Error reading file.");
            System.exit(1);
        } // end catch
    }

    public static void displayRecords(RandomAccessBankAccount record) {
        BankAccount ba = new BankAccount(record.getAccountNumber(), record.getFirstName(),
                record.getSurname(), record.getAccountType(), record.getBalance(), record.getOverdraft());
        BankApplication.table.put(ba.getAccountID(), ba);
    }

    public static void saveToFile() {

        RandomAccessBankAccount record = new RandomAccessBankAccount();

        for (Map.Entry<Integer, BankAccount> entry : BankApplication.table.entrySet()) {
            record.setAccountNumber(entry.getValue().getAccountNumber());
            record.setFirstName(entry.getValue().getFirstName());
            record.setAccountID(entry.getValue().getAccountID());
            record.setSurname(entry.getValue().getSurname());
            record.setAccountType(entry.getValue().getAccountType());
            record.setBalance(entry.getValue().getBalance());
            record.setOverdraft(entry.getValue().getOverdraft());
            if (output != null) {
                try {
                    record.write(output);
                } catch (IOException u) {
                    u.printStackTrace();
                }
            }
        }
    }

    public static void writeFile() {
        openFileWrite();
        saveToFile();
        closeFile();
    }

    public static void saveFileAs() {
        saveToFileAs();
        saveToFile();
        closeFile();
    }

    public static void readFile() {
        openFileRead();
        readRecords();
        closeFile();
    }
}
