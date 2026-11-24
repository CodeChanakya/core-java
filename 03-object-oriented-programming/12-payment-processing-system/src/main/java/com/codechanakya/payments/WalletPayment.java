package com.codechanakya.payments;

import java.math.BigDecimal;

/** A prepaid wallet. It can only spend what it holds. */
public class WalletPayment implements PaymentMethod {

    private final String owner;
    private BigDecimal balance;
    private int chargeCount = 0;

    public WalletPayment(String owner, BigDecimal balance) {
        this.owner = owner;
        this.balance = balance;
    }

    @Override
    public String name() {
        return "Wallet of " + owner;
    }

    @Override
    public PaymentResult charge(BigDecimal amount) {
        if (amount.compareTo(balance) > 0) {
            return PaymentResult.declined("Wallet balance is only " + balance.toPlainString());
        }
        balance = balance.subtract(amount);
        chargeCount++;
        return PaymentResult.approved(String.format("WAL-%04d", chargeCount));
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
