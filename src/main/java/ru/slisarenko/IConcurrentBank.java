package ru.slisarenko;

import java.math.BigDecimal;

public interface IConcurrentBank {
    BankAccount createAccount(int startBalance) throws IllegalArgumentException;
    void transfer(IBankAccount produceAccount, IBankAccount consumeAccount, int amount) throws IllegalArgumentException;
    BigDecimal getTotalBalance();
}
