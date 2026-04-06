package ru.slisarenko;

import java.math.BigDecimal;

public interface IBankAccount {
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount);
    BigDecimal getBalance();
    long getId();
}
