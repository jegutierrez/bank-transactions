package ar.com.olx.service;

import ar.com.olx.entity.Transaction;
import ar.com.olx.entity.TransactionStatus;
import ar.com.olx.entity.TransactionType;
import ar.com.olx.util.TransactionLogger;

import java.util.concurrent.Callable;

/**
 * Created by jegutierrez on 08/10/16.
 */

public class AsyncTransaction implements Callable<Transaction> {

    Transaction transaction;

    private TransactionService transactionService;

    public AsyncTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public Transaction call() throws Exception {
        return process(transaction);
    }

    /**
     * Procesamiento de la transaccion en el servicio
     *
     * @param transaction
     * @return transaction
     */
    public Transaction process(Transaction transaction) {
        return transactionService.process(transaction);
    }

}
