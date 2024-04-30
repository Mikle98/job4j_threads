package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return Objects.isNull(accounts.putIfAbsent(account.id(), account));
    }

    public synchronized boolean update(Account account) {
        return Objects.isNull(accounts.replace(account.id(), account));
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        var rsl = false;
        var fromAccountOptional = getById(fromId);
        var toAccountOptional = getById(toId);
        if (fromAccountOptional.isPresent()
                && toAccountOptional.isPresent()) {
            rsl = fromAccountOptional.get().amount() - amount >= 0;
            if (rsl) {
                update(new Account(fromId, fromAccountOptional.get().amount() - amount));
                update(new Account(toId, toAccountOptional.get().amount() + amount));
            }
        }
        return rsl;
    }
}
