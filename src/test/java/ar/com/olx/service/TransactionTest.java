package ar.com.olx.service;

import ar.com.olx.entity.Account;
import ar.com.olx.entity.Transaction;
import ar.com.olx.ws.AbstractTest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by jegutierrez on 09/10/16.
 */

@Transactional
public class TransactionTest extends AbstractTest {

    public static Account accountHsbsArgentina1;
    public static Account accountHsbsArgentina2;
    public static Account accountMacroArgentina;
    public static Account accountBanescoPanama;

    @Autowired
    public TransactionService transactionService;

    @Autowired
    public AccountService accountService;

    @Before
    public void setupAccounts(){
        accountHsbsArgentina1 = accountService.create(new Account("HSBC", "Argentina", 10000));
        accountHsbsArgentina2 = accountService.create(new Account("HSBC", "Argentina", 10000));
        accountMacroArgentina = accountService.create(new Account("Macro", "Argentina", 10000));
        accountBanescoPanama = accountService.create(new Account("Banesco", "Panama", 10000));
    }

    @After
    public void cleanAccounts(){
        accountService.delete(accountHsbsArgentina1.getId());
        accountService.delete(accountHsbsArgentina2.getId());
        accountService.delete(accountMacroArgentina.getId());
        accountService.delete(accountBanescoPanama.getId());
    }

    @Test
    public void taxNationalTransactionSameBank(){

        double transactionAmount = 100;

        Transaction newTransaction = new Transaction(
                accountHsbsArgentina1.getId(),
                accountHsbsArgentina2.getId(),
                transactionAmount);

        Transaction processedTransaction = transactionService.process(newTransaction);

        String msg = "failure - Tax between same back is expected to be cero";

        // Se espera que el impuesto de una transaccion del mismo banco sea cero
        Assert.assertEquals(msg, processedTransaction.getTax(), 0, 0);
    }

    @Test
    public void taxNationalTransaction(){

        double transactionAmount = 100;

        Transaction newTransaction = new Transaction(
                accountHsbsArgentina1.getId(),
                accountMacroArgentina.getId(),
                transactionAmount);

        Transaction processedTransaction = transactionService.process(newTransaction);

        String msg = "failure - Tax in a national transaction with " +
                "amount: " +transactionAmount+ " is expected to be 1%";

        // Se espera que el impuesto de una transaccion nacional sea 1%
        Assert.assertEquals(msg, processedTransaction.getTax(), 1, 0);
    }

    @Test
    public void taxInternationalTransaction(){

        double transactionAmount = 100;

        Transaction newTransaction = new Transaction(
                accountHsbsArgentina1.getId(),
                accountBanescoPanama.getId(),
                transactionAmount);

        Transaction processedTransaction = transactionService.process(newTransaction);

        String msg = "failure - Tax in a international transaction with " +
                "amount: " +transactionAmount+ " is expected to be 5%";

        // Se espera que el impuesto de una transaccion nacional sea 5%
        Assert.assertEquals(msg, processedTransaction.getTax(), 5, 0);
    }

}
