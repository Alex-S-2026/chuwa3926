/**
 * Wallet class with deposit/withdraw and custom InsufficientBalanceException.
 */
public class Wallet {
    private double balance;

    public Wallet(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive, got: " + amount);
        }
        balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (balance < amount) {
            throw new InsufficientBalanceException(
                "Insufficient balance. Balance: $" + balance + ", requested: $" + amount);
        }
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        Wallet wallet = new Wallet(100);
        System.out.println("Initial balance: $" + wallet.getBalance());

        wallet.deposit(50);
        System.out.println("After depositing $50: $" + wallet.getBalance());

        try {
            wallet.withdraw(200);
        } catch (InsufficientBalanceException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }

        System.out.println("Final balance: $" + wallet.getBalance());
    }
}
