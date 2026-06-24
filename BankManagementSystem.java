import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

// PERSON CLASS
class Person {
    private String name;
    private String cnic;
    private String phone;
    public Person(String name, String cnic, String phone) {
        this.name = name;
        this.cnic = cnic;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public String getCnic() {
        return cnic;
    }
    public String getPhone() {
        return phone;
    }

    public String toString() {
        return name + "," + cnic + "," + phone;
    }
}

//ACCOUNT CLASS
class Account {
    private String accountNo;
    private double balance;
    public Account(String accountNo, double balance) {
        this.accountNo = accountNo;
        this.balance = balance;
    }
    public String getAccountNo() {
        return accountNo;
    }
    public double getBalance() {
        return balance;
    }
    public void deposit(double amount) {
        if(amount>0){
        balance += amount;}
    }
    public boolean withdraw(double amount){
        if(amount<=0){
            return false;}
            if (balance>=amount){
                balance-=amount;
                return true;
            }
            return false;
        }
    @Override
    public String toString() {
        return accountNo + "," + balance;
    }
}

// CLIENT CLASS

class Client {
    private Person person;
    private Account account;

    public Client(Person person, Account account) {
        this.person = person;
        this.account = account;
    }
    public Person getPerson() {
        return person;
    }
    public Account getAccount() {
        return account;
    }
    @Override
    public String toString() {
        return person.getName() + "," +
                person.getCnic() + "," +
                person.getPhone() + "," +
                account.getAccountNo() + "," +
                account.getBalance();
    }
}

//BANK CLASS
class Bank {
    private ArrayList<Client> clients;

    public Bank() {
        clients = new ArrayList<>();
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void addClient(Client c) {
        clients.add(c);
    }

    public Client searchClientByCNIC(String cnic) {
        for(Client c : clients) {
            if(c.getPerson().getCnic().equals(cnic))
                return c;
        }
        return null;
    }
    public Client searchAccount(String accountNo) {
        for(Client c : clients) {
            if(c.getAccount().getAccountNo().equals(accountNo))
                return c;
        }
        return null;
    }
    public boolean removeClient(String cnic) {
        Client found = searchClientByCNIC(cnic);
        if(found != null) {
            clients.remove(found);
            return true;
        }
        return false;
    }
    public double totalBankAmount() {
        double total = 0;
        for(Client c : clients) {
            total += c.getAccount().getBalance();
        }
        return total;
    }
    // Check duplicate CNIC
    public boolean cnicExists(String cnic) {
        for (Client c : clients) {
            if (c.getPerson().getCnic().equals(cnic)) {
                return true;
            }
        }
        return false;
    }

    // Check duplicate Account Number
    public boolean accountExists(String accNo) {
        for (Client c : clients) {
            if (c.getAccount().getAccountNo().equalsIgnoreCase(accNo)) {
                return true;
            }
        }
        return false;
    }
}
// FILE HANDLING

class FileManager {
    private static final String FILE_NAME = "bankdata.txt";
    public static void saveData(Bank bank) {
        try {
            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter(FILE_NAME));
            for(Client c : bank.getClients()) {
                bw.write(c.toString());
                bw.newLine();
            }
            bw.close();
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error Saving File!"
            );
        }
    }
    public static void saveTransaction(String cnic, String message) {

        try {

            BufferedWriter bw =
                    new BufferedWriter(
                            new FileWriter("transactions.txt", true));

            bw.write(cnic + "," + message);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error Saving Transaction!");
        }
    }

        public static String getHistory(String cnic) {
            String history = "";
            try {
                            File file = new File("transactions.txt");
                            if (!file.exists())
                                return "No Transactions";
                            BufferedReader br =
                                    new BufferedReader(
                                            new FileReader(file));
                            String line;
                            while ((line = br.readLine()) != null) {
                                String[] data = line.split(",", 2);
                                if (data[0].equals(cnic)) {
                                    history += data[1] + "\n";
                    }
                }
                br.close();
            } catch (Exception e) {
                return "Error Loading History";
            }
            return history;
        }

    public static Bank loadData() {
        Bank bank = new Bank();
        try {
            File file = new File(FILE_NAME);
            if(!file.exists())
                return bank;
            BufferedReader br =
                    new BufferedReader(
                            new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String cnic = data[1];
                String phone = data[2];
                String accNo = data[3];
                double balance = Double.parseDouble(data[4]);
                Person p = new Person(name, cnic, phone);
                Account a = new Account(accNo, balance);
                Client c = new Client(p, a);
                bank.addClient(c);
            }
            br.close();

        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error Loading File!"
            );
        }

        return bank;
    }
}
// MAIN CLASS

public class BankManagementSystem {

    static Bank bank = FileManager.loadData(); // Load data on startup

    public static void main(String[] args) {

        new MainMenu();
    }
}

//MAIN MENU FRAME

class MainMenu extends JFrame {

    public MainMenu() {

        setTitle("Bank System - Main Menu");
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel title = new JLabel("BANKING SYSTEM");
        title.setBounds(120, 30, 200, 30);
        JButton createBtn = new JButton("Create Account");
        createBtn.setBounds(120, 80, 150, 40);
        JButton userBtn = new JButton("User Login");
        userBtn.setBounds(120, 140, 150, 40);
        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setBounds(120, 200, 150, 40);
        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(120, 260, 150, 40);
        add(title);
        add(createBtn);
        add(adminBtn);
        add(userBtn);
        add(exitBtn);

        // CREATE ACCOUNT
        createBtn.addActionListener(e -> {
            dispose();
            new CreateAccountFrame();
        });

        // ADMIN LOGIN
        adminBtn.addActionListener(e -> {
            dispose();
            new AdminLoginFrame();
        });
// user login
        userBtn.addActionListener(e -> {
            dispose();
            new UserLoginFrame();
        });
        // EXIT (SAVE BEFORE EXIT)
        exitBtn.addActionListener(e -> {

            FileManager.saveData(BankManagementSystem.bank);
            System.exit(0);
        });

        setVisible(true);
    }
}

//CREATE ACCOUNT FRAME

class CreateAccountFrame extends JFrame {

    public CreateAccountFrame() {

        setTitle("Create Account");
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel nameL = new JLabel("Name:");
        nameL.setBounds(50, 50, 100, 25);

        JTextField nameF = new JTextField();
        nameF.setBounds(150, 50, 150, 25);

        JLabel cnicL = new JLabel("CNIC:");
        cnicL.setBounds(50, 90, 100, 25);

        JTextField cnicF = new JTextField();
        cnicF.setBounds(150, 90, 150, 25);

        JLabel phoneL = new JLabel("Phone:");
        phoneL.setBounds(50, 130, 100, 25);

        JTextField phoneF = new JTextField();
        phoneF.setBounds(150, 130, 150, 25);

        JLabel accL = new JLabel("Account No:");
        accL.setBounds(50, 170, 100, 25);

        JTextField accF = new JTextField();
        accF.setBounds(150, 170, 150, 25);

        JButton create = new JButton("Create");
        create.setBounds(120, 220, 120, 40);

        JButton back = new JButton("Back");
        back.setBounds(120, 270, 120, 40);

        add(nameL);
        add(nameF);
        add(cnicL);
        add(cnicF);
        add(phoneL);
        add(phoneF);
        add(accL);
        add(accF);
        add(create);
        add(back);

        // CREATE ACCOUNT ACTION
        create.addActionListener(e -> {
            String name = nameF.getText().trim();
            String cnic = cnicF.getText().trim();
            String phone = phoneF.getText().trim();
            String accNo = accF.getText().trim();


            // ===== VALIDATIONS =====

            if (!isValidName(name)) {
                JOptionPane.showMessageDialog(this,
                        "Name must contain only letters!");
                return;
            }

            if (!isValidCNIC(cnic)) {
                JOptionPane.showMessageDialog(this,
                        "CNIC must be exactly 13 digits!");
                return;
            }

            if (!isValidPhone(phone)) {
                JOptionPane.showMessageDialog(this,
                        "Phone number must be exactly 11 digits!");
                return;
            }

            if (!isValidAccountNo(accNo)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Account Number must be 1 letter followed by 3 digits (Example: a123)"
                );
                return;
            }
            if (BankManagementSystem.bank.cnicExists(cnic)) {
                JOptionPane.showMessageDialog(this,
                        "CNIC already exists!");
                return;
            }

            if (BankManagementSystem.bank.accountExists(accNo)) {
                JOptionPane.showMessageDialog(this,
                        "Account number already exists!");
                return;
            }

            // ===== CREATE OBJECTS =====

            Person p = new Person(name, cnic, phone);
            Account a = new Account(accNo, 0);
            Client c = new Client(p, a);

            BankManagementSystem.bank.addClient(c);
            FileManager.saveData(BankManagementSystem.bank);

            JOptionPane.showMessageDialog(this,
                    "Account Created Successfully!");


            nameF.setText("");
            cnicF.setText("");
            phoneF.setText("");
            accF.setText("");
        });

        // BACK
        back.addActionListener(e -> {

            dispose();
            new MainMenu();
        });

        setVisible(true);
    }

    // Name should NOT contain numbers
    public boolean isValidName(String name) {

        return name.matches("[a-zA-Z ]+");
    }

    // CNIC must be 13 digits only
    public boolean isValidCNIC(String cnic) {

        return cnic.matches("\\d{13}");
    }

    // Phone must be 11 digits only
    public boolean isValidPhone(String phone) {

        return phone.matches("\\d{11}");
    }

    public boolean isValidAccountNo(String accNo) {

        return accNo.matches("[A-Za-z]\\d{3}");
    }
}
// ================= ADMIN LOGIN FRAME =================

class AdminLoginFrame extends JFrame {

    public AdminLoginFrame() {

        setTitle("Admin Login");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel userL = new JLabel("Username:");
        userL.setBounds(50, 50, 100, 25);

        JTextField userF = new JTextField();
        userF.setBounds(150, 50, 120, 25);

        JLabel passL = new JLabel("Password:");
        passL.setBounds(50, 90, 100, 25);

        JPasswordField passF = new JPasswordField();
        passF.setBounds(150, 90, 120, 25);

        JButton login = new JButton("Login");
        login.setBounds(110, 140, 100, 30);

        add(userL); add(userF);
        add(passL); add(passF);
        add(login);

        login.addActionListener(e -> {

            String u = userF.getText();
            String p = new String(passF.getPassword());

            if(u.equals("admin") && p.equals("123")) {

                dispose();
                new AdminDashboard();
            }

            else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Login!"
                );
            }
        });

        setVisible(true);
    }
}

// ================= ADMIN DASHBOARD =================

class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(400, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton viewAll = new JButton("View All Clients");
        viewAll.setBounds(100, 40, 200, 40);

        JButton searchClient = new JButton("Search Client (CNIC)");
        searchClient.setBounds(100, 90, 200, 40);

        JButton remove = new JButton("Remove Client");
        remove.setBounds(100, 140, 200, 40);

        JButton total = new JButton("Total Bank Amount");
        total.setBounds(100, 190, 200, 40);

        JButton logout = new JButton("Logout");
        logout.setBounds(100, 240, 200, 40);

        add(viewAll);
        add(searchClient);
        add(remove);
        add(total);
        add(logout);

        // VIEW ALL
        viewAll.addActionListener(e -> {

            String data = "";

            for(Client c : BankManagementSystem.bank.getClients()) {

                data += c.toString() + "\n\n";
            }

            JOptionPane.showMessageDialog(this, data);
        });

        // SEARCH CLIENT
        searchClient.addActionListener(e -> {

            String cnic = JOptionPane.showInputDialog("Enter CNIC");

            Client c = BankManagementSystem.bank.searchClientByCNIC(cnic);

            if(c != null) {
                String history =
                        FileManager.getHistory(cnic);

                JOptionPane.showMessageDialog(
                        this,
                        c.toString()
                                + "\n\nTransaction History:\n"
                                + history
                );
            }
            else
                JOptionPane.showMessageDialog(this, "Not Found");
        });

        // REMOVE CLIENT
        remove.addActionListener(e -> {

            String cnic = JOptionPane.showInputDialog("Enter CNIC");

            boolean ok = BankManagementSystem.bank.removeClient(cnic);

            if(ok)
                JOptionPane.showMessageDialog(this, "Removed Successfully");
            else
                JOptionPane.showMessageDialog(this, "Not Found");

            FileManager.saveData(BankManagementSystem.bank);
        });

        // TOTAL BANK
        total.addActionListener(e -> {

            double t = BankManagementSystem.bank.totalBankAmount();

            JOptionPane.showMessageDialog(this, "Total: " + t);
        });



        // LOGOUT
        logout.addActionListener(e -> {

            FileManager.saveData(BankManagementSystem.bank);
            dispose();
            new MainMenu();
        });

        setVisible(true);
    }
}

// ================= USER LOGIN =================

class UserLoginFrame extends JFrame {

    public UserLoginFrame() {

        setTitle("User Login");
        setSize(350, 250);
        setLayout(null);

        JLabel accL = new JLabel("Account No:");
        accL.setBounds(50, 50, 100, 25);

        JTextField accF = new JTextField();
        accF.setBounds(150, 50, 120, 25);

        JLabel cnicL = new JLabel("CNIC:");
        cnicL.setBounds(50, 90, 100, 25);

        JTextField cnicF = new JTextField();
        cnicF.setBounds(150, 90, 120, 25);

        JButton login = new JButton("Login");
        login.setBounds(110, 140, 100, 30);

        add(accL); add(accF);
        add(cnicL); add(cnicF);
        add(login);

        login.addActionListener(e -> {

            Client c = BankManagementSystem.bank.searchAccount(accF.getText());

            if(c != null && c.getPerson().getCnic().equals(cnicF.getText())) {

                dispose();
                new UserDashboard(c);
            }

            else {

                JOptionPane.showMessageDialog(this, "Invalid Login");
            }
        });

        setVisible(true);
    }
}

// ================= USER DASHBOARD =================

class UserDashboard extends JFrame {

    Client client;

    public UserDashboard(Client c) {

        this.client = c;

        setTitle("User Dashboard");
        setSize(400, 500);
        setLayout(null);

        JButton bal = new JButton("Check Balance");
        bal.setBounds(100, 50, 200, 40);

        JButton dep = new JButton("Deposit");
        dep.setBounds(100, 100, 200, 40);

        JButton with = new JButton("Withdraw");
        with.setBounds(100, 150, 200, 40);

        JButton transfer = new JButton("Transfer");
        transfer.setBounds(100, 200, 200, 40);

        JButton logout = new JButton("Logout");
        logout.setBounds(100, 250, 200, 40);

        add(bal);
        add(dep);
        add(with);
        add(transfer);
        add(logout);

        // BALANCE
        bal.addActionListener(e -> {

            JOptionPane.showMessageDialog(this,
                    "Balance: " + client.getAccount().getBalance());
        });

        // DEPOSIT
        dep.addActionListener(e -> {

            try {

                double amt = Double.parseDouble(
                        JOptionPane.showInputDialog("Enter Amount"));

                if (amt <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0!"
                    );

                    return;
                }

                client.getAccount().deposit(amt);
                FileManager.saveTransaction(
                        client.getPerson().getCnic(),
                        "Deposited Rs." + amt);
                ;
                FileManager.saveData(BankManagementSystem.bank);

                JOptionPane.showMessageDialog(
                        this,
                        "Amount Deposited Successfully!"
                );

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric amount!"
                );
            }
        });

        // WITHDRAW
        with.addActionListener(e -> {

            try {

                double amt = Double.parseDouble(
                        JOptionPane.showInputDialog("Enter Amount"));

                if (amt <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0!"
                    );

                    return;
                }

                boolean ok = client.getAccount().withdraw(amt);

                if (ok) {
                    FileManager.saveTransaction(
                            client.getPerson().getCnic(),
                            "Withdrew Rs." + amt);

                    JOptionPane.showMessageDialog(
                            this,
                            "Withdrawal Successful!"
                    );
                }
                else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Insufficient Balance!"
                    );
                }

                FileManager.saveData(BankManagementSystem.bank);

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric amount!"
                );
            }
        });

        // TRANSFER
        transfer.addActionListener(e -> {

            try {

                String accNo =
                        JOptionPane.showInputDialog(
                                "Transfer To Account Number");

                double amt =
                        Double.parseDouble(
                                JOptionPane.showInputDialog(
                                        "Enter Amount"));

                if (amt <= 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Amount must be greater than 0!"
                    );

                    return;
                }

                Client target =
                        BankManagementSystem.bank.searchAccount(accNo);
                if (target != null &&
                        target.getAccount().getAccountNo()
                                .equals(client.getAccount().getAccountNo())) {

                    JOptionPane.showMessageDialog(
                            this,
                            "You cannot transfer to your own account!"
                    );

                    return;
                }

                if (target == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Target Account Not Found!"
                    );
                    return;
                }

                if (client.getAccount().getBalance() < amt) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Insufficient Balance!"
                    );

                    return;
                }
                client.getAccount().withdraw(amt);
                target.getAccount().deposit(amt);
// Save sender history
                FileManager.saveTransaction(
                        client.getPerson().getCnic(),
                        "Transferred Rs." + amt +
                                " to " +
                                target.getAccount().getAccountNo());

// Save receiver history
                FileManager.saveTransaction(
                        target.getPerson().getCnic(),
                        "Received Rs." + amt +
                                " from " +
                                client.getAccount().getAccountNo());
                FileManager.saveData(BankManagementSystem.bank);
                JOptionPane.showMessageDialog(
                        this,
                        "Transfer Successful!"
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter a valid numeric amount!"
                );
            }
        });

        // LOGOUT
        logout.addActionListener(e -> {
            FileManager.saveData(BankManagementSystem.bank);
            dispose();
            new MainMenu();
        });
        setVisible(true);
    }
}