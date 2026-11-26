package com.codechanakya.processor;

/**
 * Moves money between two accounts.
 *
 * <p>A transfer must hold <em>both</em> locks, or another thread could see the money in neither
 * account, or in both. That is where deadlock comes from. Imagine thread 1 moves A to B and thread
 * 2 moves B to A at the same time. If each locks its "from" account first, thread 1 holds A and
 * waits for B, thread 2 holds B and waits for A, and neither can ever move.
 *
 * <p>The cure is a rule everyone follows: <strong>always lock the account with the smaller id
 * first</strong>. Both threads then try for the same lock first, one of them wins, and the other
 * simply waits its turn. A cycle cannot form.
 */
public final class Bank {

    private Bank() {
    }

    /**
     * @return {@code true} if the money moved, {@code false} if the source did not have enough
     */
    public static boolean transfer(Account from, Account to, long amount) {
        if (amount <= 0) {                                    // I2
            throw new IllegalArgumentException("Amount must be positive, got " + amount);
        }
        Account first = from.id() < to.id() ? from : to;      // the fixed order
        Account second = first == from ? to : from;

        first.lock().lock();
        try {
            second.lock().lock();
            try {
                if (from.balance() < amount) {
                    return false;
                }
                from.change(-amount);
                to.change(amount);
                return true;
            } finally {
                second.lock().unlock();
            }
        } finally {
            first.lock().unlock();
        }
    }

    /**
     * C1: the other way to avoid deadlock. Try for each lock with a short wait; if the second one
     * cannot be had, let go of the first and report failure, so the caller can try again later.
     */
    public static boolean tryTransfer(Account from, Account to, long amount) throws InterruptedException {
        if (from.lock().tryLock(10, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            try {
                if (to.lock().tryLock(10, java.util.concurrent.TimeUnit.MILLISECONDS)) {
                    try {
                        if (from.balance() < amount) {
                            return false;
                        }
                        from.change(-amount);
                        to.change(amount);
                        return true;
                    } finally {
                        to.lock().unlock();
                    }
                }
            } finally {
                from.lock().unlock();
            }
        }
        return false;                                         // could not get both: nothing changed
    }
}
