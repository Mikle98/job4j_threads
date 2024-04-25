package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("this")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        var rsl =  accounts.containsKey(account.id());
        if (!rsl) {
            accounts.put(account.id(), account);
        }
        return accounts.containsValue(account);
    }

    public synchronized boolean update(Account account) {
        var rsl = accounts.containsKey(account.id());
        if (rsl) {
            accounts.put(account.id(), account);
        }
        return rsl;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        var rsl = getById(fromId).get().amount() - amount >= 0;
        if (rsl) {
            update(new Account(fromId, getById(fromId).get().amount() - amount));
            update(new Account(toId, getById(toId).get().amount() + amount));
        }
        return rsl;
    }
}
