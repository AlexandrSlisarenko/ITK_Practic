package ru.slisarenko;

import java.math.BigDecimal;

public class BankAccount implements IBankAccount{
    private BigDecimal balance;
    private long id;

    public BankAccount(long id, BigDecimal balance) {
        this.balance = balance;
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public void deposit(BigDecimal amount) {
        this.balance = balance.add(amount);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        this.balance = balance.subtract(amount);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance;
    }
}
