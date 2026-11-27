package com.codechanakya.bank;

class InMemoryBankStoreTest extends BankStoreContract {

    private final BankStore store = new InMemoryBankStore();

    @Override
    BankStore store() {
        return store;
    }
}
