package ru.slisarenko;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBank implements IConcurrentBank{
    private List<IBankAccount> bank;
    private final ExecutorService executor;
    private final AtomicLong idGenerator;

    public ConcurrentBank() {
        this.bank = new CopyOnWriteArrayList<>();
        this.executor = Executors.newCachedThreadPool();
        this.idGenerator = new AtomicLong(0);
    }

    @Override
    public BankAccount createAccount(int startBalance) throws IllegalArgumentException {
        if(startBalance < 0){
            throw new IllegalArgumentException("Negative balance");
        }
        var account = new BankAccount(this.idGenerator.incrementAndGet(), BigDecimal.valueOf(startBalance));
        this.bank.add(account);
        return account;
    }

    @Override
    public void transfer(IBankAccount produceAccount,
                                      IBankAccount consumeAccount,
                                      int amount) throws IllegalArgumentException {
        if(amount < 0){
            throw new IllegalArgumentException("Amount negative");
        }
        var amountBigDecimal = new BigDecimal(amount);
        if(produceAccount.getBalance().compareTo(amountBigDecimal) < 0){
            throw new IllegalArgumentException("Amount not enough");
        }
        var firstLock = produceAccount.getId() < consumeAccount.getId()? produceAccount : consumeAccount;
        var lastLock = produceAccount.getId() < consumeAccount.getId()? consumeAccount : produceAccount;
        synchronized (firstLock) {
            synchronized (lastLock) {
                produceAccount.withdraw(amountBigDecimal);
                consumeAccount.deposit(amountBigDecimal);
            }
        }

    }

    @Override
    public synchronized BigDecimal getTotalBalance() {
        return this.bank.stream().map(IBankAccount::getBalance).reduce(BigDecimal.ZERO,BigDecimal::add);
    }


}
