/**

 * - alex_shen
 * - 2026-3-11
 * - Java OOP Homework 1
 * - Submission: Submit your answers as a single document or code files
 *
 * Q12. Create a BankAccount class with proper encapsulation:
 *  - Private fields: accountNumber (String), balance (double)
 *  - A constructor that takes accountNumber and sets initial balance to 0
 *  - Getter methods for both fields (no setter for accountNumber - it should not be changed after creation)
 *  - A deposit(double amount) method that adds money to balance (only if amount > 0)
 *  - A withdraw(double amount) method that subtracts money from balance (only if amount > 0 and balance >= amount)
 *  - Both methods should return true if successful, false otherwise
 * 
 *  Write a main method to test deposit and withdraw operations.
 */

public class BankAccount {
    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber) {
        if (accountNumber != null && !accountNumber.isEmpty()) {
            this.accountNumber = accountNumber;
        }
        this.balance = 0.0d;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }

    public boolean deposit(double amount) {
        if (amount > 0.0d) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0.0d && this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount("Test Account 1");
        System.out.println("Account: " + account.getAccountNumber() + ", Balance: " + account.getBalance());

        System.out.println("Deposit 1: " + account.deposit(1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Deposit -0 (invalid): " + account.deposit(-0));
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Deposit -1 (invalid): " + account.deposit(-1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Withdraw 1: " + account.withdraw(1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Withdraw 0.000000000000001 (exceeds balance): " + account.withdraw(0.000000000000001d));
        System.out.println("Balance: " + account.getBalance());
        System.out.println("Withdraw -1 (invalid): " + account.withdraw(-1));
        System.out.println("Balance: " + account.getBalance());

        System.out.println("Deposit 0.000000000000001: " + account.deposit(0.000000000000001d));
        System.out.println("Balance: " + account.getBalance());
    }
}