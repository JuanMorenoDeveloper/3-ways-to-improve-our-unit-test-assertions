package ve.com.proitc;

import java.util.Objects;

class BankAccount {

  private final double balance;

  public BankAccount(double balance) {
    this.balance = balance;
  }

  public double getBalance() {
    return balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankAccount that = (BankAccount) o;
    return Double.compare(that.balance, balance) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(balance);
  }

  @Override
  public String toString() {
    return "BankAccount{" +
           "balance=" + balance +
           '}';
  }
}
